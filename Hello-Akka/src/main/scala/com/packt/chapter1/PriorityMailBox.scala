package com.packt.chapter1

import akka.actor.{Props, ActorSystem, Actor}
import akka.dispatch.{PriorityGenerator, UnboundedPriorityMailbox}
import com.typesafe.config.Config

/**
  * Created by user
  */
object PriorityMailBoxApp extends App {

  val actorSystem = ActorSystem("HelloAkka")
  val myPriorityActor = actorSystem.actorOf(Props[MyPriorityActor].withDispatcher("prio-dispatcher"))

  myPriorityActor ! 6.0
  myPriorityActor ! 1
  myPriorityActor ! 5.0
  myPriorityActor ! 3
  myPriorityActor ! "Hello"
  myPriorityActor ! 5
  myPriorityActor ! "I am priority actor"
  myPriorityActor ! "I process string messages first,then integer, long and others"

}


class MyPriorityActor extends Actor {

  def receive: PartialFunction[Any, Unit] = {
    // Int Messages
    case x: Int => println(x)
    // String Messages
    case x: String => println(x)
    // Long messages
    case x: Long => println(x)
    // other messages
    case x => println(x)
  }
}

class MyPriorityActorMailbox(settings: ActorSystem.Settings, config: Config) extends UnboundedPriorityMailbox(
  // Create a new PriorityGenerator, lower prio means more important
  PriorityGenerator {

    // Int Messages
    case x: Int => 1
    // String Messages
    case x: String => 0
    // Long messages
    case x: Long => 2
    // other messages
    case _ => 3
  })

