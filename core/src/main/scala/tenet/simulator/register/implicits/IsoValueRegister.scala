package tenet.simulator.register.implicits

import tenet.simulator.register.RegisterName
import tenet.simulator.register.RegisterName._
import tenet.simulator.register.Registers

trait IsoValueRegister[A] {
  def get(registers: Registers): Int

  def set(value: Int, registers: Registers): Registers

  // It's not good...but I cannot find the way out but this.
  def widen[B >: A]: IsoValueRegister[B] =
    this.asInstanceOf[IsoValueRegister[B]]
}

object IsoValueRegister {
  def apply[A <: RegisterName](a: A): IsoValueRegister[RegisterName] = a match {
    case Eax()  =>
      implicitly[IsoValueRegister[Eax]].widen

    case Ebx()  =>
      implicitly[IsoValueRegister[Ebx]].widen

    case Ecx()  =>
      implicitly[IsoValueRegister[Ecx]].widen
  }

  def apply[A: IsoValueRegister](a: A): IsoValueRegister[A] =
    implicitly[IsoValueRegister[A]]

  implicit val eaxInstance: IsoValueRegister[Eax] =
    new IsoValueRegister[Eax] {
      def get(registers: Registers): Int =
        registers.eax

      def set(value: Int, registers: Registers): Registers =
        registers.copy(eax = value)
    }

  implicit val ebxInstance: IsoValueRegister[Ebx] =
    new IsoValueRegister[Ebx] {
      def get(registers: Registers): Int =
        registers.ebx

      override def set(value: Int, registers: Registers): Registers =
        registers.copy(ebx = value)
    }

  implicit val ecxInstance: IsoValueRegister[Ecx] =
    new IsoValueRegister[Ecx] {
      def get(registers: Registers): Int =
        registers.ecx

      override def set(value: Int, registers: Registers): Registers =
        registers.copy(ecx = value)
    }
}