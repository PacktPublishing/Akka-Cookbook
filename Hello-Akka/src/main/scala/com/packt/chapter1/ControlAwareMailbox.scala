package com.packt.chapter1

import akka.dispatch.ControlMessage
import akka.actor.{Props, Actor, ActorSystem}

/**
  * Created by user
  */
object ControlAwareMailbox extends App {

  val actorSystem = ActorSystem("HelloAkka")
  val  actor = actorSystem.actorOf(Props[Logger].withDispatcher(
    "control-aware-dispatcher"))

  actor ! "hello"

  actor ! "how are"

  actor ! "you?"

  actor ! MyControlMessage
}


case object MyControlMessage extends ControlMessage

class Logger extends Actor {

  def receive = {
    case MyControlMessage => println("Oh, I have to process Control message first")
    case x => println(x.toString)

  }
}
