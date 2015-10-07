package actors
import play.api.libs.json._

object InMessages {
  case class SetPieceToField(pieceType: String, fieldName: String)
  implicit val SetPieceToFieldReads = Json.reads[SetPieceToField]

  object FromChessField
  
  
}

object OutMessages{
  trait BaseOutMsg{var msgType: String}
  case class Error(override var msgType:String = "error", message: String) extends BaseOutMsg
  implicit val ErrorWrites = Json.writes[Error]
}