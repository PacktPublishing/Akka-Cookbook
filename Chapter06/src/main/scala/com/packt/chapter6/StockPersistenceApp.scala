package com.packt.chapter6

trait AkkaHelper {
  lazy val system = akka.actor.ActorSystem("example")
  lazy val teslaStockActor = system.actorOf(StockPersistenceActor.props("TLSA"))
}

object StockApp extends App with AkkaHelper {
  teslaStockActor ! ValueUpdate(305.12)
  teslaStockActor ! ValueUpdate(305.15)
  teslaStockActor ! "print"
  Thread.sleep(5000)
  system.terminate()
}

object StockRecoveryApp extends App with AkkaHelper {
  teslaStockActor ! ValueUpdate(305.20)
  teslaStockActor ! "print"
  Thread.sleep(2000)
  system.terminate()
}