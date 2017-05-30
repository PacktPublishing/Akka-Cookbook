package com.packt.chapter6

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, Recovery, RecoveryCompleted, SnapshotOffer}

object FriendActor {
  def props(friendId: String, recoveryStrategy: Recovery) = Props(new FriendActor(friendId, recoveryStrategy))
}

class FriendActor(friendId: String, r: Recovery) extends PersistentActor with ActorLogging {
  override val persistenceId = friendId
  override val recovery = r

  var state = FriendState()
  def updateState(event: FriendEvent) = state = state.update(event)

  val receiveRecover: Receive = {
    case evt: FriendEvent =>
      log.info(s"Replaying event: $evt")
      updateState(evt)
    case SnapshotOffer(_, recoveredState : FriendState) =>
      log.info(s"Snapshot offered: $recoveredState")
      state = recoveredState
    case RecoveryCompleted => log.info(s"Recovery completed. Current state: $state")
  }

  val receiveCommand: Receive = {
    case AddFriend(friend) => persist(FriendAdded(friend))(updateState)
    case RemoveFriend(friend) => persist(FriendRemoved(friend))(updateState)
    case "snap" => saveSnapshot(state)
    case "print" => log.info(s"Current state: $state")
  }
}