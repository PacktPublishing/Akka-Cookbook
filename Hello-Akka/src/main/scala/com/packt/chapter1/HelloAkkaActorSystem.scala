package com.packt.chapter1
import akka.actor.ActorSystem
/**
  * Created by user on 13/5/16.
  */
object HelloAkkaActorSystem extends App {

  val actorSystem =  ActorSystem("HelloAkka")

  println(actorSystem)
}
