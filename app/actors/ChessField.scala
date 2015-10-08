package actors

import akka.actor.Actor


class ChessField extends Actor{
  def receive: Receive = {
    case a: Any => println(s"not able to parse message of type '${a.getClass}'")
  }
}
