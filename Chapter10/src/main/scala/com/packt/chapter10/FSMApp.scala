package com.packt.chapter10

import akka.actor.{ActorSystem, Props}

object FSMApp extends App {
  val actorSystem = ActorSystem()
  val changeSubscriber = actorSystem.actorOf(Props[FSMChangeSubscriber], "FSMChangeSubscriber")
  actorSystem.actorOf(Props(classOf[TrafficLightFSM], changeSubscriber), "trafficLight")
}
