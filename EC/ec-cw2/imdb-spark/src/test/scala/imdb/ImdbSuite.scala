package imdb

import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

class ImdbSuite extends AnyFunSuite with BeforeAndAfterAll {
  def initializeImdb(): Boolean =
    try {
      ImdbAnalysis
      true
    } catch {
      case ex: Throwable =>
        println(ex.getMessage)
        ex.printStackTrace()
        false
    }

  val INIT_ERR_MSG = " -- did you fill in all the values in ImdbAnalysis (titleBasicsList, titleRatingsList, titleCrewList, nameBasicsList)?"

  override def afterAll(): Unit = {
    assert(initializeImdb(), INIT_ERR_MSG)
  }

  /**
    * Creates a truncated string representation of a list, adding ", ...)" if there
    * are too many elements to show
    * @param l The list to preview
    * @param n The number of elements to cut it at
    * @return A preview of the list, containing at most n elements.
    */
  def previewList[A](l: List[A], n: Int = 10): String =
    if (l.length <= n) l.toString
    else l.take(n).toString.dropRight(1) + ", ...)"

  /**
    * Asserts that all the elements in a given list and an expected list are the same,
    * regardless of order. For a prettier output, given and expected should be sorted
    * with the same ordering.
    * @param given The actual list
    * @param expected The expected list
    * @tparam A Type of the list elements
    */
  def assertSameElements[A](given: List[A], expected: List[A]): Unit = {
    val givenSet = given.toSet
    val expectedSet = expected.toSet

    val unexpected = givenSet -- expectedSet
    val missing = expectedSet -- givenSet

    val noUnexpectedElements = unexpected.isEmpty
    val noMissingElements = missing.isEmpty

    val noMatchString =
      s"""
         |Expected: ${previewList(expected)}
         |Actual:   ${previewList(given)}""".stripMargin

    assert(noUnexpectedElements,
      s"""|$noMatchString
          |The given collection contains some unexpected elements: ${previewList(unexpected.toList, 5)}""".stripMargin)

    assert(noMissingElements,
      s"""|$noMatchString
          |The given collection is missing some expected elements: ${previewList(missing.toList, 5)}""".stripMargin)

    assert(given.size == expected.size, "Sizes do not match.")
  }

  def stringsToTsv(l: List[String]) = l.mkString("\t")
  val titleBasicsData = 
    List(List("t01","movie","Name1","Name 1","0","1999","\\N","1","Documentary,Short"),
        List("t02","movie","Name2","Name 2","0","1991","\\N","5","Animation,Short")
      ).map(stringsToTsv)
  val titleRatingsData = 
    List(List("t01","7.8","1156047"), 
      List("t02","8.2","300001")
      ).map(stringsToTsv)
  val titleBasicsData2 = 
    List(List("t03","movie","Name3","Name 3","0","2010","\\N","1","Documentary,Short"),
        List("t04","movie","Name4","Name 4","0","2018","\\N","5","Animation,Short"),
        List("t05","movie","Name5","Name 5","0","2019","\\N","3","Animation,Short")
      ).map(stringsToTsv)
  val titleCrewData = 
    List(List("t01", "\\N", "nm01,nm02"),
      List("t02", "\\N", "nm04"),
      List("t03", "\\N", "nm02,nm03"),
      List("t04", "\\N", "nm01,nm03"),
      List("t05", "\\N", "nm02,nm03")
    ).map(stringsToTsv)
  val nameBasicsData = 
    List(List("nm01", "FName1 SName1", "\\N", "\\N", "miscellaneous", "t01,t04"),
      List("nm02", "FName2 SName2", "\\N", "\\N", "miscellaneous", "t01,t03,t05"),
      List("nm03", "FName3 SName3", "\\N", "\\N", "miscellaneous", "t03,t04,t05"),
      List("nm04", "FName4 SName4", "\\N", "\\N", "miscellaneous", "t02")
      ).map(stringsToTsv)

  test("'task1' should work for a (specific) list with two elements") {
    assert(initializeImdb(), INIT_ERR_MSG)
    import ImdbAnalysis._
    import ImdbData._
    val list = titleBasicsData.map(parseTitleBasics)
    val expectedResult = 
      List((1.0, 1, 1, "Documentary"), 
           (3.0, 1, 5, "Short"),
           (5.0, 5, 5, "Animation"),
          )
    assertSameElements(task1(sc.parallelize(list)).collect().toList, expectedResult)
  }

  test("'task2' should work for (specific) lists with two elements") {
    assert(initializeImdb(), INIT_ERR_MSG)
    import ImdbAnalysis._
    import ImdbData._
    val list1 = titleBasicsData.map(parseTitleBasics)
    val list2 = titleRatingsData.map(parseTitleRatings)
    val res = task2(sc.parallelize(list1), sc.parallelize(list2)).collect().toList
    assertSameElements(res, List("Name1"))
  }

  test("'task3' should work for (specific) lists with two elements") {
    assert(initializeImdb(), INIT_ERR_MSG)
    import ImdbAnalysis._
    import ImdbData._
    val list1 = titleBasicsData.map(parseTitleBasics)
    val list2 = titleRatingsData.map(parseTitleRatings)
    val res = task3(sc.parallelize(list1), sc.parallelize(list2)).collect().toList
    val expectedResult = List((9, "Animation", "Name2"), 
      (9, "Documentary", "Name1"),
      (9, "Short", "Name2"))
    assert(res == expectedResult)
  }

  test("'task4' should work for small lists") {
    assert(initializeImdb(), INIT_ERR_MSG)
    import ImdbAnalysis._
    import ImdbData._
    val list1 = (titleBasicsData ++ titleBasicsData2).map(parseTitleBasics)
    val list2 = titleCrewData.map(parseTitleCrew)
    val list3 = nameBasicsData.map(parseNameBasics)
    val res = task4(sc.parallelize(list1), sc.parallelize(list2), sc.parallelize(list3)).collect().toList
    val expectedResult = List(("FName2 SName2", 2), ("FName3 SName3", 3))
    assertSameElements(res, expectedResult)
  }

}
