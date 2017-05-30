package com.packt.chapter6

import akka.actor.ActorLogging
import akka.persistence._

class SnapshotActor extends PersistentActor with ActorLogging {
  override val persistenceId = "ss-id-1"

  var state = ActiveUsers()
  def updateState(event: Event) = state = state.update(event)

  val receiveRecover: Receive = {
    case evt: Event => updateState(evt)
    case SnapshotOffer(metadata, snapshot: ActiveUsers) => state = snapshot
    case RecoveryCompleted => log.info(s"Recovery completed. Current state: [$state]")
  }

  val receiveCommand: Receive = {
    case UserUpdate(userId, Add) => persist(AddUserEvent(userId))(updateState)
    case UserUpdate(userId, Remove) => persist(RemoveUserEvent(userId))(updateState)
    case "snap"  => saveSnapshot(state)
    case SaveSnapshotSuccess(metadata)         => log.info(s"Snapshot success [$metadata]")
    case SaveSnapshotFailure(metadata, reason) => log.warning(s"Snapshot failure [$metadata] Reason: [$reason]")
  }

  override def postStop() = log.info("Stopping")
  override def recovery = Recovery(SnapshotSelectionCriteria.Latest)
}