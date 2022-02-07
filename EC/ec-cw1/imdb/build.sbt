name := "hw1"

scalaVersion := "2.12.15"

scalacOptions ++= Seq("-deprecation")

resolvers += Resolver.sonatypeRepo("releases")

// grading libraries
libraryDependencies += "junit" % "junit" % "4.10" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % Test
