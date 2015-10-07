package actors

import akka.actor.{ActorRef, Props, Actor}
import play.api.Logger

object FromChessField

object WebSocketConnection {
  def props(out: ActorRef) = Props(new WebSocketConnection(out))
}

class WebSocketConnection(out: ActorRef) extends Actor {

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

