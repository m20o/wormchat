package akka.chat

import akka.chat.command._
import akka.chat.event._
import akka.model.User
import akka.persistence.PersistentActor
import play.api.libs.iteratee.Concurrent.Channel

import scala.collection.mutable


class Room(name: String, bus: EventBus) extends PersistentActor {

  val persistenceId: String = name.hashCode.toString

  private val participants: mutable.Set[User] = mutable.Set.empty

  override def receiveRecover: Receive = {
    case _ =>
  }

  override def receiveCommand: Receive = {

    case EnterRoom(_, user) => {
      val joined = Joined(user, name)
      persist(joined) { ev =>
        participants.add(user)
        publishOnBus(ev)
      }
    }

    case SendMessage(user, _, text) => {
      val received = MessageReceived(name, user, text)
      persistAsync(received) { ev =>
        publishOnBus(ev)
      }
    }
  }

  def publishOnBus(ev: Event) {
    context.system.eventStream.publish(ev)
    bus.push(ev)
  }
}
