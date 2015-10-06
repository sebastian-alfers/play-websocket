package controllers

import actors.WS
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

case class Connection(id: Long, actor: ActorRef)

object Users{
  var list: ListBuffer[Connection] = new ListBuffer[Connection]()
}

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  val system = ActorSystem("mySystem")

  system.scheduler.schedule(1 second, 1 second) {
    println("schedule")

    Users.list.map { connection =>
      connection.actor ! s"jeajea -> ${connection.id}"
    }
  }



  def ws = WebSocket.acceptWithActor[String, String] { request => out : ActorRef =>

    //val (enumerator, channel) = Concurrent.broadcast[String]

    val props = WS.props(out)

    Users.list += Connection(request.id, out)

    Logger.info("got msg")
    Logger.info(s"${request.id}")

    props
  }

}
