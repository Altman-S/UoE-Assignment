package imdb

import java.io.File

object ImdbData {
  private val SKIP_VAL = "\\N"

  private[imdb] def filePath(name: String) = {
    val resource = this.getClass.getClassLoader.getResource(s"imdb/$name.tsv")
    if (resource == null) sys.error("Please download the dataset as explained in the assignment instructions.")
    new File(resource.toURI).getPath
  }

  private[imdb] def titleBasicsPath = filePath("title.basics")
  private[imdb] def titleRatingsPath = filePath("title.ratings")
  private[imdb] def titleCrewPath = filePath("title.crew")
  private[imdb] def nameBasicsPath = filePath("name.basics")

  private[imdb] def parseAttribute(word: String): Option[String] = 
    if(word == SKIP_VAL) None else Some(word)

  private[imdb] def parseTitleBasics(line: String): TitleBasics = {
    val attrs = line.split("\t").map(parseAttribute)
    if(attrs.length != 9)
      sys.error("Error in the format of `title.basics.tsv`.")
    TitleBasics(attrs(0).get, attrs(1), attrs(2), 
               attrs(3), attrs(4).get.toInt, attrs(5).map(_.toInt), attrs(6).map(_.toInt),
               attrs(7).map(_.toInt), attrs(8).map(_.split(",").toList))
  }

  private[imdb] def parseTitleRatings(line: String): TitleRatings = {
    val attrs = line.split("\t").map(parseAttribute)
    if(attrs.length != 3)
      sys.error("Error in the format of `title.ratings.tsv`.")
    TitleRatings(attrs(0).get, attrs(1).get.toFloat, attrs(2).get.toInt)
  }

  private[imdb] def parseTitleCrew(line: String): TitleCrew = {
    val attrs = line.split("\t").map(parseAttribute)
    if(attrs.length != 3)
      sys.error("Error in the format of `title.crew.tsv`.")
    TitleCrew(attrs(0).get, attrs(1).map(_.split(",").toList), attrs(2).map(_.split(",").toList))
  }

  private[imdb] def parseNameBasics(line: String): NameBasics = {
    val attrs = line.split("\t").map(parseAttribute)
    if(attrs.length != 6)
      sys.error("Error in the format of `name.basics.tsv`.")
    NameBasics(attrs(0).get, attrs(1), attrs(2).map(_.toInt), attrs(3).map(_.toInt),
               attrs(4).map(_.split(",").toList), attrs(5).map(_.split(",").toList))
  }
}
