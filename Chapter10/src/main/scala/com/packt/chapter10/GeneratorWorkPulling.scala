package com.packt.chapter10

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.packt.chapter10.WorkerWorkPulling.{RejectWork, Work, WorkDone}

import scala.concurrent.duration._
import scala.util.Random

object GeneratorWorkPulling {
  case object GenerateWork
}

class GeneratorWorkPulling(master: ActorRef) extends Actor with ActorLogging {
  import GeneratorWorkPulling._
  import context._

  override def preStart() = {
    context.system.scheduler.schedule(1 second, 1 seconds, self, GenerateWork)
  }

  def receive = {
    case GenerateWork =>
      master ! Work(Random.nextInt(1000), self)
    case WorkDone(work) =>
      log.info(s"Work done [${work.workId}]")
    case RejectWork(work) =>
      log.info(s"Work rejected [${work.workId}]")
  }

}
