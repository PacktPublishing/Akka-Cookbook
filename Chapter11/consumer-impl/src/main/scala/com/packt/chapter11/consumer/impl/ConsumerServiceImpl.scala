package com.packt.chapter11.consumer.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.packt.chapter11.consumer.api.ConsumerService
import com.packt.chapter11.token.api.{TokenService, ValidateTokenRequest}

import scala.concurrent.ExecutionContext

class ConsumerServiceImpl(tokenService: TokenService)(implicit ec: ExecutionContext)  extends ConsumerService {
  override def consume = ServiceCall { request =>
    val validateTokenRequest = ValidateTokenRequest(request.clientId, request.token)
    tokenService.validateToken.invoke(validateTokenRequest).map(_.successful)
  }
}
