package actors

import actors.Messages.MovePieceToPosition
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
      val requestType = (json \ "msgType" ).get.toString()
      val payload = (json \ "payload" ).get

      println(s"json in actor: ${self.path}}")

      //forward base on type of message
      requestType.replace("\"", "") match {
        case "setPieceToField" => getPieceTypeFieldName(json) match {
          case Some(message) => {
            chessFieldActor ! message
          }
          case None => println("Not able to parse message 'setPieceToField' ")
        }
        case "selectField" => getSelectField(json) match {
          case Some(message) => chessFieldActor ! message
          case None => println("Not able to parse message 'select' ")
        }
        case _ => println(s"Error! websocket sent unknown type '${requestType}'")
      }
    case item:Error => {
      println(s"got error message: '${item.message}'. Forward to frontend...")
      val json = Json.toJson(item)
      out ! json.toString()
    }
    case msg: BackendReady => {
      val json = Json.toJson(msg)
      println(s"do it to out jo ${json}")
      out ! json.toString()
    }
    case msg: PieceFieldSelected => {
      val msg = new PieceFieldWasSelected()
      val json = Json.toJson(msg)
      out ! json.toString()
    }

    case msg: MovePieceToPosition => {

      println(s"moveeee(new -> ${msg.newField.field.payload.fieldName}}) ${msg}")

      val newPosition = new MovePiece(oldField = msg.origField, newField = msg.newField.field.payload.fieldName)
      val json = Json.toJson(newPosition)
      out ! json.toString()
    }

    case a: Any => println(s"not able to process message: ${a.getClass}")
  }

  /*
  private def onSelectField(json: JsValue) = {
    println("onSelectField")
    val item = genericOnMsg[PieceTypeFieldName](json)(InMessages.PieceTypeFieldNameReads)
    item match {
      case Some(message) => {
        println("to chessFieldActor")
        chessFieldActor ! message
      }
      case None => println("not able to parse message")
    }
  }
  */

  private def getSelectField(json: JsValue) = {
    println("convert PieceTypeFieldName from json to object")
    genericOnMsg[SelectField](json)(InMessages.SelectFieldReads)
  }

  private def getPieceTypeFieldName(json: JsValue) = {
    println("convert PieceTypeFieldName from json to object")
    genericOnMsg[SetPieceToField](json)(InMessages.SetPieceToFieldReads)
    /*
    item match {
      case message: Some[PieceTypeFieldName] => {
        println("to chessFieldActor")
        //chessFieldActor ! message
        message
      }
      case None => {
        println("not able to parse message")
        None
      }
    }
    */
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
