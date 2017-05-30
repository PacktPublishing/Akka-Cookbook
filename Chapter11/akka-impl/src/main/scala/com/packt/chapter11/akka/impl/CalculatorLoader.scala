package com.packt.chapter11.akka.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.packt.chapter11.akka.api.CalculatorService
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

class CalculatorLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication =
    new CalculatorApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication = {
    new CalculatorApplication(context) with LagomDevModeComponents
  }

  override def describeServices = List(
    readDescriptor[CalculatorService]
  )
}

abstract class CalculatorApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the services that this server provides
  override lazy val lagomServer = LagomServer.forServices(
    bindService[CalculatorService].to(wire[CalculatorServiceImpl])
  )
}