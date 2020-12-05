package tenet.simulator.parse

import tenet.simulator.asm.Asm
import scala.util.Try
import scala.util.{Success => ScalaSuccess, Failure => ScalaFailure}

class Parser {
  private val parser = new ParserSyntax.Commands

  import parser._

  def parse(
    string: String
  ): Try[List[Asm]] =
    parser.parse(parser.commands, string) match {
      case Success(result, next) =>
        if (next.atEnd) ScalaSuccess(result)
        else ScalaFailure(ParseError(s"Parser cannot reach at the end of code."))
      case e: NoSuccess =>
        ScalaFailure(ParseError(s"Parse failed!!!!! (${e.msg})."))
    }
}
