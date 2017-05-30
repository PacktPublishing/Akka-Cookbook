package com.packt.chapter11.akkacookbook.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.packt.chapter11.akkacookbook.api.AkkacookbookService
import com.softwaremill.macwire._

class AkkacookbookLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new AkkacookbookApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new AkkacookbookApplication(context) with LagomDevModeComponents

  override def describeServices = List(
    readDescriptor[AkkacookbookService]
  )
}

abstract class AkkacookbookApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with AhcWSComponents {

  // Bind the services that this server provides
  override lazy val lagomServer = LagomServer.forServices(
    bindService[AkkacookbookService].to(wire[AkkacookbookServiceImpl])
  )

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = AkkacookbookSerializerRegistry

  // Register the AkkaCookbook persistent entity
  persistentEntityRegistry.register(wire[AkkacookbookEntity])
}
