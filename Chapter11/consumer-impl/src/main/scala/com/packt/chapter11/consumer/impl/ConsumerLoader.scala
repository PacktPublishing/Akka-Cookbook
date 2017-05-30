package com.packt.chapter11.consumer.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.packt.chapter11.consumer.api.ConsumerService
import com.packt.chapter11.token.api.TokenService
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

class ConsumerLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication =
    new ConsumerApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ConsumerApplication(context) with LagomDevModeComponents

  override def describeServices = List(readDescriptor[ConsumerService])
}

abstract class ConsumerApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the services that this server provides
  override lazy val lagomServer = LagomServer.forServices(
    bindService[ConsumerService].to(wire[ConsumerServiceImpl])
  )

  lazy val tokenService = serviceClient.implement[TokenService]
}
