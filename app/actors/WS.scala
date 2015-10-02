package actors

import akka.actor.{ActorRef, Props, Actor}
import play.api.Logger

object WS {
  def props(out: ActorRef) = Props(new WS(out))
}

class WS(out: ActorRef) extends Actor {
  def receive = {
    case msg: String =>
      Logger.info(s"got actor: ${self.path}")
      out ! ("I received your message: " + msg)
  }
}