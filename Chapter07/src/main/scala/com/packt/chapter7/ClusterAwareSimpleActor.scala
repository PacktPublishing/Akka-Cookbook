package com.packt.chapter7

import akka.actor.Actor
import akka.cluster.Cluster

class ClusterAwareSimpleActor extends Actor {
  val cluster = Cluster(context.system)

  def receive = {
    case _ =>
      println(s"I have been created at ${cluster.selfUniqueAddress}")
  }
}
