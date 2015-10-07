package actors

import actors.InMessages.{SetPieceToField}
import actors.OutMessages.Error
import akka.actor.{ActorRef, Props, Actor}

import scala.Error

/**
 * actor that represents our chess field
 */
class ChessField extends Actor{

  val whiteTeamActor = context.system.actorOf(Props(classOf[Team], White))
  val blackTeamActor = context.system.actorOf(Props(classOf[Team], Black))

  def receive: Receive = {
    case msg: SetPieceToField => {

      //"Die Figur"
      val pieceType = msg.pieceType

      pieceType.startsWith("black") match {
        case true => messageFromBlackTeam(msg)
        case false => {

          pieceType.startsWith("white") match {
            case true => messageFromWhiteTeam(msg)
            case false => context.parent ! Error(message = "fieldName in message SetPieceToField must either start with 'black' or 'white' for the corresponding team")
          }
        }
      }
    }
    case _ => println("not expected message")
  }

  def messageFromBlackTeam(msg: SetPieceToField) = {
    println("messageFromBlackTeam")
    println(s"send msg to parens: ${context.parent.path}")
    context.parent ! Error(message = "test error black")
  }

  def messageFromWhiteTeam(msg: SetPieceToField) = {
    println("messageFromWhiteTeam")
  }

}
