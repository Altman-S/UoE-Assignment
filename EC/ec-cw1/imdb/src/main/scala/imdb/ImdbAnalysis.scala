package imdb
import scala.io.Source

case class TitleBasics(tconst: String, titleType: Option[String], primaryTitle: Option[String],
                      originalTitle: Option[String], isAdult: Int, startYear: Option[Int], endYear: Option[Int],
                      runtimeMinutes: Option[Int], genres: Option[List[String]])
case class TitleRatings(tconst: String, averageRating: Float, numVotes: Int)
case class TitleCrew(tconst: String, directors: Option[List[String]], writers: Option[List[String]])
case class NameBasics(nconst: String, primaryName: Option[String], birthYear: Option[Int], deathYear: Option[Int],
                      primaryProfession: Option[List[String]], knownForTitles: Option[List[String]])

object ImdbAnalysis {

  // Hint: use a combination of `ImdbData.titleBasicsPath` and `ImdbData.parseTitleBasics`
  val titleBasicsList: List[TitleBasics] = readTsv(ImdbData.titleBasicsPath, ImdbData.parseTitleBasics)

  // Hint: use a combination of `ImdbData.titleRatingsPath` and `ImdbData.parseTitleRatings`
  val titleRatingsList: List[TitleRatings] = readTsv(ImdbData.titleRatingsPath, ImdbData.parseTitleRatings)

  // Hint: use a combination of `ImdbData.titleCrewPath` and `ImdbData.parseTitleCrew`
  val titleCrewList: List[TitleCrew] = readTsv(ImdbData.titleCrewPath, ImdbData.parseTitleCrew)

  // Hint: use a combination of `ImdbData.nameBasicsPath` and `ImdbData.parseNameBasics`
  val nameBasicsList: List[NameBasics] = readTsv(ImdbData.nameBasicsPath, ImdbData.parseNameBasics)

  // path: path of dataset
  def readTsv[A](path: String, fn: String => A): List[A] = {
    val lines = Source.fromFile(path: String).getLines().toList
    val data_list = lines.map(fn)
    data_list
  }



  def task1(list: List[TitleBasics]): List[(Float, Int, Int, String)] = {
    // put the tuple (genre,runtime) into genres_time_list
    val genres_time_list = list.flatMap(i => {
      if (i.genres.isDefined && i.runtimeMinutes.isDefined && i.titleType.isDefined) i.genres.get.map(j => (j, i.runtimeMinutes.get))
      else List()
    })

    // groupBy(genre)     List[(String, Int)] -> Map[String, List[(String, Int)]] or Map[genre, timeList]
    val genres_time_map = genres_time_list.groupBy(_._1)

    // transform (genres,runtime) to 'runtime'    Map[String, List[(String, Int)]] -> Map[String, List[Int]]
    val num_list = genres_time_map.mapValues(i => i.map(j => j._2))
    val result = num_list.map(i => (i._2.sum.toFloat/i._2.size.toFloat, i._2.min, i._2.max, i._1)).toList
//    val result = genres_time_map.map(i => {
//      (i._2.map(a=>a._2).sum.toFloat/i._2.size.toFloat, i._2.map(b=>b._2).min, i._2.map(c=>c._2).max, i._1)
//    })

    result
  }

  def task2(l1: List[TitleBasics], l2: List[TitleRatings]): List[String] = {
    // get Map(tconst, 1/0)   List[TitleRatings] -> Map[String, Int]
    val tconst_map = l2.flatMap(i => {
      if (i.averageRating>=7.5 && i.numVotes>=500000) List((i.tconst,1))
      else List()
    }).toMap

    // find the result that satisfies the condition    List[TitleBasics] -> List[String]
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

  def task3(l1: List[TitleBasics], l2: List[TitleRatings]): List[(Int, String, String)] = {
    // Map[tconst -> averageRating]     Map[String, Float]
    val tconst_rating_map = l2.map(i => (i.tconst, i.averageRating)).toMap

    // List[Titlebasic] -> List[(Int, String, String, Float)] or List[(decade, genre, primaryTitle, averageRating)]
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

    // List[(decade, genre, primaryTitle, averageRating)] ->
    // Map[decade -> List(decade, genre, primaryTitle, averageRating)] ->                 len=10
    // List[(decade, List(decade, genre, primaryTitle, averageRating))] -> sortBy(decade)
    val decade_list = decade_genre_list.groupBy(_._1).toList.sortBy(_._1)

    // List[(decade, List[(decade, genre, primaryTitle, averageRating)])]->
    // List[(decade, Map[genre -> (decade, genre, primaryTitle, averageRating)])] ->      len=10
    // List[(decade, List[(genre, List(decade, genre, primaryTitle, averageRating))])] -> sortBy(genre)
    val d_g_list = decade_list.map(i => (i._1, i._2.groupBy(_._2).toList.sortBy(_._1)))

    // len = len(genre1) + len(genre2) + ...
    // List[(Int, String, String, Float)]
    val result0 = d_g_list.flatMap(i => i._2.map(j => j._2.maxBy(a => a._4)))

    val result = result0.map(i => (i._1, i._2, i._3))

    result
  }

  // Hint: There could be an input list that you do not really need in your implementation.
  def task4(l1: List[TitleBasics], l2: List[TitleCrew], l3: List[NameBasics]): List[(String, Int)] = {
    // get Map(tconst, 1/0)   List[TitleBasics] -> Map[String, Int]
    val tconst_start_map = l1.flatMap(i => {
      if (i.startYear.isDefined) {
        if (i.startYear.get >= 2010) List((i.tconst,1))
        else List()
      }
      else List()
    }).toMap

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

    val result = name_num_list.flatMap(i => {
      if (i.size >= 2) List((i.head._1, i.size))
      else List()
    })

    result
  }

  def main(args: Array[String]) {
    val durations = timed("Task 1", task1(titleBasicsList))
    val titles = timed("Task 2", task2(titleBasicsList, titleRatingsList))
    val topRated = timed("Task 3", task3(titleBasicsList, titleRatingsList))
    val crews = timed("Task 4", task4(titleBasicsList, titleCrewList, nameBasicsList))
    println(durations)
    println(titles)
    println(topRated)
    println(crews)
    println(timing)
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
