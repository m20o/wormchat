package controllers.ui

import play.api.mvc.{Action, Controller}

object MainApplication extends Controller  {

  def index = Action {
    Ok(views.html.system())
  }

}
