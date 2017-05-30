package com.packt.chapter8

import akka.actor.{Actor, Props}
import akka.stream.scaladsl.SourceQueueWithComplete

import scala.concurrent.duration._

object SourceActor {
  case object Tick
  def props(sourceQueue: SourceQueueWithComplete[String]) = Props(new SourceActor(sourceQueue))
}

class SourceActor(sourceQueue: SourceQueueWithComplete[String]) extends Actor {
  import SourceActor._
  import context.dispatcher

  override def preStart() = {
    context.system.scheduler.schedule(0 seconds, 5 seconds, self, Tick)
  }

  def receive = {
    case Tick =>
      println(s"Offering element from SourceActor")
      sourceQueue.offer("Integrating!!### Akka$$$ Actors? with}{ Akka** Streams")
  }

}
