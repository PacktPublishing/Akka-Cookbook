package com.packt.chapter10

import akka.actor.{Actor, ActorLogging, ActorRef}
import scala.concurrent.duration._

class ServicesManager(childrenCreator: ActorRef) extends Actor with ActorLogging {
  import OrderedKiller._
  import context._

  override def preStart() {
    log.info("Asking for my children")
    childrenCreator ! GetChildren(self)
  }

  def waiting: Receive = {
    case Children(kids) =>
      log.info("Children received")
      context.become(initialized(kids))
      context.system.scheduler.scheduleOnce(1 second, self, "something")
  }

  def initialized(kids: Iterable[ActorRef]) : Receive = {
    case _ =>
      log.info(s"I have been happily initialized with my kids: ${kids.map(_.path.name)}")
  }

  def receive = waiting
  override def postStop() = log.info(s"${self.path.name} has stopped")
}
