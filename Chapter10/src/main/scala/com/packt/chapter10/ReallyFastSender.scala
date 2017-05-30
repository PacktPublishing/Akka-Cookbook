package com.packt.chapter10

import akka.actor.{Actor, ActorLogging, ActorRef}

import scala.concurrent.duration._
import scala.util.Random

object ReallyFastSender {
  case object Tick
  case class ReallyImportantMessage(i: Int)
}

class ReallyFastSender(nextActor: ActorRef) extends Actor with ActorLogging {
  import ReallyFastSender._
  import context.dispatcher

  override def preStart() = {
    context.system.scheduler.schedule(2 second, 200 millis, self, Tick)
  }

  def receive = {
    case Tick =>
      val msg = ReallyImportantMessage(Random.nextInt(100))
      log.info(s"Sending $msg")
      nextActor ! msg
  }
}
