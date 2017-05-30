package com.packt.chapter10

import akka.actor.{Actor, ActorLogging}
import com.packt.chapter10.SocialMediaAggregator.{GetLatestPosts, LatestPostResult, Post}

import scala.util.Random

class SocialMediaHandler(socialMedia: String) extends Actor with ActorLogging {

  def receive = {
    case GetLatestPosts(id) =>
      Thread.sleep(Random.nextInt(4) * 100)
      val posts = (0 to Random.nextInt(2)).map(_ => Post("some title", "some content"))
      sender ! LatestPostResult(socialMedia, posts)
  }
}
