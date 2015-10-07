package actors

import actors.InMessages.{SetPieceToField}
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

      //"Die Figur"
      val pieceType = msg.pieceType.splitAt(5)._2 match {
        case "King" => King
        case "Queen" => Queen
        case "Rock" => Rock
        case "Bishop" => Bishop
        case "Knight" => Knight
        case "Pawn" => Pawn
      }

      val pieceColor = msg.pieceType.startsWith("black") match {
        case true => Black
        case false => White
      }
      val pieceActor = context.actorOf(Props(classOf[Piece], pieceColor, pieceType))
      val newActors = pieceActor::pieces

      if(newActors.length == 32){
        //tell the frontend all peaces are initialized
        context.parent ! new BackendReady
      }

      context.become(receiveWithPieces(newActors))
    }
    case _ => println("not expected message")
  }


  def messageFromBlackTeam(msg: SetPieceToField) = {
    println("messageFromBlackTeam")
    //println(s"send msg to parens: ${context.parent.path}")
    //context.parent ! Error(message = "test error black")
  }

  def messageFromWhiteTeam(msg: SetPieceToField) = {
    println("messageFromWhiteTeam")

  }


}
