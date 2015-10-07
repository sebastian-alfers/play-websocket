package actors

import akka.actor.Actor

sealed trait PieceType
object King extends PieceType     // König
object Queen extends PieceType    // Königin
object Rock extends PieceType     // Rock
object Bishop extends PieceType   // Läufer
object Knight extends PieceType   // Pferd
object Pawn extends PieceType     // Bauer

sealed trait TeamColor
object Black extends TeamColor
object White extends TeamColor

class Piece(teamColor: TeamColor, pieceType: PieceType) extends Actor{
  def receive: Receive = {
    case _ => println("not processed in piece")
  }
}
