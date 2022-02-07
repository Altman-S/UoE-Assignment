package imdb

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD


case class TitleBasics(tconst: String, titleType: Option[String], primaryTitle: Option[String],
                      originalTitle: Option[String], isAdult: Int, startYear: Option[Int], endYear: Option[Int],
                      runtimeMinutes: Option[Int], genres: Option[List[String]]) {
  def getGenres(): List[String] = genres.getOrElse(List[String]())
}
case class TitleRatings(tconst: String, averageRating: Float, numVotes: Int)
case class TitleCrew(tconst: String, directors: Option[List[String]], writers: Option[List[String]])
case class NameBasics(nconst: String, primaryName: Option[String], birthYear: Option[Int], deathYear: Option[Int],
                      primaryProfession: Option[List[String]], knownForTitles: Option[List[String]])

object ImdbAnalysis {

  val conf: SparkConf = new SparkConf().setMaster("local").setAppName("appName")
  val sc: SparkContext = new SparkContext(conf)

  // path: path of dataset
  def readTsv(path: String): RDD[String] = {
    val data = sc.textFile(path)
    data
  }

  // Hint: use a combination of `ImdbData.titleBasicsPath` and `ImdbData.parseTitleBasics`
  val titleBasicsRDD: RDD[TitleBasics] = readTsv(ImdbData.titleBasicsPath).map(ImdbData.parseTitleBasics)

  // Hint: use a combination of `ImdbData.titleRatingsPath` and `ImdbData.parseTitleRatings`
  val titleRatingsRDD: RDD[TitleRatings] = readTsv(ImdbData.titleRatingsPath).map(ImdbData.parseTitleRatings)

  // Hint: use a combination of `ImdbData.titleCrewPath` and `ImdbData.parseTitleCrew`
  val titleCrewRDD: RDD[TitleCrew] = readTsv(ImdbData.titleCrewPath).map(ImdbData.parseTitleCrew)

  // Hint: use a combination of `ImdbData.nameBasicsPath` and `ImdbData.parseNameBasics`
  val nameBasicsRDD: RDD[NameBasics] = readTsv(ImdbData.nameBasicsPath).map(ImdbData.parseNameBasics)


  def task1(rdd: RDD[TitleBasics]): RDD[(Float, Int, Int, String)] = {
    // put the tuple (genre,runtime) into RDD     RDD[titleBasics] -> RDD[(genre, runtime)] / RDD[(String, Int)]
    val genres_time = rdd.flatMap(i => {
      if (i.genres.isDefined && i.runtimeMinutes.isDefined && i.titleType.isDefined) i.genres.get.map(j => (j, i.runtimeMinutes.get))
      else List()
    })

    // groupBy(genre)    RDD[(String, Int)] -> RDD[String, CompactBuffer[(String, Int)]]
    val genres_time_map = genres_time.groupBy(_._1)

    // transform (genres,runtime) to 'runtime'    RDD[String, CompactBuffer[(String, Int)]] -> RDD[String, List[Int]]
    val num_list = genres_time_map.mapValues(i => i.map(j => j._2))
    val result = num_list.map(i => (i._2.sum.toFloat/i._2.size.toFloat, i._2.min, i._2.max, i._1))

    result
  }

  def task2(l1: RDD[TitleBasics], l2: RDD[TitleRatings]): RDD[String] = {
    // get Map(tconst, 1/0)   RDD[TitleRatings] -> Map[String, Int]
    val tconst_map = l2.flatMap(i => {
      if (i.averageRating>=7.5 && i.numVotes>=500000) List((i.tconst,1))
      else List()
    }).collect().toMap

    // find the result that satisfies the condition    RDD[TitleBasics] -> RDD[String]
    val result = l1.flatMap(i => { // primaryTitle!=None
      if (i.startYear.isDefined && i.titleType.isDefined && i.primaryTitle.isDefined) {
        if (i.startYear.get >= 1990 && i.startYear.get <= 2018) {
          if (i.titleType.get == "movie" && tconst_map.contains(i.tconst)) List(i.primaryTitle.get)
          else List()
        }
        else List()
      }
      else List()
    })

    result
  }

