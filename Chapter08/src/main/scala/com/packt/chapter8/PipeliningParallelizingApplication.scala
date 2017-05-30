package com.packt.chapter8

import akka.stream.scaladsl._

object SynchronousPipeliningApplication extends PipeliningParallelizing {
  runGraph(Flow[Wash].via(washStage).via(dryStage))
}

object AsynchronousPipeliningApplication extends PipeliningParallelizing {
  runGraph(Flow[Wash].via(washStage.async).via(dryStage.async))
}

object ParallelizingApplication extends PipeliningParallelizing {
  runGraph(Flow[Wash].via(parallelStage))
}