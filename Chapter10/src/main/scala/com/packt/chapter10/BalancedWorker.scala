package com.packt.chapter10

import akka.actor.{Actor, ActorLogging}

object BalancedWorker {
  case class WorkTask(id: Int)
}

class BalancedWorker extends Actor with ActorLogging {
  import BalancedWorker._

  def receive = {
    case WorkTask(id) =>
      log.info(s"[$id] Sleeping for 1000 milliseconds.")
      Thread.sleep(1000)
  }
}
