package com.packt.chapter7

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster

import scala.concurrent.duration._
import scala.util.Random

object DistributedPubSubApplication extends App {
  val actorSystem = ActorSystem("ClusterSystem")
  val cluster = Cluster(actorSystem)

  val notificationSubscriber = actorSystem.actorOf(Props[NotificationSubscriber])
  val notificationPublisher = actorSystem.actorOf(Props[NotificationPublisher])

  val clusterAddress = cluster.selfUniqueAddress
  val notification = Notification(s"Sent from $clusterAddress", "Test!")

  import actorSystem.dispatcher
  actorSystem.scheduler.schedule(Random.nextInt(5) seconds, 5 seconds, notificationPublisher, notification)
}
