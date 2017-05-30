package com.packt.chapter8

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.amqp._
import akka.stream.alpakka.amqp.scaladsl.{AmqpSink, AmqpSource}
import akka.util.ByteString

object ProcessingRabbitMQApplication extends App {

  implicit val actorSystem = ActorSystem("SimpleStream")
  implicit val actorMaterializer = ActorMaterializer()

  val consumerQueueName = "akka_streams_consumer_queue"
  val consumerQueueDeclaration = QueueDeclaration(consumerQueueName)
  val sourceDeclarations = Seq(consumerQueueDeclaration)

  val exchangeName = "akka_streams_exchange"
  val exchangeDeclaration = ExchangeDeclaration(exchangeName, "direct")
  val destinationQueueName = "akka_streams_destination_queue"
  val destinationQueueDeclaration = QueueDeclaration(destinationQueueName)
  val bindingDeclaration = BindingDeclaration(destinationQueueName, exchangeName)
  val sinkDeclarations = Seq(exchangeDeclaration, destinationQueueDeclaration, bindingDeclaration)

  val credentials = AmqpCredentials("guest", "guest")
  val connectionSetting = AmqpConnectionDetails("127.0.0.1", 5672, Some(credentials))
  val amqpSourceConfig = NamedQueueSourceSettings(connectionSetting, consumerQueueName, sourceDeclarations)
  val rabbitMQSource = AmqpSource(amqpSourceConfig, 1000)
  val amqpSinkConfig = AmqpSinkSettings(connectionSetting, Some(exchangeName), None, sinkDeclarations)
  val rabbitMQSink = AmqpSink(amqpSinkConfig)

  val stream = rabbitMQSource
      .map(incomingMessage => {
        val upperCased = incomingMessage.bytes.utf8String.toUpperCase
        OutgoingMessage(bytes = ByteString(upperCased),
          immediate = false,
          mandatory = false,
          props = None)
      })
    .to(rabbitMQSink)

  stream.run()
}
