package com.packt.chapter10

import akka.actor.{Actor, ActorLogging}

import scala.util.Random

class CountDownLatchWorker(countDownLatch: CountDownLatch) extends Actor with ActorLogging {

  override def preStart() = {
    //do something
    val millis = Random.nextInt(5) * 100
    log.info(s"Sleeping $millis millis")
    Thread.sleep(millis)
    countDownLatch.countDown()
  }

  def receive = Actor.ignoringBehavior
}
