package com.packt.chapter10

import java.util.UUID

import akka.actor.{ActorSystem, Props}

object EnvelopingActorApp extends App {

  val actorSystem = ActorSystem()
  val envelopReceived = actorSystem.actorOf(Props[EnvelopeReceiver], "receiver")
  val envelopingActor = actorSystem.actorOf(Props(classOf[EnvelopingActor], envelopReceived, headers _))
  envelopingActor ! "Hello!"

  def headers(msg: Any) = {
    Map(
      "timestamp" -> System.currentTimeMillis(),
      "correlationId" -> UUID.randomUUID().toString
    )
  }
}
