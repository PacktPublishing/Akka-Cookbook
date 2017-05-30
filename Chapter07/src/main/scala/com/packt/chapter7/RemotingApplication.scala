package com.packt.chapter7

import akka.actor.{ActorSystem, Props}

object HelloAkkaRemoting1 extends App {
  val actorSystem = ActorSystem("HelloAkkaRemoting1")
}

object HelloAkkaRemoting2 extends App {
  val actorSystem = ActorSystem("HelloAkkaRemoting2")
  println("Creating actor from HelloAkkaRemoting2")
  val actor = actorSystem.actorOf(Props[SimpleActor], "simpleRemoteActor")
  actor ! "Checking"
}