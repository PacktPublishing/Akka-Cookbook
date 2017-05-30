package com.packt.chapter10

import akka.actor.ActorLogging


class ShutdownPatternWorker extends ReaperAwareActor with ActorLogging {
  def receive = { case e : Exception => throw e }

  override def preStartPostRegistration() = log.info(s"${self.path.name} is running")
  override def postStop() = log.info("${self.path.name} has stopped")
}
