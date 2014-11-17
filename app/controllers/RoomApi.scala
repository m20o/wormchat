package controllers

import akka.StarWarsChat
import akka.chat.event.Event
import controllers.ChatApplication._
import play.api.libs.EventSource
import play.api.libs.iteratee.{Concurrent, Enumeratee}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Action

import akka.pattern._
import play.api.mvc.Controller


object RoomApi extends Controller {

  import scala.concurrent.ExecutionContext.Implicits.global

  def list() = Action {
    val result = List("MeetingRoom", "OnlyRomeo", "OnlyJuliet")
    Ok(Json.toJson(result))
  }

  val output = StarWarsChat.chatOut

  def feed(room: String) = Action { req =>
    Ok.feed(output
      &> filter(room)
      &> Concurrent.buffer(50)
      &> connDeathWatch(req.remoteAddress)
      &> toJsEvent
      &> EventSource()
    ).as("text/event-stream")
  }

  /** Enumeratee for filtering messages based on room */
  def filter(room: String) = Enumeratee.filter[akka.chat.event.Event] { ev => ev.room == room}

  def toJsEvent: Enumeratee[Event, JsValue] = Enumeratee.mapInput[Event] {
    case e: Event => e.map(Json.toJson(_))
  }

  /** Enumeratee for detecting disconnect of SSE stream */
  def connDeathWatch(addr: String): Enumeratee[Event, Event] = {
    Enumeratee.onIterateeDone { () => println(addr + " - SSE disconnected")}
  }
}