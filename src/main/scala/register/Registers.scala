package register

case class Registers(
  eax: Int,
  ebx: Int,
  ecx: Int,
  flags: Flags
)

object Registers {
  val initRegister: Registers = Registers(
    eax = 0,
    ebx = 0,
    ecx = 0,
    flags = Flags(
      zero = false,
      carry = false
    )
  )
}