package com.packt.chapter10

import akka.actor.{Actor, ActorLogging, ActorRef, Stash}
import com.packt.chapter10.PausableActor.{Ready, Work}

object PausableActor {
  case class Work(id: Int)
  case object Ready
}

class PausableActor(target: ActorRef) extends Actor with ActorLogging with Stash {
  def receive = {
    case work: Work =>
      target ! work
      log.info(s"Received Work [${work.id}]. Sending and pausing.")
      context.become(pausedBehavior, discardOld = false)
  }

  def pausedBehavior : Receive = {
    case work: Work =>
      stash()
    case Ready if sender == target =>
      log.info(s"[${target.path.name}] is ready again.")
      context.unbecome()
      unstashAll()
    case Ready =>
      log.info(s"Discarding [Ready] from other actor different from ${target.path.name}")
  }
}
