package actors

import akka.actor.ActorRef
import play.api.libs.json._

object InMessages {
  sealed trait BaseInMsg{val msgType: String}

  case class PieceTypeFieldName(pieceType: String, fieldName: String)
  implicit val PieceTypeFieldNameReads = Json.reads[PieceTypeFieldName]

  case class SetPieceToField(msgType: String, payload: PieceTypeFieldName) extends BaseInMsg
  implicit val SetPieceToFieldReads = Json.reads[SetPieceToField]

  case class SelectField(msgType: String, payload: PieceTypeFieldName) extends BaseInMsg
  implicit val SelectFieldReads = Json.reads[SelectField]

  case class PieceFieldSelected(piece: ActorRef)

  object FromChessField
}

object OutMessages{
  trait BaseOutMsg{var msgType: String}
  case class Error(override var msgType:String = "error", message: String) extends BaseOutMsg
  implicit val ErrorWrites = Json.writes[Error]

  case class BackendReady(override var msgType: String = "backendReady") extends BaseOutMsg
  implicit val BackendReadWrites = Json.writes[BackendReady]

  case class PieceFieldWasSelected(override var msgType: String = "pieceFieldWasSelected") extends BaseOutMsg
  implicit val PieceFieldWasSelectedWrites = Json.writes[PieceFieldWasSelected]

}