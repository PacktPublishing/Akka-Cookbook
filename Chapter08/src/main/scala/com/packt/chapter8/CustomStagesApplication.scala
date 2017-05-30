package com.packt.chapter8

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

object CustomStagesApplication extends App {

  implicit val actorSystem = ActorSystem("CustomStages")
  implicit val actorMaterializer = ActorMaterializer()

  val MaxGroups = 1000

  /**
    * Source stages
    */
  val source = Source.fromGraph(new HelloAkkaStreamsSource())

  /**
    * Processing stages
    */
  val upperCaseMapper = Flow[String].map(_.toUpperCase())
  val splitter = Flow[String].mapConcat(_.split(" ").toList)
  val punctuationMapper = Flow[String].map(_.replaceAll("""[\p{Punct}&&[^.]]""", "").replaceAll(System.lineSeparator(), ""))
  val filterEmptyElements = Flow[String].filter(_.nonEmpty)

  /**
    * Sink stages
    */
  val wordCounterSink = Sink.fromGraph(new WordCounterSink)

  val stream = source
    .via(upperCaseMapper)
    .via(splitter)
    .via(punctuationMapper)
    .via(filterEmptyElements)
    .to(wordCounterSink)

  stream.run()
}
