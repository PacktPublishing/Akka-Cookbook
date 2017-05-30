package com.packt.chapter11.token.api

import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait TokenService extends Service {
  def retrieveToken: ServiceCall[RetrieveTokenRequest, RetrieveTokenResult]
  def validateToken: ServiceCall[ValidateTokenRequest, ValidateTokenResult]

  override final def descriptor = {
    import Service._
    named("token").withCalls(
      pathCall("/token/retrieve", retrieveToken),
      pathCall("/token/validate", validateToken)
    ).withAutoAcl(true)
  }
}

