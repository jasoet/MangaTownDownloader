package org.jasoet.scala.mangatown

import java.net.URLEncoder
import java.nio.file.{DirectoryStream, Files, Path, Paths}

import scala.collection.JavaConverters._

/**
 * Created by Deny Prasetyo,S.T
 * Java(Script) Developer and Trainer
 * Software Engineer
 * jasoet87@gmail.com
 * <p/>
 * http://github.com/jasoet
 * http://bitbucket.com/jasoet
 *
 * @jasoet
 */

object TestRename extends App {


  val directoryStream: DirectoryStream[Path] = Files.newDirectoryStream(Paths.get("/Users/jasoet/Downloads/One Piece"))
  directoryStream.asScala.foreach { p =>
    println(p, p.resolveSibling(p.toFile.getName.replace("cbr", "cbz").replaceAll("[^a-zA-Z0-9 '\\.\\-]", "")))
    Files.move(p, p.resolveSibling(p.toFile.getName.replace("cbr", "cbz").replaceAll("[^a-zA-Z0-9 '\\.\\-]", "")))
  }

}
