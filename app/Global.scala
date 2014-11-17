import akka.{StarWarsChat, ChatActors}
import play.api.GlobalSettings

object Global extends GlobalSettings {

  override def onStart(application: play.api.Application) {
    //ChatActors
   StarWarsChat
  }
  
  override def onStop(application: play.api.Application) { 
    //ChatActors.system.shutdown()
    //ChatWorld.system.shutdown()
    StarWarsChat.system.shutdown()
  }
}
