package com.packt.chapter6

import akka.actor.ActorSystem
import akka.persistence.{Recovery, SnapshotSelectionCriteria}

object FriendApp extends App {
  val system = ActorSystem("test")
  val hector = system.actorOf(FriendActor.props("Hector", Recovery()))
  hector ! AddFriend(Friend("Laura"))
  hector ! AddFriend(Friend("Nancy"))
  hector ! AddFriend(Friend("Oliver"))
  hector ! AddFriend(Friend("Steve"))
  hector ! "snap"
  hector ! RemoveFriend(Friend("Oliver"))
  hector ! "print"
  Thread.sleep(2000)
  system.terminate()
}

object FriendRecoveryDefault extends App {
  val system = ActorSystem("test")
  val hector = system.actorOf(FriendActor.props("Hector", Recovery()))
  hector ! "print"
  Thread.sleep(2000)
  system.terminate()
}

object FriendRecoveryOnlyEvents extends App {
  val system = ActorSystem("test")
  val recovery = Recovery(fromSnapshot = SnapshotSelectionCriteria.None)
  val hector = system.actorOf(FriendActor.props("Hector", recovery))
  hector ! "print"
  Thread.sleep(2000)
  system.terminate()
}

object FriendRecoveryEventsSequence extends App {
  val system = ActorSystem("test")
  val recovery = Recovery(fromSnapshot = SnapshotSelectionCriteria.None, toSequenceNr = 2)
  val hector = system.actorOf(FriendActor.props("Hector", recovery))
  Thread.sleep(2000)
  system.terminate()
}

object FriendRecoveryEventsReplay extends App {
  val system = ActorSystem("test")
  val recovery = Recovery(fromSnapshot = SnapshotSelectionCriteria.None, replayMax = 3)
  val hector = system.actorOf(FriendActor.props("Hector", recovery))
  Thread.sleep(2000)
  system.terminate()
}
