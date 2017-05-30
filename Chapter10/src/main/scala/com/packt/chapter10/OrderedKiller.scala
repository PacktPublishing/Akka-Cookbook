package com.packt.chapter10

import akka.actor.{Actor, ActorLogging, ActorRef, Terminated}
import akka.pattern._

import scala.concurrent.duration._
import scala.concurrent.Future

object OrderedKiller {
  case object AllChildrenStopped
  case class GetChildren(parentActor: ActorRef)
  case class Children(children: Iterable[ActorRef])
}

abstract class OrderedKiller extends Actor with ActorLogging {
  import OrderedKiller._
  import context._

  def killChildrenOrderly(orderedChildren: List[ActorRef]): Future[Any] = {
    orderedChildren.foldLeft(Future(AllChildrenStopped))(
      (p, child) => p.flatMap(_ => gracefulStop(child, 2 seconds).map(_ => AllChildrenStopped))
    )
  }

  def noChildrenRegistered: Receive = {
    case GetChildren(parentActor) =>
      watch(parentActor)
      parentActor ! Children(children)
      become(childrenRegistered(parentActor))
  }

  def childrenRegistered(to: ActorRef): Receive = {
    case GetChildren(parentActor) if sender == to =>
      parentActor ! Children(children)
    case Terminated(`to`) =>
      killChildrenOrderly(orderChildren(children)) pipeTo self
    case AllChildrenStopped =>
      stop(self)
  }

  def orderChildren(unorderedChildren: Iterable[ActorRef]) : List[ActorRef]

  def receive = noChildrenRegistered
}
