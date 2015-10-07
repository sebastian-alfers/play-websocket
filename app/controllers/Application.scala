package controllers

import actors.WebSocketConnection
import play.api.mvc._
import play.api.Play.current
import akka.actor.ActorRef

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def hello = Action {
    Ok("It works!")
  }


  def ws = WebSocket.acceptWithActor[String, String] { request => out : ActorRef =>
    val props = WebSocketConnection.props(out)
    props
  }

}
