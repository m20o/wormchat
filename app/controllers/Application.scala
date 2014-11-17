package controllers

import akka.StarWarsChat
import akka.chat.event.Event
import controllers.ChatApplication._
import play.api.libs.EventSource
import play.api.libs.iteratee.{Enumeratee, Concurrent}
import play.api.libs.json.{Json, JsValue}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  /** Central hub for distributing chat messages */

  val chatOut = StarWarsChat.chatOut

  /** Controller action serving AngularJS chat page */
  def index = Action {
    Ok(views.html.myindex("Chat using Server Sent Events and AngularJS"))
  }

  /** Controller action for POSTing chat messages */
  //def postMessage = Action(parse.json) { req => chatChannel.push(req.body); Ok}

  /** Enumeratee for filtering messages based on room */
  def filter(room: String) = Enumeratee.filter[JsValue] { json: JsValue => (json \ "room").as[String] == room}

  //def toJs= Enumeratee.map[Event] { ev => Json.toJson(ev)  }

  /** Enumeratee for detecting disconnect of SSE stream */
  def connDeathWatch(addr: String): Enumeratee[JsValue, JsValue] =
    Enumeratee.onIterateeDone { () => println(addr + " - SSE disconnected")}

  /** Controller action serving activity based on room
  def chatFeed = Action { req =>
    println(req.remoteAddress + " - SSE connected")
    Ok.feed(chatOut
      &> toJs
      &> Concurrent.buffer(50)
      &> connDeathWatch(req.remoteAddress)
      &> EventSource()
    ).as("text/event-stream")

  }

    */
}