package akka

import akka.StarWarsChat.Drama.{Personae, Luke, Vader}
import akka.actor._
import akka.chat.RoomHandler
import akka.chat.command.{EnterRoom, SendMessage}
import akka.chat.event.Event
import akka.model.User
import play.api.libs.iteratee.Concurrent

import scala.concurrent.duration._
import scala.util.Random

object StarWarsChat {

  import scala.concurrent.ExecutionContext.Implicits.global

  val system = ActorSystem("starwars")

  val (chatOut, chatChannel) = Concurrent.broadcast[Event]

  val chatSystem = system.actorOf(Props(new RoomHandler(chatChannel)), "RoomHandler")

  class Participant(name: String, room: String) extends Actor {

    val me = User(name)

    val roomHandler = context.actorSelection("/user/RoomHandler")

    override def preStart(): Unit = roomHandler ! EnterRoom(room, me)

    def receive: Actor.Receive = {
      case (message: String) => roomHandler.tell(SendMessage(me, room, message), self)
    }
  }


  class RunTheDrama(room: String, playbook: List[Personae]) extends Actor with ActorLogging {

    var idx = 0

    val luke = context.actorOf(Props(new Participant("Luke Skywalker", room)), "luke")
    val vader = context.actorOf(Props(new Participant("Darth Vader", room)), "vader")

    def nextOnDrama(): Unit = {
      val piece = playbook(idx % playbook.length)
      log.debug(piece.toString)
      piece match {
        case Luke(quote) => luke ! quote
        case Vader(quote) => vader ! quote
      }
      idx = idx + 1
    }

    override def receive: Actor.Receive = {
      case _ => nextOnDrama()
    }
  }

  class Supervisor extends Actor {


    val first = context.actorOf(Props(new RunTheDrama("The Cloud City", Drama.theCloudCityDuel)))

    saySometingAfter(3 seconds)(first)

    def receive = {
      case _ =>
    }

    private def saySometingAfter(time: FiniteDuration)(actor: ActorRef) = context.system.scheduler.schedule(Random.nextInt(7) seconds, time, actor, SaySomething)
  }

  object SaySomething

  val supervisor = system.actorOf(Props[Supervisor])


  object Drama {

    trait Personae {
      def quote: String
    }

    case class Luke(quote: String) extends Personae

    case class Vader(quote: String) extends Personae


    val theCloudCityDuel = {
      Vader("Your destiny lies with me Skywalker. Obi-Wan knew this to be true.") ::
        Luke("No...") ::
        Vader("You are beaten. It is useless to resist") ::
        Vader("Don't let yourself be destroyed as Obi-Wan did.") ::
        Vader("There is no escape. Don't make me destroy you.") ::
        Vader("Luke, you do not yet realize your importance. You have only begun to discover your power.") ::
        Vader("Join me, and I will complete your training. With our combined strength, we can end this destructive conflict and bring order to the galaxy.") ::
        Luke("I'll never join you!") ::
        Vader("If you only knew the power of the Dark Side...") ::
        Vader("Obi-Wan never told you what happened to your father.") ::
        Luke("He told me enough!") ::
        Luke("He told me you killed him!") ::
        Vader("No. I am your father.") ::
        Luke("No...no...") ::
        Luke("That's not true....") ::
        Luke("That's impossible!") ::
        Vader("Search your feelings, you know it to be true!") ::
        Luke("[crying in anguish] No! No!") ::
        Vader("Luke, you can destroy the Emperor. He has foreseen this. It is your destiny.") ::
        Vader(" Join me, and together we can rule the galaxy as father and son.") ::
        Nil
    }
  }

}













