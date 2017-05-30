package com.packt.chapter9

import akka.actor.Actor

class UnresponsiveActor extends Actor {
  def receive = Actor.ignoringBehavior
}