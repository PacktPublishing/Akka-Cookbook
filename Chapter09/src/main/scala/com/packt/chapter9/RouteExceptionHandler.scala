package com.packt.chapter9

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.pattern.AskTimeoutException

trait RouteExceptionHandler {

  val routeExceptionHandler = ExceptionHandler {
    case _: ArithmeticException =>
      complete {
        StatusCodes.BadRequest -> "You values are incorrect. Probably b needs to be different from 0"
      }
    case _: AskTimeoutException =>
      complete {
        StatusCodes.ServiceUnavailable -> "Internal actor is not responding within 500 millis"
      }
  }
}
