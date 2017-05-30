package com.packt.chapter1

import akka.actor.{PoisonPill, Props, ActorSystem, Actor}

/**
  * Created by user
  */
object ShutdownApp extends App{
  val actorSystem = ActorSystem("HelloAkka")
  val shutdownActor1 = actorSystem.actorOf(Props[ShutdownActor], "shutdownActor1")
  shutdownActor1 ! "hello"
  shutdownActor1 ! PoisonPill
  shutdownActor1 ! "Are you there?"

  val shutdownActor2 = actorSystem.actorOf(Props[ShutdownActor], "shutdownActor2")
  shutdownActor2 ! "hello"
  shutdownActor2 ! Stop
  shutdownActor2 ! "Are you there?"

}

class ShutdownActor extends Actor {
  override def receive: Receive = {
    case msg:String => println(s"$msg")
    case Stop => context.stop(self)
  }
} {

case object Stop

}