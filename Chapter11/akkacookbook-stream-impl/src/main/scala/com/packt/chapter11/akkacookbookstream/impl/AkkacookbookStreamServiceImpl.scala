package com.packt.chapter11.akkacookbookstream.impl

import akka.Done
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.packt.chapter11.akkacookbookstream.api.AkkacookbookStreamService
import com.packt.chapter11.akkacookbook.api.AkkacookbookService

import scala.concurrent.Future

/**
  * Implementation of the AkkacookbookStreamService.
  */
class AkkacookbookStreamServiceImpl(akkacookbookService: AkkacookbookService) extends AkkacookbookStreamService {
  def stream = ServiceCall { hellos =>
    ???
    //Future.successful(hellos.mapAsync(8)(akkacookbookService.hello(_).invoke()))
  }
}
