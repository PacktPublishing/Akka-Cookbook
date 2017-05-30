package com.packt.chapter2

import akka.actor.{Props, ActorRef, Actor, OneForOneStrategy}
import akka.actor.SupervisorStrategy._
import scala.concurrent.duration._

/**
  * Created by user.
  */
class ActorLifeCycle {

}


class Supervisor extends Actor {

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: ArithmeticException => Resume
      case t =>
        super.supervisorStrategy.decider.applyOrElse(t, (_: Any) => Escalate)
    }

  def receive = {
    case (props: Props, name: String) => context.actorOf(props, name)

  }
}

class LifeCycleActor extends Actor {

  var sum = 1

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    sum = sum * 2
  }

  override def preStart(): Unit = println(s"sum is $sum")

  def receive = {
    case x: Int => sum = sum + x
    case "getState" => println(s"sum is $sum")
    case "error" => throw new ArithmeticException()
    case _ => println("default msg")
  }

  override def postStop(): Unit = {
    println(s"sum is $sum*3")
  }

  @throws[Exception](classOf[Exception])
  override def postRestart(reason: Throwable): Unit = super.postRestart(reason)
}

