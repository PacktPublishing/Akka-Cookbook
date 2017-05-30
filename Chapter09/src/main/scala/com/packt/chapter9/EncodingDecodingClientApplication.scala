package com.packt.chapter9

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.coding.{Encoder, Gzip, NoCoding}
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.headers.HttpEncodings._
import akka.http.scaladsl.model.HttpMethods._
import headers.HttpEncodings
import akka.stream.ActorMaterializer
import akka.util.ByteString

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.util.{Failure, Success}

object EncodingDecodingClientApplication extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val http = Http()
  val uriServer = "http://localhost:8088/"

  val requests = Seq (
    HttpRequest(POST, uriServer, List(`Accept-Encoding`(gzip)), HttpEntity("Hello!")),
    HttpRequest(POST, uriServer, List(`Content-Encoding`(gzip), `Accept-Encoding`(gzip)), HttpEntity(compress("Hello compressed!", Gzip))
    )
  )

  Future.traverse(requests)(http.singleRequest(_).map(decodeResponse)) andThen {
    case Success(responses) => responses.foreach(response =>
      response.entity.toStrict(5 seconds).map(_.data.decodeString("UTF-8")).andThen {
        case Success(content) => println(s"Response: $content")
        case _ =>
      })
    case Failure(e) => println(s"request failed $e")
  }

  private def decodeResponse(response: HttpResponse) = {
    val decoder = response.encoding match {
      case HttpEncodings.gzip => Gzip
      case HttpEncodings.identity => NoCoding
    }

    decoder.decode(response)
  }

  private def compress(input: String, encoder: Encoder): ByteString =
    encoder.encode(ByteString(input))
}
