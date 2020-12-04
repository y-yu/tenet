package asm

import register.RegisterName
import register.implicits.IsoValueRegister

sealed trait Asm

object Asm {
  case class Cmp[A <: RegisterName: IsoValueRegister](
    value: Int,
  ) extends Asm {
    def iso: IsoValueRegister[A] = implicitly[IsoValueRegister[A]]
  }

  case class Add[A <: RegisterName: IsoValueRegister](
    value: Int
  ) extends Asm {
    def iso: IsoValueRegister[A] = implicitly[IsoValueRegister[A]]
  }

  case class Mul[A <: RegisterName: IsoValueRegister](
    value: Int
  ) extends Asm {
    def iso: IsoValueRegister[A] = implicitly[IsoValueRegister[A]]
  }

  case class Mov[
    Src <: RegisterName: IsoValueRegister,
    Dest <: RegisterName: IsoValueRegister
  ]() extends Asm {
    def isoSrc: IsoValueRegister[Src] = implicitly[IsoValueRegister[Src]]

    def isoDest: IsoValueRegister[Dest] = implicitly[IsoValueRegister[Dest]]
  }

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