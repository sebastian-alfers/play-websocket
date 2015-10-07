package actors

import akka.actor.Actor.emptyBehavior
import play.api.libs.json._
import akka.actor.{ActorRef, Props, Actor}
import InMessages._
import OutMessages._

/**
 * companion object -> kind of factory for the class
 */
object WebSocketConnection {
  def props(out: ActorRef) = Props(new WebSocketConnection(out))
}

class WebSocketConnection(out: ActorRef) extends Actor {

  val chessFieldActor = context.actorOf(Props[ChessField])

  def receive = {
    case msg: String =>
      //parse json
      val json: JsValue = Json.parse(msg)
      val requestType = (json \ "type" ).get.toString()
      val payload = (json \ "payload" ).get

      println(s"json in actor: ${self.path}}")

      //forward base on type of message
      requestType.replace("\"", "") match {
        case "setPieceToField" => onSetPieceToFieldMsg(payload)
        case _ => println(s"Error! websocket sent unknown type '${requestType}'")
      }
    case item:Error => {
      println(s"got error message: '${item.message}'. Forward to frontedn...")
      val json = Json.toJson(item)
      out ! json.toString()
    }
    case msg: BackendReady => {
      val json = Json.toJson(msg)
      println(s"do it to out jo ${json}")
      out ! json.toString()
    }
    case a: Any => println(s"not able to process message: ${a.getClass}")
  }

  private def onSetPieceToFieldMsg(json: JsValue) = {
    println("onSetPieceToField")
    val item = genericOnMsg[SetPieceToField](json)(InMessages.SetPieceToFieldReads)
    item match {
      case Some(message) => {
        println("to chessFieldActor")
        chessFieldActor ! message
      }
      case None => println("not able to parse message")
    }
  }

  private def genericOnMsg[T](json: JsValue)(implicit reads: Reads[T]): Option[T] = {
    json.validate[T] match {
      case s: JsSuccess[T] => Some(s.get)
      case e: JsError => {
        println(s"Error! not bale to parse message '${json.toString()}'. Error: ${ JsError.toJson(e).toString() }")
        None
      }
    }
  }
}
