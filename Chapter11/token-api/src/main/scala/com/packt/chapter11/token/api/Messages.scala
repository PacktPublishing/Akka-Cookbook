package com.packt.chapter11.token.api

import play.api.libs.json.{Format, Json}

case class RetrieveTokenRequest(clientId: String, clientSecret: String)
case class RetrieveTokenResult(successful: Boolean, token: Option[String] = None)
case class ValidateTokenRequest(clientId: String, token: String)
case class ValidateTokenResult(successful: Boolean)

object RetrieveTokenRequest {
  implicit val retrieveTokenRequestFormat: Format[RetrieveTokenRequest] = Json.format[RetrieveTokenRequest]
}
object RetrieveTokenResult {
  implicit val retrieveTokenResultFormat: Format[RetrieveTokenResult] = Json.format[RetrieveTokenResult]
}
object ValidateTokenRequest {
  implicit val validateTokenRequestFormat: Format[ValidateTokenRequest] = Json.format[ValidateTokenRequest]
}
object ValidateTokenResult {
  implicit val validateTokenResultFormat: Format[ValidateTokenResult] = Json.format[ValidateTokenResult]
}