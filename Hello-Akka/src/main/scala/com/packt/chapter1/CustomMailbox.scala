package com.packt.chapter1

import java.util.concurrent.ConcurrentLinkedQueue

import akka.actor.{Props, Actor, ActorSystem, ActorRef}

import akka.dispatch.{MailboxType, ProducesMessageQueue, Envelope, MessageQueue}

import com.typesafe.config.Config

/**
  * Created by user
  */
object CustomMailbox extends App  {

  val actorSystem = ActorSystem("HelloAkka")
  val actor = actorSystem.actorOf(Props[MySpecialActor].withDispatcher("custom-dispatcher"))
  val actor1 = actorSystem.actorOf(Props[MyActor],"xyz")

  val actor2 = actorSystem.actorOf(Props[MyActor],"MyActor")

  actor1 !  ("hello", actor)
  actor2 !  ("hello", actor)
  
}

class MySpecialActor extends Actor {
  override def receive: Receive = {
    case msg: String => println(s"msg is $msg" )
  }
}
class MyActor extends Actor {
  override def receive: Receive = {
    case (msg: String, actorRef: ActorRef) => actorRef ! msg
    case msg => println(msg)
  }
}



trait MyUnboundedMessageQueueSemantics

  // This is the MessageQueue implementation
  class MyMessageQueue extends MessageQueue
  {

    private final val queue = new ConcurrentLinkedQueue[Envelope]()

    // these should be implemented; queue used as example
    def enqueue(receiver: ActorRef, handle: Envelope): Unit = {
      if(handle.sender.path.name == "MyActor") {
        handle.sender !  "Hey dude, How are you?, I Know your name,processing your request"
          queue.offer(handle)
          }
          else handle.sender ! "I don't talk to strangers, I can't process your request"
    }
    def dequeue(): Envelope = queue.poll
    def numberOfMessages: Int = queue.size
    def hasMessages: Boolean = !queue.isEmpty
    def cleanUp(owner: ActorRef, deadLetters: MessageQueue) {
      while (hasMessages) {
        deadLetters.enqueue(owner, dequeue())
      }
    }
}

class MyUnboundedMailbox extends MailboxType
with ProducesMessageQueue[MyMessageQueue] {

  // This constructor signature must exist, it will be called by Akka
  def this(settings: ActorSystem.Settings, config: Config) = {
    // put your initialization code here
    this()
  }

  // The create method is called to create the MessageQueue
  final override def create(owner: Option[ActorRef],
                            system: Option[ActorSystem]): MessageQueue =
    new MyMessageQueue()
}