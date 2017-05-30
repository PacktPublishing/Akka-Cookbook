package com.packt.chapter7

import akka.actor.{Actor, PoisonPill, ReceiveTimeout}
import akka.cluster.Cluster
import akka.cluster.sharding.ShardRegion
import akka.cluster.sharding.ShardRegion.Passivate

import scala.concurrent.duration._

object TemperatureActor {

  case class Location(country: String, city: String) {
    override def toString = s"$country-$city"
  }
  case class UpdateTemperature(location: Location, currentTemp: Double)
  case class GetCurrentTemperature(location: Location)

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case msg@UpdateTemperature(location, _) ⇒ (s"$location", msg)
    case msg@GetCurrentTemperature(location) ⇒ (s"$location", msg)
  }

  val numberOfShards = 100

  val extractShardId: ShardRegion.ExtractShardId = {
    case UpdateTemperature(location, _) ⇒ (s"$location".hashCode % numberOfShards).toString
    case GetCurrentTemperature(location) ⇒ (s"$location".hashCode % numberOfShards).toString
  }

  val shardName = "Temperature"
}


class TemperatureActor extends Actor {

  import TemperatureActor._

  // passivate the entity when no activity
  context.setReceiveTimeout(2 minutes)

  var temperatureMap = Map.empty[Location, Double]

  override def preStart() = {
    println(s"I have been created at ${Cluster(context.system).selfUniqueAddress}")
  }

  def receive = {
    case update @ UpdateTemperature(location, temp) =>
      temperatureMap += (location -> temp)
      println(s"Temp updated: $location")

    case GetCurrentTemperature(location) =>
      sender ! temperatureMap(location)

    case ReceiveTimeout => context.parent ! Passivate(stopMessage = PoisonPill)
  }
}
