package org.jasoet.akka.mangatown

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.{RoundRobinPool, RoundRobinRouter}

import scala.collection.mutable.ArrayBuffer

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

class MasterActor extends Actor with ActorLogging {
  val mangaActor = context.actorOf(Props[MangaActor].withRouter(
    RoundRobinPool(nrOfInstances = 15)), name = "manga")
  val mangaChapterActor = context.actorOf(Props[MangaChapterActor].withRouter(
    RoundRobinPool(nrOfInstances = 15)), name = "manga_chapter")

  val mangas = ArrayBuffer.empty[MangaTown]

  override def receive: Receive = {
    case request@Request(url, startRange, endRange) =>
      mangaActor ! request
    case manga: MangaTown =>
      mangas += manga
      manga.chapters.foreach(c => {
        mangaChapterActor ! c
      })
    case chapter: MangaTownChapter =>
      val selectedManga = mangas.find(m => m.title.equalsIgnoreCase(chapter.title)).get
      val selectedChapter = selectedManga.chapters.find(mc => mc.url.equalsIgnoreCase(chapter.url)).get
      selectedChapter.pages = chapter.pages
      selectedChapter.completed = true

      if (selectedManga.chapters.forall(_.completed)) {
        selectedManga.completed = true
      }

    case Result(title) =>
      title.toLowerCase match {
        case "all" => sender ! mangas
        case _ =>
          val selectedManga = mangas.find(m => m.title.equalsIgnoreCase(title)).get
          sender ! selectedManga
      }

  }
}
