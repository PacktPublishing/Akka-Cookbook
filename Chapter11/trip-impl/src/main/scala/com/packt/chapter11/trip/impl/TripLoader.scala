package com.packt.chapter11.trip.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.packt.chapter11.trip.api.TripService
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

class TripLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new TripApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication = {
    new TripApplication(context) with LagomDevModeComponents
  }

  override def describeServices = List(
    readDescriptor[TripService]
  )
}

abstract class TripApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with AhcWSComponents {

  // Bind the services that this server provides
  override lazy val lagomServer = LagomServer.forServices(
    bindService[TripService].to(wire[TripServiceImpl])
  )

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = ClientSerializerRegistry

  // Register the AkkaCookbook persistent entity
  persistentEntityRegistry.register(wire[ClientEntity])
}