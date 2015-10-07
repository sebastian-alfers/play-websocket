package controllers

import actors.{WebserviceConnection, WebserviceConnection$}
import play.api._
import play.api.libs.iteratee.{Concurrent, Enumerator, Iteratee}
import play.api.mvc._
import play.api.mvc._
import play.api.Play.current

import akka.actor.{Props, ActorRef, ActorSystem, Actor}
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.iteratee.Concurrent
import play.api.Logger
import play.api.libs.iteratee.Iteratee
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.duration._
import scala.collection.mutable.ListBuffer

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def hello = Action {
    Ok("It works!")
  }


  def ws = WebSocket.acceptWithActor[String, String] { request => out : ActorRef =>
    val props = WebserviceConnection.props(out)
    props
  }

}
