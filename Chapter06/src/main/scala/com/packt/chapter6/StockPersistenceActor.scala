package com.packt.chapter6

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}

object StockPersistenceActor {
  def props(stockId: String) = Props(new StockPersistenceActor(stockId))
}

class StockPersistenceActor(stockId: String) extends PersistentActor with ActorLogging {
  override val persistenceId = stockId

  var state = StockHistory()
  def updateState(event: ValueAppended) = state = state.update(event)

  val receiveRecover: Receive = {
    case evt: ValueAppended => updateState(evt)
    case RecoveryCompleted => log.info(s"Recovery completed. Current state: $state")
  }

  val receiveCommand: Receive = {
    case ValueUpdate(value) => persist(ValueAppended(StockValue(value)))(updateState)
    case "print" => log.info(s"Current state: $state")
  }

  override def postStop() = log.info(s"Stopping [${self.path}]")
}