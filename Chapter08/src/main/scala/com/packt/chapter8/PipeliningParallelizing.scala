package com.packt.chapter8

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, FlowShape}
import akka.stream.scaladsl.{Balance, Flow, GraphDSL, Merge, Sink, Source}

import scala.util.Random

trait PipeliningParallelizing extends App {

  implicit val actorSystem = ActorSystem("PipeliningParallelizing")
  implicit val actorMaterializer = ActorMaterializer()

  case class Wash(id: Int)
  case class Dry(id: Int)
  case class Done(id: Int)

  val tasks = (1 to 5).map(Wash)

  def washStage = Flow[Wash].map(wash => {
    val sleepTime = Random.nextInt(3) * 1000
    println(s"Washing ${wash.id}. It will take $sleepTime milliseconds.")
    Thread.sleep(sleepTime)
    Dry(wash.id)
  })

  def dryStage = Flow[Dry].map(dry => {
    val sleepTime = Random.nextInt(3) * 1000
    println(s"Drying ${dry.id}. It will take $sleepTime milliseconds.")
    Thread.sleep(sleepTime)
    Done(dry.id)
  })

  val parallelStage = Flow.fromGraph(GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val dispatchLaundry = builder.add(Balance[Wash](3))
    val mergeLaundry = builder.add(Merge[Done](3))

    dispatchLaundry.out(0) ~> washStage.async ~> dryStage.async ~> mergeLaundry.in(0)
    dispatchLaundry.out(1) ~> washStage.async ~> dryStage.async ~> mergeLaundry.in(1)
    dispatchLaundry.out(2) ~> washStage.async ~> dryStage.async ~> mergeLaundry.in(2)

    FlowShape(dispatchLaundry.in, mergeLaundry.out)
  })

  def runGraph(testingFlow: Flow[Wash, Done, NotUsed]) = Source(tasks).via(testingFlow).to(Sink.foreach(println)).run()
}
