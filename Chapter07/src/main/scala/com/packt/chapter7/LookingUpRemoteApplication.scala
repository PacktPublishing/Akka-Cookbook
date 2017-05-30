package com.packt.chapter7

import akka.actor.{ActorRef, ActorSystem, Props}

import scala.concurrent.duration._

object LookingUpActorSelection extends App {
  val actorSystem = ActorSystem("LookingUpActors")
  implicit val dispatcher = actorSystem.dispatcher

  val selection = actorSystem.actorSelection("akka.tcp://LookingUpRemoteActors@127.0.0.1:2553/user/remoteActor")
  selection ! "test"

  selection.resolveOne(3 seconds).onSuccess {
    case actorRef : ActorRef =>
      println("We got an ActorRef")
      actorRef ! "test"
  }
}


object LookingUpRemoteActors extends App {
  val actorSystem = ActorSystem("LookingUpRemoteActors")
  actorSystem.actorOf(Props[SimpleActor], "remoteActor")
}
