package actors

import actors.Messages.MovePieceToPosition
import play.api.libs.json._
import akka.actor.{ActorRef, Props, Actor}
import InMessages._
import OutMessages._


object WebSocketConnection {
  def props(out: ActorRef) = Props(new WebSocketConnection(out))
}

class WebSocketConnection(out: ActorRef) extends Actor {

  val chessFieldActor = context.actorOf(Props[ChessField])

  def receive = {

    case msg: String =>
      //parse json
      val json: JsValue = Json.parse(msg)
      val requestType = (json \ "msgType").get.toString()
      val payload = (json \ "payload").get

      println(s"json in actor: ${self.path}}")

      //forward base on type of message
      requestType.replace("\"", "") match {

        /** **********************************************************
          *                   start here
          * ***********************************************************/

        case "pingPong" => {
          println("got ping")
          //??
        }

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


        /** **********************************************************
          *                       stop here
          * ***********************************************************/

      }
    case item: Error => {
      println(s"got error message: '${item.message}'. Forward to frontend...")
      val json = Json.toJson(item)
      out ! json.toString()
    }
    case msg: BackendReady => {
      val json = Json.toJson(msg)
      out ! json.toString()
    }
    case msg: PieceFieldSelected => {
      val msg = new PieceFieldWasSelected()
      val json = Json.toJson(msg)
      out ! json.toString()
    }

    case msg: MovePieceToPosition => {
      val newPosition = new MovePiece(oldField = msg.origField, newField = msg.newField.field.payload.fieldName)
      val json = Json.toJson(newPosition)
      out ! json.toString()
    }

    case a: Any => println(s"not able to process message: ${a.getClass}")
  }

  private def getSelectField(json: JsValue) = genericOnMsg[SelectField](json)(InMessages.SelectFieldReads)

  private def getPieceTypeFieldName(json: JsValue) = genericOnMsg[SetPieceToField](json)(InMessages.SetPieceToFieldReads)

  private def genericOnMsg[T](json: JsValue)(implicit reads: Reads[T]): Option[T] = {
    json.validate[T] match {
      case s: JsSuccess[T] => Some(s.get)
      case e: JsError => {
        println(s"Error! not bale to parse message '${json.toString()}'. Error: ${JsError.toJson(e).toString()}")
        None
      }
    }
  }
}
