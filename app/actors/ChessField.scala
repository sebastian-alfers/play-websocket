package actors

import actors.Messages.{SetPieceToField, FromChessField}
import akka.actor.Actor

/**
 * actor that represents our chess field
 */
class ChessField extends Actor{


  def receive: Receive = {
    case msg: SetPieceToField => {
      //this is our first message, but we have not team set up -> lets do it!

      val whiteTeamActor = new Actor(P)

    }
    case _ => println("not expected message")
  }

  def receiveWithTeams(whiteTeamActor: Team, blackTeamActor: Team): Receive = {

    case _ => println("not expected message")
  }
}
