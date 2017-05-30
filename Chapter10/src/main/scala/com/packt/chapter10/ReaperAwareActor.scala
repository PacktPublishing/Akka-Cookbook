package com.packt.chapter10

import akka.actor.Actor
import com.packt.chapter10.Reaper.WatchMe

trait ReaperAwareActor extends Actor {
  override final def preStart() = {
    registerReaper()
    preStartPostRegistration()
  }

  private def registerReaper() = {
    context.actorSelection("/user/Reaper") ! WatchMe(self)
  }

  def preStartPostRegistration() : Unit = ()
}
