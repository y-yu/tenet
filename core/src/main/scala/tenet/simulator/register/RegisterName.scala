package tenet.simulator.register

sealed trait RegisterName

object RegisterName {
  case class Eax() extends RegisterName
  case class Ebx() extends RegisterName
  case class Ecx() extends RegisterName
}
