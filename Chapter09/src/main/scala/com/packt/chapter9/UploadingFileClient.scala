package com.packt.chapter9

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object UploadingFileClient extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val http = Http()
  val entity = Multipart.FormData.fromPath(
    "file",
    ContentTypes.`text/plain(UTF-8)`,
    Paths.get("./src/main/resources/testfile.txt")
  ).toEntity()
  val uris = Seq(
    "http://localhost:8088/regularupload",
    "http://localhost:8088/streamupload"
  )
  val requests = uris.map(uri => HttpRequest(POST, uri, Nil, entity))

  Future.traverse(requests)(http.singleRequest(_)) andThen {
    case Success(responses) => responses.foreach(response =>
      response.entity.toStrict(5 seconds).map(_.data.utf8String).andThen {
        case Success(content) => println(s"Response: $content")
        case _ =>
      })
    case Failure(e) => println(s"request failed $e")
  }
}
