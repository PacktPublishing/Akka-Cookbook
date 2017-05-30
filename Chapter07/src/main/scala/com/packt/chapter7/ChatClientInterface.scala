package com.packt.chapter7

import akka.actor.{Actor, ActorRef, Props}
import com.packt.chapter7.ChatServer.Disconnect

import scala.io.StdIn._

object ChatClientInterface {
  case object Check

  def props(chatClient: ActorRef) = Props(new ChatClientInterface(chatClient))
}

class ChatClientInterface(chatClient: ActorRef) extends Actor {
  import ChatClientInterface._

  override def preStart() = {
    println("You are logged in. Please type and press enter to send messages. Type 'DISCONNECT' to log out.")
    self ! Check
  }

  def receive = {
    case Check =>
      readLine() match {
        case "DISCONNECT" =>
          chatClient ! Disconnect
          println("Disconnecting...")
          context.stop(self)
        case msg =>
          chatClient ! msg
          self ! Check
      }
  }
}
