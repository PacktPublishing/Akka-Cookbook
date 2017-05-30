package com.packt.chapter7

import akka.actor.{ActorSystem, Address, Deploy, Props}
import akka.remote.RemoteScope

object RemoteActorsProgrammatically1 extends App {
  val actorSystem = ActorSystem("RemoteActorsProgramatically1")
}

object RemoteActorsProgrammatically2 extends App {
  val actorSystem = ActorSystem("RemoteActorsProgramatically2")
  println("Creating actor from RemoteActorsProgramatically2")
  val address = Address("akka.tcp", "RemoteActorsProgramatically1", "127.0.0.1", 2552) // this gives the same
  val actor = actorSystem.actorOf(Props[SimpleActor].withDeploy(Deploy(scope = RemoteScope(address))), "remoteActor")
  actor ! "Checking"
}