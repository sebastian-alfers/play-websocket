package actors

import actors.InMessages.{PieceFieldSelected, SelectField, SetPieceToField, PieceTypeFieldName}
import actors.Messages.{Unselect, MovePiece}
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
      val props = Props(classOf[Piece], pieceColor, pieceType, msg.payload.fieldName, context.parent)
      val pieceActor = context.actorOf(props, s"pieceActor${pieceColor}_${pieces.length}")
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

      println(s"mutate state of chess field with selected ${msg.piece}")

      //change state
      context.become(receiveWithSelectedField(msg.piece, allPieces = pieces))
    }

    case a: Any => println(s"not expected message. ${a.getClass}")
  }

  def receiveWithSelectedField(selectedPiece: ActorRef, allPieces: List[ActorRef]): Receive = {
    case msg: SelectField => {
      /**
       * possible actions now:
       * 1) the user selects a field -> move the piece there is allowed (rules)
       * 2) another piece was selected
       */

      //here, we check if the piece should move
      msg.payload.pieceType match {
        case "" => {
          //the message says, that the field where the user clicked, there is not "piece" -> lets move there (if possible)
          selectedPiece ! new MovePiece(msg)
        }
        case _ => {
          //the message says, that the field where the users clicks, there is a piece -> choose that peace (if allowed)

          //change state
          context.become(receiveWithPieces(allPieces))

          //and lets send the message
          self ! msg

        }
      }
    }
    case Unselect => {
      println("change state to unselect current selected piece")
      context.become(receiveWithPieces(allPieces))
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
