package com.packt.chapter8

import akka.stream.{Attributes, Inlet, SinkShape}
import akka.stream.stage._

import scala.concurrent.duration._

class WordCounterSink extends GraphStage[SinkShape[String]] {
  val in: Inlet[String] = Inlet("WordCounterSink")
  override val shape: SinkShape[String] = SinkShape(in)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new TimerGraphStageLogic(shape) {

      var counts = Map.empty[String, Int].withDefaultValue(0)

      override def preStart(): Unit =  {
        schedulePeriodically(None, 5 seconds)
        pull(in)
      }

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val word = grab(in)
          counts += word -> (counts(word) + 1)
          pull(in)
        }
      })

      override def onTimer(timerKey: Any) = println(s"At ${System.currentTimeMillis()} count map is $counts")
    }
}