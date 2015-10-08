package actors

import actors.InMessages.{PieceFieldSelected, SelectField}
import akka.actor.Actor

/*
sealed trait PieceType{val label: String}
case class King(label: String = "King") extends PieceType     // König
case class Queen(label: String = "Queen") extends PieceType    // Königin
case class Rock(label: String = "Rock") extends PieceType     // Rock
case class Bishop(label: String = "Bishop") extends PieceType   // Läufer
case class Knight(label: String = "Knight") extends PieceType   // Pferd
case class Pawn(label: String = "Pawn") extends PieceType     // Bauer

sealed trait TeamColor
object Black extends TeamColor
object White extends TeamColor
*/

class Piece(teamColor: String, pieceType: String, field: String) extends Actor{
  def receive: Receive = {
    case msg: SelectField => {
      //is the selection equal to this actor?
      if(msg.payload.fieldName == field){
        sender ! new PieceFieldSelected(self)
      }
    }
    case _ => println("not processed in piece")
  }
}
