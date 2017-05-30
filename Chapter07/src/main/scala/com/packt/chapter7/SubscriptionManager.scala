package com.packt.chapter7

import akka.actor.{Actor, ActorRef, Props}
import akka.cluster.Cluster
import akka.cluster.ddata._
import akka.cluster.ddata.Replicator._

object SubscriptionManager {
  case class Subscription(id: Int, origin: String, creationTimestamp: Long)

  case class AddSubscription(subscription: Subscription)
  case class RemoveSubscription(subscription: Subscription)
  case class GetSubscriptions(consistency: ReadConsistency)

  trait GetSubscriptionsResult
  case class GetSubscriptionsSuccess(subscriptions: Set[Subscription]) extends GetSubscriptionsResult
  case object GetSubscriptionsFailure extends GetSubscriptionsResult

  def props = Props(new SubscriptionManager())

  val subscriptionKey = "subscription_key"
}

class SubscriptionManager extends Actor {
  import SubscriptionManager._

  val replicator = DistributedData(context.system).replicator
  implicit val node = Cluster(context.system)

  private val DataKey = ORSetKey[Subscription](subscriptionKey)
  replicator ! Subscribe(DataKey, self)

  def receive = {
    case AddSubscription(subscription) =>
      println(s"Adding: $subscription")
      replicator ! Update(DataKey, ORSet.empty[Subscription], WriteLocal)(_ + subscription)

    case RemoveSubscription(subscription) =>
      println(s"Removing $subscription")
      replicator ! Update(DataKey, ORSet.empty[Subscription], WriteLocal)(_ - subscription)

    case GetSubscriptions(consistency) =>
      replicator ! Get(DataKey, consistency, request = Some(sender()))
    case g @ GetSuccess(DataKey, Some(replyTo: ActorRef)) =>
      val value = g.get(DataKey).elements
      replyTo ! GetSubscriptionsSuccess(value)
    case GetFailure(DataKey, Some(replyTo: ActorRef)) =>
      replyTo ! GetSubscriptionsFailure

    case _: UpdateResponse[_] => // ignore

    case c @ Changed(DataKey) =>
      val data = c.get(DataKey)
      println(s"Current elements: ${data.elements}")
  }
}
