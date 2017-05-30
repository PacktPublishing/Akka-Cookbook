package com.packt.chapter8

import akka.actor.{ActorSystem, Props}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl._
import akka.pattern.ask
import akka.util.Timeout
import com.packt.chapter8.SinkActor.{AckSinkActor, CompletedSinkActor, InitSinkActor}

import scala.concurrent.duration._

object IntegratingWithActorsApplication extends App {

  implicit val actorSystem = ActorSystem("IntegratingWithActors")
  implicit val actorMaterializer = ActorMaterializer()

  implicit val askTimeout = Timeout(5 seconds)
  val stringCleaner = actorSystem.actorOf(Props[StringCleanerActor])
  val sinkActor = actorSystem.actorOf(Props[SinkActor])

  val source = Source.queue[String](100, OverflowStrategy.backpressure)
  val sink = Sink.actorRefWithAck[String](sinkActor, InitSinkActor, AckSinkActor, CompletedSinkActor)
  val queue = source
    .mapAsync(parallelism = 5)(elem => (stringCleaner ? elem).mapTo[String])
    .to(sink)
    .run()

  actorSystem.actorOf(SourceActor.props(queue))
}
