import sbtassembly.Plugin.AssemblyKeys._

// put this at the top of the file

assemblySettings

jarName in assembly := "mangatown-sedoter.jar"

mainClass in assembly := Some("org.jasoet.scala.mangatown.TownScrapMain")

name := "MangaTownScrap"

version := "1.0"

publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath + "/.m2/repository")))

scalaVersion := "2.11.1"

libraryDependencies += "org.jsoup" % "jsoup" % "1.7.3"

libraryDependencies += "com.google.code.gson" % "gson" % "2.2.4"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.3.2"