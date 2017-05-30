package com.packt.chapter7

import akka.actor.{Actor, ActorRef, Terminated}
import scala.util.Random

case class RegisterWorker(workerActor: ActorRef)

class MasterActor extends Actor {
  var workers = List.empty[ActorRef]

  def receive = {
    case RegisterWorker(workerActor) =>
      context.watch(workerActor)
      workers =  workerActor :: workers
    case Terminated(actorRef) =>
      println(s"Actor ${actorRef.path.address} has been terminated. Removing from available workers.")
      workers = workers.filterNot(_ == actorRef)
    case work: Work if workers.isEmpty =>
      println("We cannot process your work since there is no workers.")
    case work: Work =>
      workers(Random.nextInt(workers.size)) ! work
    case WorkDone(workId) =>
      println(s"Work with id $workId is done.")
  }
}
