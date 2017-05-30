package com.packt.chapter10

import akka.actor.{ActorSystem, Props}
import scala.concurrent.duration._
import scala.concurrent.Await

object OrderedTerminationApp extends App {

  val actorSystem = ActorSystem()
  val orderedKiller = actorSystem.actorOf(Props[ServiceHandlersCreator], "serviceHandlersCreator")
  val servicesManager = actorSystem.actorOf(Props(classOf[ServicesManager], orderedKiller), "servicesManager")
  //...
  Thread.sleep(2000)
  actorSystem.stop(servicesManager)
  Await.ready(actorSystem.terminate(), 2.seconds)
}
