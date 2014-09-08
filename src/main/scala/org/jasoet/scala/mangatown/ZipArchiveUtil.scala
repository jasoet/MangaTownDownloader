package org.jasoet.scala.mangatown

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

import java.io.{File, FileInputStream, FileOutputStream, IOException}
import java.util.zip.{ZipEntry, ZipFile, ZipOutputStream}

import scala.collection.JavaConversions.enumerationAsScalaIterator
import scala.io.{BufferedSource, Codec}

object ZipArchiveUtil {

  def createFileList(file: File, outputFilename: String): List[String] = {
    file match {
      case fileIx if fileIx.isFile => {
        if (fileIx.getName != outputFilename)
          List(fileIx.getAbsoluteFile.toString)
        else
          List()
      }
      case fileIx if fileIx.isDirectory => {
        val fList = fileIx.list
        // Add all files in current dir to list and recur on subdirs
        fList.foldLeft(List[String]())((pList: List[String], path: String) =>
          pList ++ createFileList(new File(fileIx, path), outputFilename))
      }
      case _ => throw new IOException("Bad path. No file or directory found.")
    }
  }

  def addFileToZipEntry(filename: String, parentPath: String,
                        filePathsCount: Int): ZipEntry = {
    if (filePathsCount <= 1)
      new ZipEntry(new File(filename).getName)
    else {
      // use relative path to avoid adding absolute path directories
      val relative = new File(parentPath).toURI.
        relativize(new File(filename).toURI).getPath
      new ZipEntry(relative)
    }
  }

  def createZip(filePaths: List[String], outputFilename: String,
                parentPath: String) = {
    try {
      val fileOutputStream = new FileOutputStream(outputFilename)
      val zipOutputStream = new ZipOutputStream(fileOutputStream)

      filePaths.foreach((name: String) => {
        println("adding " + name)
        val zipEntry = addFileToZipEntry(name, parentPath, filePaths.size)
        zipOutputStream.putNextEntry(zipEntry)
        val inputSrc = new BufferedSource(
          new FileInputStream(name))(Codec.ISO8859)
        inputSrc foreach { c: Char => zipOutputStream.write(c)}
        inputSrc.close()
      })

      zipOutputStream.closeEntry()
      zipOutputStream.close()
      fileOutputStream.close()

    } catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }

  def unzip(file: File): Unit = {
    val basename = file.getName.substring(0, file.getName.lastIndexOf("."))
    val destDir = new File(file.getParentFile, basename)
    destDir.mkdirs

    val zip = new ZipFile(file)
    zip.entries foreach { entry =>
      val entryName = entry.getName
      val entryPath = {
        if (entryName.startsWith(basename))
          entryName.substring(basename.length)
        else
          entryName
      }

      // create output directory if it doesn't exist already
      val splitPath = entry.getName.split(File.separator).dropRight(1)
      if (splitPath.size >= 1) {
        // create intermediate directories if they don't exist
        val dirBuilder = new StringBuilder(destDir.getName)
        splitPath.foldLeft(dirBuilder)((a: StringBuilder, b: String) => {
          val path = a.append(File.separator + b)
          val str = path.mkString
          if (!new File(str).exists) {
            new File(str).mkdir
          }
          path
        })
      }

      // write file to dest
      println("Extracting " + destDir + File.separator + entryPath)
      val inputSrc = new BufferedSource(
        zip.getInputStream(entry))(Codec.ISO8859)
      val ostream = new FileOutputStream(new File(destDir, entryPath))
      inputSrc foreach { c: Char => ostream.write(c)}
      inputSrc.close()
      ostream.close()
    }
  }

  def main(args: Array[String]) = {
    if (args.length == 1 && args(0).split('.').last == "zip") {
      unzip(new File(args(0)))
    } else if (args.length == 2) {
      val path = args(0)
      val outputFilename = args(1)
      val file = new File(path)
      val filePaths = createFileList(file, outputFilename)
      createZip(filePaths, outputFilename, path)
    }
    else {
      val err = "\nZip Usage: <source> <dest>" +
        "\n" + "Unzip Usage: <file.zip>\n"
      throw new IllegalArgumentException(err)
    }
  }
}