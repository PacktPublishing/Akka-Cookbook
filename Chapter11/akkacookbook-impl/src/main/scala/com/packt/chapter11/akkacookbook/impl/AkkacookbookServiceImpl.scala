package com.packt.chapter11.akkacookbook.impl

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.{ServiceCall, ServiceLocator}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.packt.chapter11.akkacookbook.api.AkkacookbookService

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

//class AkkacookbookServiceImpl(per: PersistentEntityRegistry, serviceLocator: ServiceLocator)
//                             (implicit ex: ExecutionContext) extends AkkacookbookService {
//
//  override def healthCheck() = ServiceCall { _ =>
//    Await.result(serviceLocator.locate("login"), 2 seconds) match {
//      case Some(serviceUri) => println(s"Service found at: [$serviceUri]")
//      case None => println("Service not found")
//    }
//    Future.successful(Done)
//  }
//
//  override def hello(id: String) = ServiceCall { _ =>
//    val ref = per.refFor[AkkacookbookEntity](id)
//    ref.ask(Hello(id, None))
//  }
//
//  override def useGreeting(id: String) = ServiceCall { request =>
//    val ref = per.refFor[AkkacookbookEntity](id)
//    ref.ask(UseGreetingMessage(request.message))
//  }
//}

class AkkacookbookServiceImpl extends AkkacookbookService {
  override def toUppercase = ServiceCall { x => Future.successful(x.toUpperCase) }
  override def toLowercase = ServiceCall { x => Future.successful(x.toLowerCase) }
  override def isEmpty(str: String) = ServiceCall { _ => Future.successful(str.isEmpty) }
  override def areEqual(str1: String, str2: String) = ServiceCall { _ => Future.successful(str1 == str2)}
}