package actors
import play.api.libs.json._

object Messages {
  case class SetPieceToField(pieceType: String, fieldName: String)
  implicit val SetPieceToFieldReads = Json.reads[SetPieceToField]


  object FromChessField
}
