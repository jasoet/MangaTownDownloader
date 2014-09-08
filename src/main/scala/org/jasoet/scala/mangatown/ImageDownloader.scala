package org.jasoet.scala.mangatown

import java.net.URL
import java.nio.file.{Files, Paths}
import javax.imageio.ImageIO


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

class ImageDownloader(localImagePath: String) {

  if (Files.notExists(Paths.get(localImagePath)) || !Files.isDirectory(Paths.get(localImagePath))) {
    Files.createDirectories(Paths.get(localImagePath))
  }

  def downloadImage(url: String, fileName: String) = {
    val cleanUrl = url.substring(0, url.lastIndexOf("?"))
    val imageFormat = cleanUrl.substring(url.lastIndexOf(".") + 1)
    val imageUrl = new URL(url)
    val path = Paths.get(localImagePath, s"$fileName.$imageFormat")
    val pathFile = path.toFile
    println(s"Download to ${pathFile.getAbsolutePath}")
    val image = ImageIO.read(imageUrl)
    ImageIO.write(image, imageFormat, pathFile)

    pathFile.exists()
  }

}

object ImageDownloader {
  def apply(localImagePath: String) = new ImageDownloader(localImagePath)
}
