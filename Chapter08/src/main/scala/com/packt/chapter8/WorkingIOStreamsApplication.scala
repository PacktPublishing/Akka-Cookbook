package com.packt.chapter8

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Tcp.{IncomingConnection, ServerBinding}
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.Future

object WorkingIOStreamsApplication extends App {

  implicit val actorSystem = ActorSystem("WorkingIOStreams")
  implicit val actorMaterializer = ActorMaterializer()

  val MaxGroups = 1000

  val connections = Tcp().bind("127.0.0.1", 1234)
  connections.runForeach(connection => connection.handleWith(wordCount))

  val wordCount = Flow[ByteString].map(_.utf8String.toUpperCase)
    .mapConcat(_.split(" ").toList)
    .collect { case w if w.nonEmpty =>
      w.replaceAll("""[p{Punct}&&[^.]]""", "").replaceAll(System.lineSeparator(), "") }
    .groupBy(MaxGroups, identity)
    .map(_ -> 1)
    .reduce((l, r) => (l._1, l._2 + r._2))
    .mergeSubstreams
    .map(x => ByteString(s"[${x._1} => ${x._2}]\n"))
}
