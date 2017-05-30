package com.packt.chapter10

import akka.actor.{Actor, ActorLogging, Props}

import scala.concurrent.duration._

class ShutdownPatternMaster extends ReaperAwareActor with ActorLogging {
  import context.dispatcher

  val receive = Actor.emptyBehavior

  override def preStartPostRegistration() = {
    val worker1 = context.actorOf(Props[ShutdownPatternWorker], "worker1")
    context.actorOf(Props[ShutdownPatternWorker], "worker2")
    context.system.scheduler.scheduleOnce(2 second, worker1, new Exception("something went wrong"))
    log.info(s"${self.path.name} is running")
  }
  override def postStop() = log.info(s"${self.path.name} has stopped")
}
