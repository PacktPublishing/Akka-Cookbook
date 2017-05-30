package com.packt.chapter10

import akka.actor.{ActorSystem, Props}

object AggregatorPatternApp extends App {

  val actorSystem = ActorSystem()
  val tHandler = actorSystem.actorOf(Props(classOf[SocialMediaHandler], "Twitter"), "twitterHandler")
  val fHandler = actorSystem.actorOf(Props(classOf[SocialMediaHandler], "Facebook"), "facebookHandler")
  val lHandler = actorSystem.actorOf(Props(classOf[SocialMediaHandler], "LinkedIn"), "linkedInHandler")
  val handlers = List(tHandler, fHandler, lHandler)
  val aggregator = actorSystem.actorOf(Props(classOf[SocialMediaAggregator], handlers), "aggregator")

  actorSystem.actorOf(Props(classOf[SocialMediaStalker], aggregator, "stalker"), "stalker")
}
