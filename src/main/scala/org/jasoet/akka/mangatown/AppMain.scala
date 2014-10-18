package org.jasoet.akka.mangatown

import akka.actor.{Props, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Await
import scala.concurrent.duration._


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

object AppMain extends App {
  val _system = ActorSystem("MangaDownloader")
  val masterActor = _system.actorOf(Props[MasterActor], name = "manga")

  implicit val timeout = Timeout(15 minutes)
  masterActor ! Request("http://www.mangatown.com/manga/one_piece/", 0, 2)
  masterActor ! Request("http://www.mangatown.com/manga/naruto/", 0, 2)
  masterActor ! Request("http://www.mangatown.com/manga/bleach/", 0, 2)
  masterActor ! Request("http://www.mangatown.com/manga/full_metal_wing/", 0, 2)
  Thread.sleep(10000)
  val future = (masterActor ? Result("all")).mapTo[ArrayBuffer[MangaTown]]
  val results = Await.result(future, timeout.duration)
  results.foreach(result => {
    println(s"${result.title} - ${result.url}")
    result.chapters.foreach(c => {
      println(s"${c.title} - ${c.number}- ${c.url}")
    })
  })
  _system.shutdown()
}
