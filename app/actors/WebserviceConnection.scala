package actors

import akka.actor.{ActorSystem, ActorRef, Props, Actor}
import play.api.Logger

import scala.collection.mutable

object FromChessField

object WebserviceConnection {
  def props(out: ActorRef) = Props(new WebserviceConnection(out))
}

class WebserviceConnection(out: ActorRef) extends Actor {

  val props = Props[ChessField]
  val chessField = context.system.actorOf(props)

  def receive = {
    case msg: String =>
      Logger.info(s"got msg: ${self.path}, ${msg}")
      chessField ! "was sagste"
      //out ! ("I received your message: " + msg)

    case FromChessField => out ! "chess field sacht jo"
  }
}

