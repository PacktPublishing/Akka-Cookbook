package com.packt.chapter11.trip.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.packt.chapter11.trip.api.TripService

class TripServiceImpl(per: PersistentEntityRegistry) extends TripService {
  override def startTrip(clientId: String) = ServiceCall { _ =>
    per.refFor[ClientEntity](clientId).ask(StartTrip)
  }

  override def reportLocation(clientId: String) = ServiceCall { req =>
    per.refFor[ClientEntity](clientId).ask(AddLocation(req))
  }

  override def endTrip(clientId: String) = ServiceCall { _ =>
    per.refFor[ClientEntity](clientId).ask(EndTrip)
  }
}