  def task3(l1: RDD[TitleBasics], l2: RDD[TitleRatings]): RDD[(Int, String, String)] = {
    // Map[tconst -> averageRating]     Map[String, Float]
    val tconst_rating_map = l2.map(i => (i.tconst, i.averageRating)).collect().toMap

    // RDD[Titlebasic] -> RDD[(Int, String, String, Float)] or RDD[(decade, genre, primaryTitle, averageRating)]
    val decade_genre_list = l1.flatMap(i => {
      if (i.titleType.isDefined && i.primaryTitle.isDefined && i.startYear.isDefined && i.genres.isDefined) {
        if (i.titleType.get == "movie" && i.startYear.get >= 1900 && i.startYear.get <= 1999) {
          if (tconst_rating_map.contains(i.tconst)) i.genres.get.map(j => {
            ((i.startYear.get-1900)/10, j, i.primaryTitle.get, tconst_rating_map(i.tconst))
          })
          else List()
        }
        else List()
      }
      else List()
    }).sortBy(_._3)

    // RDD[(decade, genre, primaryTitle, averageRating)] ->
    // RDD[(decade -> Iterable[(decade, genre, primaryTitle, averageRating)])] -> sortBy(decade)       len=10
    val decade_list = decade_genre_list.groupBy(_._1).sortBy(_._1)

    // RDD[(decade, Iterable[(decade, genre, primaryTitle, averageRating)])]->
    // RDD[(decade, Map[genre -> (decade, genre, primaryTitle, averageRating)])] ->      len=10
    // RDD[(decade, List[(genre, Iterable[(decade, genre, primaryTitle, averageRating)])])] -> sortBy(genre)
    val d_g_list = decade_list.map(i => (i._1, i._2.groupBy(_._2).toList.sortBy(_._1)))

    // len = len(genre1) + len(genre2) + ...
    // RDD[(Int, String, String, Float)]
    val result0 = d_g_list.flatMap(i => i._2.map(j => j._2.maxBy(a => a._4)))

    val result = result0.map(i => (i._1, i._2, i._3))

    result
  }

  // Hint: There could be an input RDD that you do not really need in your implementation.
  def task4(l1: RDD[TitleBasics], l2: RDD[TitleCrew], l3: RDD[NameBasics]): RDD[(String, Int)] = {
    // get Map(tconst, 1/0)   RDD[TitleBasics] -> Map[String, Int]
    val tconst_start_map = l1.flatMap(i => {
      if (i.startYear.isDefined) {
        if (i.startYear.get >= 2010) List((i.tconst,1))
        else List()
      }
      else List()
    }).collect().toMap

    // get List((crew, 1), (crew, 1))     RDD[NameBasics] -> RDD[List[(String, Int)]]
    val name_num_list = l3.map(i => {
      if (i.knownForTitles.isDefined && i.primaryName.isDefined) {
        if (i.knownForTitles.get.size >= 2) i.knownForTitles.get.flatMap(j => {
          if (tconst_start_map.contains(j)) List((i.primaryName.get, 1))
          else List()
        })
        else List()
      }
      else List()
    }).filter(j => j.nonEmpty)

    // RDD[List[(String, Int)]] -> RDD[(String, Int)]
    val result = name_num_list.flatMap(i => {
      if (i.size >= 2) List((i.head._1, i.size))
      else List()
    })

    result
  }

  def main(args: Array[String]) {
    val durations = timed("Task 1", task1(titleBasicsRDD).collect().toList)
    val titles = timed("Task 2", task2(titleBasicsRDD, titleRatingsRDD).collect().toList)
    val topRated = timed("Task 3", task3(titleBasicsRDD, titleRatingsRDD).collect().toList)
    val crews = timed("Task 4", task4(titleBasicsRDD, titleCrewRDD, nameBasicsRDD).collect().toList)
    println(durations)
    println(titles)
    println(topRated)
    println(crews)
    println(timing)
    sc.stop()
  }

  val timing = new StringBuffer
  def timed[T](label: String, code: => T): T = {
    val start = System.currentTimeMillis()
    val result = code
    val stop = System.currentTimeMillis()
    timing.append(s"Processing $label took ${stop - start} ms.\n")
    result
  }
}
