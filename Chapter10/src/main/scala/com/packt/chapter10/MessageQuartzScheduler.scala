package com.packt.chapter10

import akka.actor.{Actor, ActorLogging}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

object MessageQuartzScheduler {
  case object Every2Seconds
}

class MessageQuartzScheduler extends Actor with ActorLogging {
  import MessageQuartzScheduler._

  override def preStart() = {
    QuartzSchedulerExtension(context.system).schedule("Every2Seconds", self, Every2Seconds)
  }

  override def postStop() = {
    QuartzSchedulerExtension(context.system).cancelJob("Every2Seconds")
  }

  def receive = {
    case x => log.info(s"Received $x")
  }
}
