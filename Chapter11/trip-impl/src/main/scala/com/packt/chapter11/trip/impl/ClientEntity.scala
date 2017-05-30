package com.packt.chapter11.trip.impl

import java.util.Date
import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class ClientEntity extends PersistentEntity {
  override type Command = ClientCommand[_]
  override type Event = ClientEvent
  override type State = ClientState

  override def initialState = ClientState(false, Nil)

  override def behavior: Behavior =
    Actions()
      .onCommand[StartTrip.type, Done] {
      case (_, ctx, state) if !state.tripInProgress =>
        ctx.thenPersist(TripStarted(new Date().getTime)) { _ => ctx.reply(Done) }
      case (_, ctx, _) =>
        ctx.invalidCommand("The trip has started already.")
        ctx.done
    }
      .onCommand[EndTrip.type, Done] {
      case (_, ctx, state) if state.tripInProgress =>
        ctx.thenPersist(TripEnded(new Date().getTime)) { _ => ctx.reply(Done) }
      case (_, ctx, _)  =>
        ctx.invalidCommand("The trip has not started.")
        ctx.done
    }
      .onCommand[AddLocation, Done] {
      case (AddLocation(req), ctx, state) if state.tripInProgress =>
        ctx.thenPersist(LocationAdded(Location(req.latitude, req.longitude))) { _ => ctx.reply(Done) }
      case (_, ctx, _) =>
        ctx.invalidCommand("The trip has not started.")
        ctx.done
    }
      .onEvent {
        case (TripStarted(_), _) => ClientState(true, Nil)
        case (TripEnded(_), _) => ClientState(false, Nil)
        case (LocationAdded(loc), state) => state.copy(locations = state.locations :+ loc)
      }
}



