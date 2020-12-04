package register.implicits

import register.RegisterName._
import register.Registers

trait IsoValueRegister[A] {
  def get(registers: Registers): Int

  def set(value: Int, registers: Registers): Registers
}

object IsoValueRegister {
  implicit val eaxValueFromRegister: IsoValueRegister[Eax] =
    new IsoValueRegister[Eax] {
      def get(registers: Registers): Int =
        registers.eax

      override def set(value: Int, registers: Registers): Registers =
        registers.copy(eax = value)
    }

  implicit val ebxValueFromRegister: IsoValueRegister[Ebx] =
    new IsoValueRegister[Ebx] {
      def get(registers: Registers): Int =
        registers.ebx

      override def set(value: Int, registers: Registers): Registers =
        registers.copy(ebx = value)
    }

  implicit val ecxValueFromRegister: IsoValueRegister[Ecx] =
    new IsoValueRegister[Ecx] {
      def get(registers: Registers): Int =
        registers.ecx

      override def set(value: Int, registers: Registers): Registers =
        registers.copy(ecx = value)
    }
}