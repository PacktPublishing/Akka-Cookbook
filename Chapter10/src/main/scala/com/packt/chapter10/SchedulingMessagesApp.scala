package com.packt.chapter10

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory


object SchedulingMessagesApp extends App {
  val c = """akka.quartz.schedules.Every2Seconds.expression = "*/2 * * ? * *""""

  val actorSystem = ActorSystem("scheduling", ConfigFactory.parseString(c))
  actorSystem.actorOf(Props[MessageAkkaScheduler], "MessageAkkaScheduler")
  actorSystem.actorOf(Props[MessageQuartzScheduler], "MessageQuartzScheduler")

  Thread.sleep(10000)

  actorSystem.terminate()
}
