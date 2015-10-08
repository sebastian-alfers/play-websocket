package actors

import actors.InMessages.SelectField
import akka.actor.ActorRef
import play.api.libs.json._

object Messages{
  case class MovePiece(field: SelectField)

  object FromChessField
  object Unselect

  case class MovePieceToPosition(origField: String, newField: MovePiece)

  case class PieceFieldSelected(piece: ActorRef)

}

object InMessages {
  sealed trait BaseInMsg{val msgType: String}

  case class PieceTypeFieldName(pieceType: String, fieldName: String)
  implicit val PieceTypeFieldNameReads = Json.reads[PieceTypeFieldName]

  case class SetPieceToField(msgType: String, payload: PieceTypeFieldName) extends BaseInMsg
  implicit val SetPieceToFieldReads = Json.reads[SetPieceToField]

  case class SelectField(msgType: String, payload: PieceTypeFieldName) extends BaseInMsg
  implicit val SelectFieldReads = Json.reads[SelectField]

}

object OutMessages{
  trait BaseOutMsg{var msgType: String}
  case class Error(override var msgType:String = "error", message: String) extends BaseOutMsg
  implicit val ErrorWrites = Json.writes[Error]

  case class BackendReady(override var msgType: String = "backendReady") extends BaseOutMsg
  implicit val BackendReadWrites = Json.writes[BackendReady]

  case class PieceFieldWasSelected(override var msgType: String = "pieceFieldWasSelected") extends BaseOutMsg
  implicit val PieceFieldWasSelectedWrites = Json.writes[PieceFieldWasSelected]

  case class MovePiece(override var msgType: String = "moveField", oldField: String, newField: String) extends BaseOutMsg
  implicit val MovePieceWrites = Json.writes[MovePiece]
}