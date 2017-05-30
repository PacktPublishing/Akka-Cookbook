package com.packt.chapter10

import akka.actor.{ActorSystem, Props}
import akka.routing.{BalancingPool, SmallestMailboxPool}
import com.packt.chapter10.BalancedWorker.WorkTask

import scala.concurrent.duration._
import scala.util.Random

object BalancingDispatcherApp extends App {
  val actorSystem = ActorSystem()
  val workerPool = actorSystem.actorOf(Props[BalancedWorker].withRouter(BalancingPool(4)),"workers")

  import actorSystem.dispatcher
  actorSystem.scheduler.schedule(1 second, 200 millis)(sendTask)

  def sendTask : Unit = workerPool ! WorkTask(Random.nextInt(10000))
}

object SmallestMailboxRouterApp extends App {
  val actorSystem = ActorSystem()
  val workerPool = actorSystem.actorOf(Props[BalancedWorker].withRouter(SmallestMailboxPool(4)),"workers")

  import actorSystem.dispatcher
  actorSystem.scheduler.schedule(1 second, 200 millis)(sendTask)

  def sendTask() : Unit = workerPool ! WorkTask(Random.nextInt(10000))
}