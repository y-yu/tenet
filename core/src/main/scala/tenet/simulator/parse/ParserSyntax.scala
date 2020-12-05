package tenet.simulator.parse

import tenet.simulator.asm.Asm
import tenet.simulator.asm.Asm._
import tenet.simulator.register.RegisterName
import tenet.simulator.register.RegisterName._
import scala.util.parsing.combinator.RegexParsers

object ParserSyntax {
  private[parse] class Commands extends RegexParsers { self =>
    override def skipWhitespace = false

    val name: Parser[String] = """\w+""".r

    def registersName: Parser[RegisterName] =
      name.map { str =>
        str.toLowerCase match {
          case "eax" => Eax()
          case "ebx" => Ebx()
          case "ecx" => Ecx()
        }
      }

    val delimiter: Parser[String] =
      whiteSpace.* ~> ",".r <~ whiteSpace.*

    val number: Parser[Int] =
      """[-+]?\d+""".r.map { str =>
        str.toInt
      }

    val cmp: Parser[Cmp[RegisterName]] =
      (("""(?i)cmp""".r ~> whiteSpace ~> registersName) ~ (delimiter ~> number)).map {
        case target ~ value =>
          Cmp(target, value)
      }

    val add: Parser[Add[RegisterName]] =
      (("""(?i)add""".r ~> whiteSpace ~> registersName) ~ (delimiter ~> number)).map {
        case target ~ value =>
          Add(target, value)
      }

    val mul: Parser[Mul[RegisterName]] =
      (("""(?i)mul""".r ~> whiteSpace ~> registersName) ~ (delimiter ~> number)).map {
        case target ~ value =>
          Mul(target, value)
      }

    val mov: Parser[Mov[RegisterName, RegisterName]] =
      (("""(?i)mov""".r ~> whiteSpace ~> registersName) ~ (delimiter ~> registersName)).map {
        case src ~ dest =>
          Mov(src, dest)
      }

    val set: Parser[Set[RegisterName]] =
      (("""(?i)set""".r ~> whiteSpace ~> registersName) ~ (delimiter ~> number)).map {
        case target ~ value =>
          Set(target, value)
      }

    val jmp: Parser[Jmp] =
      ("""(?i)jmp""".r ~> whiteSpace ~> name).map { label =>
        Jmp(label)
      }

    val jz: Parser[Jz] =
      ("""(?i)jz""".r ~> whiteSpace ~> name).map { label =>
        Jz(label)
      }

    val label: Parser[Label] =
      ("""(?i)Label:""".r ~> whiteSpace ~> name).map { label =>
        Label(label)
      }

    val commands: Parser[List[Asm]] =
      (whiteSpace.? ~> (cmp | add | mul | mov | set | jmp | jz | label) <~ whiteSpace.?).+
  }
}