MangaTownDownloader
===================

MangaTown Downloader using Scala, Step for Using

* Install Java 8 [Download](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* Install Sbt [http://www.scala-sbt.org/release/tutorial/Setup.html](http://www.scala-sbt.org/release/tutorial/Setup.html)
* Run `sbt clean assembly`
* Binary available on `target/scala-2.11/mangatown-sedoter.jar`
* Usage `java -jar mangatown-sedoter.jar <URL Manga> <Start Chapter> <End Chapter> <Destination>`
* Example `java -jar mangatown-sedoter.jar http://www.mangatown.com/manga/one_piece/ 12 17 /Users/jasoet/Downloads`
