package actors

import akka.actor.Actor

trait TeamColor
object White extends TeamColor
object Black extends TeamColor

class Team(color: TeamColor) extends Actor{

  def receive: Receive = {

    case _ => println("message not processed")
  }

}
