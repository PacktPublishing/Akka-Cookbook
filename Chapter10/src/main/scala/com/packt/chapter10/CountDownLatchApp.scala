package com.packt.chapter10

import akka.actor.{ActorSystem, Props}
import akka.routing.RoundRobinPool

object CountDownLatchApp extends App {
  implicit val actorSystem = ActorSystem()
  import actorSystem._
  val routeesToSetUp = 2
  val countDownLatch = CountDownLatch(routeesToSetUp)

  actorSystem.actorOf(Props(classOf[CountDownLatchWorker], countDownLatch)
    .withRouter(RoundRobinPool(routeesToSetUp)), "workers")

  //Future based solution
  countDownLatch.result.onSuccess { case _ => log.info("Future completed successfully") }

  //Await based solution
  countDownLatch.await()
  actorSystem.terminate()
}
