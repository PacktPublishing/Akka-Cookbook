package com.packt.chapter6

import akka.actor.{ActorLogging, Props}
import akka.persistence.fsm.PersistentFSM
import scala.reflect.ClassTag

object PersistentFSMActor {
  def props(persistenceId: String) = Props(new PersistentFSMActor(persistenceId))
}

class PersistentFSMActor(_persistenceId: String)(implicit val domainEventClassTag: ClassTag[DomainEvent])
  extends PersistentFSM[CountDownLatchState,Count,DomainEvent] with ActorLogging {

  startWith(Closed, Count())
  when(Closed) {
    case Event(Initialize(count), _) =>
      log.info(s"Initializing countdown latch with count $count")
      stay applying LatchDownClosed(count)
    case Event(Mark, Count(n)) if n != 0 =>
      log.info(s"Still $n to open gate.")
      stay applying LatchDownClosed(n)
    case Event(Mark, _) =>
      log.info(s"Gate open.")
      goto(Open) applying LatchDownOpen
  }
  when(Open) {
    case Event(Initialize(count), _) => goto(Closed) applying LatchDownClosed(count)
  }

  override def preStart() = log.info("Starting.")
  override def postStop() = log.info("Stopping.")
  override val persistenceId = _persistenceId
  override def applyEvent(event: DomainEvent, countdown: Count) = event match {
    case LatchDownClosed(i) => Count(i-1)
    case LatchDownOpen => Count()
  }
}
