package com.packt.chapter11.token.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._
import com.packt.chapter11.token.api.TokenService
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class TokenLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication =
    new TokenApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new TokenApplication(context) with LagomDevModeComponents

  override def describeServices = List(readDescriptor[TokenService])
}

abstract class TokenApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the services that this server provides
  override lazy val lagomServer = LagomServer.forServices(
    bindService[TokenService].to(wire[TokenServiceImpl])
  )
}
