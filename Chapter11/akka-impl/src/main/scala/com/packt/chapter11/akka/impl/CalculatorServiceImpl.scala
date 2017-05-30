package com.packt.chapter11.akka.impl

import akka.actor.{ActorSystem, Props}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.packt.chapter11.akka.api.CalculatorService
import akka.pattern.ask
import akka.util.Timeout
import com.packt.chapter11.akka.impl.CalculatorActor.{Multiply, Sum}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

class CalculatorServiceImpl(system: ActorSystem)
                           (implicit val ec: ExecutionContext) extends CalculatorService {
  implicit val timeout = Timeout(2 seconds)

  override def add(one: Int, other: Int) = ServiceCall { _ =>
    val calculatorActor = system.actorOf(Props[CalculatorActor])
    (calculatorActor ? Sum(one, other)).mapTo[Int]
  }

  override def multiply(one: Int, other: Int) = ServiceCall { _ =>
    val calculatorActor = system.actorOf(Props[CalculatorActor])
    (calculatorActor ? Multiply(one, other)).mapTo[Int]
  }
}
