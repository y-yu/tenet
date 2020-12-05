package tenet.simulator.asm

import tenet.simulator.register.RegisterName

sealed trait Asm extends Product with Serializable

object Asm {
  case class Cmp[A <: RegisterName](
    target: A,
    value: Int,
  ) extends Asm

  case class Add[A <: RegisterName](
    target: A,
    value: Int
  ) extends Asm

  case class Mul[A <: RegisterName](
    target: A,
    value: Int
  ) extends Asm

  case class Mov[Src <: RegisterName, Dest <: RegisterName](
    src: Src,
    dest: Dest
  ) extends Asm

  case class Set[A <: RegisterName](
    target: A,
    value: Int
  ) extends Asm

  case class Jmp(
    label: String
  ) extends Asm

  // jump if zero-flag is set
  case class Jz(
    label: String
  ) extends Asm

  case class Label(
    name: String
  ) extends Asm
}