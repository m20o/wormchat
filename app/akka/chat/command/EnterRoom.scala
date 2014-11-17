package akka.chat.command

import akka.model.User

case class EnterRoom(name: String, user: User)
