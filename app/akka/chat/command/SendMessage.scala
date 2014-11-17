package akka.chat.command

import akka.model.User

case class SendMessage(user: User, room: String, text: String)
