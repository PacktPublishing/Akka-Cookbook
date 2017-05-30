package com.packt.chapter10

import akka.actor.{Actor, ActorLogging, ActorRef, Terminated}

import scala.collection.mutable

object Reaper {
  case class WatchMe(ref: ActorRef)
}

class Reaper extends Actor with ActorLogging {
  import Reaper._

  val watched = mutable.Set.empty[ActorRef]

  def allActorsTerminated() = {
    log.info("All actors terminated. Proceeding to shutdown system.")
    context.system.terminate()
  }

  def receive = {
    case WatchMe(ref) =>
      log.info(s"Registering ${ref.path.name}.")
      context.watch(ref)
      watched += ref
    case Terminated(ref) =>
      log.info(s"Terminated ${ref.path.name}")
      watched -= ref
      if (watched.isEmpty) allActorsTerminated()
  }

  override def preStart() = log.info(s"${self.path.name} is running")
}