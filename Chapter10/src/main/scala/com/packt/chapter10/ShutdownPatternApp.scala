package com.packt.chapter10

import akka.actor.{ActorSystem, PoisonPill, Props}

import scala.concurrent.duration._

object ShutdownPatternApp extends App {
  val actorSystem = ActorSystem()
  import actorSystem.dispatcher
  val reaper = actorSystem.actorOf(Props[Reaper], "Reaper")
  val worker = actorSystem.actorOf(Props[ShutdownPatternWorker], "worker")
  val master = actorSystem.actorOf(Props[ShutdownPatternMaster], "master")
  actorSystem.scheduler.scheduleOnce(3 seconds, worker, PoisonPill)
  actorSystem.scheduler.scheduleOnce(5 seconds, master, PoisonPill)
}
