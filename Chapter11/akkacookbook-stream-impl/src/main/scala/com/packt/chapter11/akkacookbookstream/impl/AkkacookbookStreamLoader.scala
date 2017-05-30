package com.packt.chapter11.akkacookbookstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.packt.chapter11.akkacookbookstream.api.AkkacookbookStreamService
import com.packt.chapter11.akkacookbook.api.AkkacookbookService
import com.softwaremill.macwire._

class AkkacookbookStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new AkkacookbookStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new AkkacookbookStreamApplication(context) with LagomDevModeComponents

  override def describeServices = List(
    readDescriptor[AkkacookbookStreamService]
  )
}

abstract class AkkacookbookStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the services that this server provides
  override lazy val lagomServer = LagomServer.forServices(
    bindService[AkkacookbookStreamService].to(wire[AkkacookbookStreamServiceImpl])
  )

  // Bind the AkkacookbookService client
  lazy val akkacookbookService = serviceClient.implement[AkkacookbookService]
}
