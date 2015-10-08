package actors

import actors.InMessages.{PieceFieldSelected, SelectField, SetPieceToField, PieceTypeFieldName}
import actors.OutMessages.{BackendReady, Error}
import akka.actor.{ActorRef, Props, Actor}

import scala.Error

/**
 * actor that represents our chess field
 */
class ChessField extends Actor{

  //val whiteTeamActor = context.system.actorOf(Props(classOf[Team], White))
  //val blackTeamActor = context.system.actorOf(Props(classOf[Team], Black))

  def receive: Receive = receiveWithPieces(List())

  def receiveWithPieces(pieces: List[ActorRef]): Receive = {
    case msg: SetPieceToField => {

      //"Die Schach-Figur"
      val pieceType = msg.payload.pieceType.splitAt(5)._2

      val pieceColor = msg.payload.pieceType
      val pieceActor = context.actorOf(Props(classOf[Piece], pieceColor, pieceType, msg.payload.fieldName))
      val newActors = pieceActor::pieces

      if(newActors.length == 32){
        //tell the frontend all peaces are initialized
        context.parent ! new BackendReady
      }

      context.become(receiveWithPieces(newActors))
    }
    case msg: SelectField => {
      //check if selection is valid
      println(s"selected: ${msg}")
      pieces.foreach { piece =>
        piece ! msg
      }
    }

    case msg: PieceFieldSelected => {
      //tell parent -> frontend that state has hanged
      context.parent ! msg

      //change state
      context.become(receiveWithSelectedField(msg.piece, allPieces = pieces))
    }

    case _ => println("not expected message")
  }

  def receiveWithSelectedField(selectedPiece: ActorRef, allPieces: List[ActorRef]): Receive = {
    case msg: SelectField => {
      //a piece is selected, but a new one was chosen

      //change state
      context.become(receiveWithPieces(allPieces))

      //and lets send the message
      self ! msg
    }
    case a: Any => println(s"msg not expected in state selectedField: ${a.getClass}")
  }


  def messageFromBlackTeam(msg: PieceTypeFieldName) = {
    println("messageFromBlackTeam")
    //println(s"send msg to parens: ${context.parent.path}")
    //context.parent ! Error(message = "test error black")
  }

  def messageFromWhiteTeam(msg: PieceTypeFieldName) = {
    println("messageFromWhiteTeam")

  }


}
