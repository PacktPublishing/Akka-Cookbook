package com.packt.chapter6

import akka.actor.{ActorSystem, Props}

object SnapshotApp extends App {
  val system = ActorSystem("snapshot")
  val persistentActor1 = system.actorOf(Props[SnapshotActor])

  persistentActor1 ! UserUpdate("user1", Add)
  persistentActor1 ! UserUpdate("user2", Add)
  persistentActor1 ! "snap"

  Thread.sleep(2000)
  system.stop(persistentActor1)

  val persistentActor2 = system.actorOf(Props[SnapshotActor])

  Thread.sleep(2000)
  system.terminate()
}
