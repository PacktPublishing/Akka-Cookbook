package com.packt.chapter10

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill}
import akka.contrib.pattern.Aggregator

import scala.collection.mutable
import scala.concurrent.duration._

object SocialMediaAggregator {
  case object GetPosts
  case object ReturnPosts
  case class StartFetching(userId: String, period: FiniteDuration)
  case class StopFetching(userId: String)
  case class GetLatestPosts(userId: String)

  case class Post(title: String, content: String)
  case class LatestPostResult(socialNetwork: String, posts: Seq[Post])
  case class Report(list: List[LatestPostResult])
}

class SocialMediaAggregator(handlers: List[ActorRef]) extends Actor with Aggregator with ActorLogging {
  import SocialMediaAggregator._
  import context._

  val initBehavior : Receive = {
    case StartFetching(id, period) =>
      log.info(s"Fetching latest posts for $id")
      new LatestPostsAggregator(sender, id, period)
  }

  expectOnce(initBehavior)

  class LatestPostsAggregator(originalSender: ActorRef, userId: String, period: FiniteDuration) {
    val latestPosts = mutable.ArrayBuffer.empty[LatestPostResult]

    val returnPostCancel = context.system.scheduler.schedule(1.second, period, self, ReturnPosts)
    val getPostsCancel = context.system.scheduler.schedule(0.seconds, 400 millis, self, GetPosts)

    val behavior : Receive = {
      case GetPosts => handlers.foreach(_ ! GetLatestPosts(userId))
      case lpr : LatestPostResult => latestPosts += lpr
      case ReturnPosts =>
        originalSender ! Report(latestPosts.toList)
        latestPosts.clear()
      case StopFetching(id) =>
        log.info(s"Stopping latest posts fetching for $id")
        returnPostCancel.cancel()
        getPostsCancel.cancel()
        context.system.scheduler.scheduleOnce(5 seconds, self, PoisonPill)
    }

    expect(behavior)
  }

  override def postStop() = log.info(s"Stopped.")
}
