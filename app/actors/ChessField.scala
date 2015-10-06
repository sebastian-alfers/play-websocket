package actors

import akka.actor.Actor


class ChessField extends Actor{
  def receive: Receive = {
    case msg: String => {
      println("msg")
      sender ! FromChessField
    }
  }
}
