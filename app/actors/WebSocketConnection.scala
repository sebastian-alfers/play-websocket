package actors

import akka.actor.Actor.emptyBehavior
import play.api.libs.json._
import akka.actor.{ActorRef, Props, Actor}
import play.api.Logger
import Messages._

/**
 * companion object -> kind of factory for the class
 */
object WebSocketConnection {
  def props(out: ActorRef) = Props(new WebSocketConnection(out))
}

class WebSocketConnection(out: ActorRef) extends Actor {

  val chessFieldActor = context.system.actorOf(Props[ChessField])

  def receive = {
    case msg: String =>
      //parse json
      val json: JsValue = Json.parse(msg)
      val requestType = (json \ "type" ).get.toString()
      val payload = (json \ "payload" ).get

      //forward base on type of message
      requestType match {
        case "setPieceToField" => onSetPieceToFieldMsg(payload)
        case _ => println(s"Error! websocket sent unknown type '${requestType}'")
      }

    case FromChessField => out ! "chess field sacht jo"
  }

  private def onSetPieceToFieldMsg(json: JsValue) = {
    val item = genericOnMsg[SetPieceToField](json)
    item match {
      case Some(message) => chessFieldActor ! message
      case None => println("not able to parse message")
    }
  }

  private def genericOnMsg[T](json: JsValue): Option[T] = {
    json.validate[T] match {
      case s: JsSuccess[T] => Some(s.get)
      case e: JsError => {
        println(s"Error! not bale to parse message '${json.toString()}'. Error: ${ JsError.toJson(e).toString() }")
        None
      }
    }
  }
}
