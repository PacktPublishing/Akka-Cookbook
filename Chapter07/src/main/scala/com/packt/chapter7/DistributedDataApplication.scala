package com.packt.chapter7

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.cluster.ddata.Replicator.{ReadFrom, ReadMajority}
import akka.pattern.ask
import akka.util.Timeout
import com.packt.chapter7.SubscriptionManager._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random

object DistributedDataApplication extends App {
  val actorSystem = ActorSystem("ClusterSystem")

  Cluster(actorSystem).registerOnMemberUp {
    val subscriptionManager = actorSystem.actorOf(SubscriptionManager.props)

    val subscription = Subscription(Random.nextInt(3), Cluster(actorSystem).selfUniqueAddress.toString, System.currentTimeMillis())
    subscriptionManager ! AddSubscription(subscription)

    //Let's simulate some time has passed. Never use Thread.sleep in production!
    Thread.sleep(10000)

    implicit val timeout = Timeout(5 seconds)

    val readMajority = ReadMajority(timeout = 5.seconds)
    val readFrom = ReadFrom(n = 2, timeout = 5.second)

    Await.result(subscriptionManager ? GetSubscriptions(readMajority), 5 seconds) match {
      case GetSubscriptionsSuccess(subscriptions) =>
        println(s"The current set of subscriptions is $subscriptions")
      case GetSubscriptionsFailure =>
        println(s"Subscription manager was not able to get subscriptions successfully.")
    }

    subscriptionManager ! RemoveSubscription(subscription)

    Await.result(subscriptionManager ? GetSubscriptions(readFrom), 5 seconds) match {
      case GetSubscriptionsSuccess(subscriptions) =>
        println(s"The current set of subscriptions is $subscriptions")
      case GetSubscriptionsFailure =>
        println(s"Subscription manager was not able to get subscriptions successfully.")
    }
  }
}
