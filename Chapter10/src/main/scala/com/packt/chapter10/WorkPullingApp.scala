package com.packt.chapter10

import akka.actor.{ActorSystem, Props}

object WorkPullingApp extends App {
  val actorSystem = ActorSystem()
  val master = actorSystem.actorOf(Props(classOf[MasterWorkPulling], 2), "master")

  val worker1 = actorSystem.actorOf(Props(classOf[WorkerWorkPulling], master), "worker1")
  val worker2 = actorSystem.actorOf(Props(classOf[WorkerWorkPulling], master), "worker2")

  actorSystem.actorOf(Props(classOf[GeneratorWorkPulling], master), "generator")
}
