package controllers

import actors.WS
import play.api._
import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.mvc._
import play.api.mvc._
import play.api.Play.current

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def ws = WebSocket.acceptWithActor[String, String] { request => out =>
    Logger.info("got msg")
    Logger.info(s"${request.id}")
    WS.props(out)
  }

}
