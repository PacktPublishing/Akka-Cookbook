package com.packt.chapter10

import akka.actor.{ActorSystem, Props}
import akka.contrib.throttle.Throttler._
import akka.contrib.throttle.TimerBasedThrottler

import scala.concurrent.duration._

object ThrottlingApp extends App {
  val actorSystem = ActorSystem()
  val throttler = actorSystem.actorOf(Props(classOf[TimerBasedThrottler], 2 msgsPer 1.second))
  val easyToOverwhelmReceiver = actorSystem.actorOf(Props[EasyToOverwhelmReceiver], "receiver")
  throttler ! SetTarget(Some(easyToOverwhelmReceiver))
  actorSystem.actorOf(Props(classOf[ReallyFastSender], throttler), "sender")
}
