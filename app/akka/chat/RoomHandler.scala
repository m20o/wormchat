package akka.chat

import akka.actor.{ActorRef, Props}
import akka.chat.command._
import akka.chat.event._
import akka.persistence.PersistentActor

import scala.collection.mutable

class RoomHandler(bus: EventBus) extends PersistentActor {

  val persistenceId: String = "room-handler"

  private val rooms: mutable.Map[String, ActorRef] = mutable.Map.empty

  override def receiveRecover: Receive = {
    case _ =>
  }

  override def receiveCommand: Receive = {
    case CreateRoom(roomName) if !roomExists(roomName) => persist(RoomCreated(roomName)) { ev =>
      updateOpenRooms(ev)
      bus.push(ev)
      context.system.eventStream.publish(ev)
    }

    case e@EnterRoom(roomName, _) => forwardToRoom(roomName, e)

    case e@SendMessage(_, roomName, _) => forwardToRoom(roomName, e)

    case ListAvailableRooms => sender() ! rooms.keySet
  }

  private def forwardToRoom(roomName: String, event: Any): Unit = rooms.get(roomName).map { actor =>
    actor.forward(event)
  }

  private def roomExists(roomName: String) = rooms.contains(roomName)

  def updateOpenRooms(ev: RoomCreated) {
    val reference = context.actorOf(Props(new Room(ev.room, bus)))
    rooms.update(ev.room, reference)
  }
}
