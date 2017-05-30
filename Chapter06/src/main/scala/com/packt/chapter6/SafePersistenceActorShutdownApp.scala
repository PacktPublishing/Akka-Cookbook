package com.packt.chapter6

import akka.actor.{ActorSystem, PoisonPill, Props}

object SafePersistenceActorShutdownApp extends App {
  val system = ActorSystem("safe-shutdown")
  val persistentActor1 = system.actorOf(Props[SamplePersistenceActor])
  val persistentActor2 = system.actorOf(Props[SamplePersistenceActor])

  persistentActor1 ! UserUpdate("foo", Add)
  persistentActor1 ! UserUpdate("foo", Add)
  persistentActor1 ! PoisonPill
  persistentActor2 ! UserUpdate("foo", Add)
  persistentActor2 ! UserUpdate("foo", Add)
  persistentActor2 ! ShutdownPersistentActor
}
