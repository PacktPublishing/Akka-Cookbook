package com.packt.chapter8

import java.io.File

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

object ComposingStreamsApplication extends App {

  implicit val actorSystem = ActorSystem("SimpleStream")
  implicit val actorMaterializer = ActorMaterializer()

  val fileList = List(
    "src/main/resources/testfile1.text",
    "src/main/resources/testfile2.txt",
    "src/main/resources/testfile3.txt")

  val stream = Source(fileList)
    .map(new File(_))
    .filter(_.exists())
    .filter(_.length() != 0)
    .to(Sink.foreach(f => println(s"Absolute path: ${f.getAbsolutePath}")))

  stream.run()
}
