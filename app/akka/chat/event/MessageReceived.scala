package akka.chat.event

import java.util.Date

import akka.model.User

case class MessageReceived(room: String, user: User, text: String, timestamp: Date = new Date) extends Event
