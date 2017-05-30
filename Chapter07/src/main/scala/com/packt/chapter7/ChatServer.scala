package com.packt.chapter7

import akka.actor.{Actor, ActorRef, Props, Terminated}

object ChatServer {
  case object Connect
  case object Disconnect
  case object Disconnected
  case class Message(author: ActorRef, body: String, creationTimestamp : Long = System.currentTimeMillis())

  def props = Props(new ChatServer())
}

class ChatServer extends Actor {
  import ChatServer._

  var onlineClients = Set.empty[ActorRef]

  def receive = {
    case Connect =>
      onlineClients += sender
      context.watch(sender)
    case Disconnect =>
      onlineClients -= sender
      context.unwatch(sender)
      sender ! Disconnected
    case Terminated(ref) =>
      onlineClients -= ref
    case msg: Message =>
      onlineClients.filter(_ != sender).foreach(_ ! msg)
  }
}
