package org.jasoet.akka.mangatown

/**
 * Deny Prasetyo,S.T
 * Java(Scala) Developer and Trainer
 * Software Engineer | Java Virtual Machine Junkie!
 * jasoet87@gmail.com
 * <p/>
 * http://github.com/jasoet
 * http://bitbucket.com/jasoet
 *
 */
case class MangaTownChapterPage(number: String, imageUrl: String)

case class MangaTownChapter(title: String, name: String, number: Double, url: String, var pages: List[MangaTownChapterPage], var completed: Boolean = false)

case class MangaTown(title: String, url: String, chapters: List[MangaTownChapter], var completed: Boolean = false)

case class Result(title: String)

case class Request(url: String, start: Double, end: Double)

