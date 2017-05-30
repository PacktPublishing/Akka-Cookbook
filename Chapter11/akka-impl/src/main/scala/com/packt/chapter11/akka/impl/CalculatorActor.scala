package com.packt.chapter11.akka.impl

import akka.actor.Actor

object CalculatorActor {
  case class Sum(one: Int, another: Int)
  case class Multiply(one: Int, another: Int)
}

class CalculatorActor extends Actor {
  import CalculatorActor._

  def receive = {
    case Sum(one, another) => sender ! one + another
    case Multiply(one, another) => sender ! one * another
  }
}
