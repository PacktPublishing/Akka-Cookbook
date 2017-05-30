package com.packt.chapter7

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.cluster.Cluster
import akka.cluster.singleton._
import scala.concurrent.duration._

object ClusterSingletonApplication extends App {
  val actorSystem = ActorSystem("ClusterSystem")
  val cluster = Cluster(actorSystem)

  val clusterSingletonSettings = ClusterSingletonManagerSettings(actorSystem)
  val clusterSingletonManager = ClusterSingletonManager.props(Props[ClusterAwareSimpleActor], PoisonPill, clusterSingletonSettings)
  actorSystem.actorOf(clusterSingletonManager, "singletonClusteAwareSimpleActor")

  val singletonSimpleActor = actorSystem.actorOf(ClusterSingletonProxy.props(
    singletonManagerPath = "/user/singletonClusteAwareSimpleActor",
    settings = ClusterSingletonProxySettings(actorSystem)),
    name = "singletonSimpleActorProxy")

  import actorSystem.dispatcher
  actorSystem.scheduler.schedule(10 seconds, 5 seconds, singletonSimpleActor, "TEST")
}