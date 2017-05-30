package com.packt.chapter10

import akka.actor.{Actor, ActorLogging, Cancellable}

import scala.concurrent.duration._

object MessageAkkaScheduler {
  case object JustOne
  case object Every2Seconds
}

class MessageAkkaScheduler extends Actor with ActorLogging {
  import MessageAkkaScheduler._
  import context.dispatcher
  var scheduleCancellable : Cancellable = _

  override def preStart() = {
    context.system.scheduler.scheduleOnce(2 seconds, self, JustOne)
    scheduleCancellable = context.system.scheduler.schedule(2 seconds, 1 second, self, Every2Seconds)
  }

  override def postStop() = scheduleCancellable.cancel()
  def receive = { case x => log.info(s"Received $x") }
}
