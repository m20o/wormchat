package akka

package object chat {

  import play.api.libs.iteratee.Concurrent.Channel
  import akka.chat.event.Event

  type EventBus = Channel[Event]

}
