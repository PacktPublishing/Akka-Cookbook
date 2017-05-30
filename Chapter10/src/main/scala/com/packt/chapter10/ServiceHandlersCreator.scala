package com.packt.chapter10

import akka.actor.{ActorRef, Props}

class ServiceHandlersCreator extends OrderedKiller {
  override def preStart() = {
    context.actorOf(Props[ServiceHandler], "DatabaseHandler1")
    context.actorOf(Props[ServiceHandler], "DatabaseHandler2")
    context.actorOf(Props[ServiceHandler], "ExternalSOAPHandler")
    context.actorOf(Props[ServiceHandler], "ExternalRESTHandler")
  }

  def orderChildren(unorderedChildren: Iterable[ActorRef]) = {
    val result = unorderedChildren.toList.sortBy(_.path.name)
    log.info(s"Killing order is ${result.map(_.path.name)}")
    result
  }
}
