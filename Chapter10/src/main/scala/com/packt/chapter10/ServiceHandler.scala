package com.packt.chapter10

import akka.actor.{Actor, ActorLogging}

class ServiceHandler extends Actor with ActorLogging {
  def receive = Actor.ignoringBehavior
  override def preStart() = log.info(s"${self.path.name} is running")
  override def postStop() = log.info(s"${self.path.name} has stopped")
}
