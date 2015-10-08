package actors

import actors.InMessages.{PieceFieldSelected, SelectField}
import actors.Messages.{Unselect, MovePieceToPosition, MovePiece}
import akka.actor.{ActorRef, Actor}

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

class Piece(teamColor: String, pieceType: String, initialField: String, connectionActor: ActorRef) extends Actor{

  def receive: Receive = receive(initialField)

  def receive(stateFieldName :String): Receive = {
    case msg: SelectField => {
      val newField = msg.payload.fieldName
      //is the selection equal to this actor?

      println(s"compare ${newField} == ${stateFieldName}")

      if(newField == stateFieldName){
        sender ! new PieceFieldSelected(self)
      }
    }

    case msg: MovePiece => {
      val newField = msg.field.payload.fieldName

      println(s"the piece ${teamColor},${pieceType}, old field: ${stateFieldName} was asked to move to new field ${msg.field.payload.fieldName}")

      //here comes the important part: check rules if piece can go there

      //tell the frontend to move the piece there
      connectionActor ! new MovePieceToPosition(origField = stateFieldName, msg)
      context.parent ! Unselect



      //need to mutate this actor to be on the new position
      context.become(receive(newField))
      println(s"mutate actor ${self.path} to be on field ${newField}")
    }

    case _ => println("not processed in piece")
  }
}
