package com.packt.chapter10

import akka.actor.{Actor, ActorLogging}
import com.packt.chapter10.TrafficLightFSM.ReportChange

class FSMChangeSubscriber extends Actor with ActorLogging {
  def receive = { case ReportChange(s, d) => log.info(s"Change detected to [$s] at [$d]") }
}
