package akka.chat.event

import akka.model.User

case class Joined(user: User, room: String) extends Event
