package com.packt.chapter10

import akka.actor.{Actor, ActorLogging, ActorRef, ReceiveTimeout}
import com.packt.chapter10.SocialMediaAggregator.{Report, StartFetching, StopFetching}

import scala.collection.mutable
import scala.concurrent.duration._

class SocialMediaStalker(aggregator: ActorRef, userId: String) extends Actor with ActorLogging {
  import context.dispatcher

  context.setReceiveTimeout(10 seconds)

  val counts = mutable.Map.empty[String, Int].withDefaultValue(0)

  override def preStart() = {
    log.info("Politely asking to aggregate")
    aggregator ! StartFetching(userId, 1 second)
    context.system.scheduler.scheduleOnce(5 second, aggregator, StopFetching(userId))
  }

  override def postStop() = {
    log.info(s"Stopping. Overall counts for $userId: $counts")
  }

  def receive = {
    case Report(list) =>
      val stats = list.groupBy(_.socialNetwork).mapValues(_.map(_.posts.size).sum)
      log.info(s"New report: $stats")
      stats.foreach(kv => counts += kv._1 -> (counts(kv._1) + kv._2))
    case ReceiveTimeout =>
      context.stop(self)
  }
}
