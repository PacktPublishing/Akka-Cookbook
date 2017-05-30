package com.packt.chapter10

import akka.actor.{Actor, ActorLogging, ActorRef, Terminated}
import com.packt.chapter10.WorkerWorkPulling._

import scala.collection.mutable

object MasterWorkPulling {
  case object JoinWorker
  case object DeregisterWorker
}

class MasterWorkPulling(maxQueueSize: Int) extends Actor with ActorLogging {
  import MasterWorkPulling._

  val workers = mutable.Map.empty[ActorRef, WorkerState]
  val pendingWork = mutable.Queue.empty[Work]

  def receive = {
    case JoinWorker =>
      workers += sender -> Idle
      context.watch(sender)
      log.info(s"New worker registered [$sender]")
    case Terminated(actorRef) =>
      workers -= actorRef
      log.info(s"Worker terminated [$actorRef]")
    case DeregisterWorker =>
      workers -= sender
      log.info(s"Worker deregistered [$sender]")
    case PullWork if pendingWork.nonEmpty =>
      log.info(s"Idle worker asking for work. Setting [$sender] to [Working] state")
      sender ! pendingWork.dequeue
      workers += sender -> Working
    case PullWork =>
      log.info(s"Idle worker asking for work but no work available. Setting [$sender] to [Idle] state")
      workers += sender -> Idle
    case work : Work if pendingWork.size > maxQueueSize =>
      log.info(s"Work received but max pending work tasks reached. Rejecting [$work]")
      sender ! RejectWork(work)
    case work : Work =>
      pendingWork.enqueue(work)
      workers.find(_._2 == Idle) match {
        case Some((worker, _)) =>
          val nextWork = pendingWork.dequeue
          worker ! nextWork
          workers += worker -> Working
          log.info(s"Work received and found idle worker. Submitting [$nextWork] to [$worker]")
        case None =>
          log.info(s"Work received and no idle worker found. Adding to pending work tasks queue.")
      }
  }
}
