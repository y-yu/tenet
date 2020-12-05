package tenet.simulator.eval

import tenet.simulator.asm.Asm
import tenet.simulator.asm.Asm._
import tenet.simulator.register.Registers
import tenet.simulator.register.implicits.IsoValueRegister
import scala.annotation.tailrec

class Eval {
  def run(
    initRegisters: Registers,
    commands: List[Asm]
  ): MachineState = {
    @tailrec
    def loop(acc: MachineState, cs: List[Asm]): MachineState = cs match {
      case Nil =>
        acc
      case cmd :: xs =>
        cmd match {
          case Cmp(target, rhs) =>
            val lhs = IsoValueRegister(target).get(acc.registers)
            loop(
              acc.copy(
                commandLogs = cmd :: acc.commandLogs,
                registers = acc.registers.copy(
                  flags = acc.registers.flags.copy(zero = lhs == rhs)
                )
              ),
              xs
            )

          case Add(target, rhs) =>
            val iso = IsoValueRegister(target)
            val lhs = iso.get(acc.registers)
            loop(
              acc.copy(
                commandLogs = cmd :: acc.commandLogs,
                registers = iso.set(
                  value = lhs + rhs,
                  registers = acc.registers
                )
              ),
              xs
            )

          case Mul(target, rhs) =>
            val iso = IsoValueRegister(target)
            val lhs = iso.get(acc.registers)
            loop(
              acc.copy(
                commandLogs = cmd :: acc.commandLogs,
                registers = iso.set(
                  value = lhs * rhs,
                  registers = acc.registers
                )
              ),
              xs
            )

          case Mov(src, dest) =>
            val lhs = IsoValueRegister(src).get(acc.registers)
            loop(
              acc.copy(
                commandLogs = cmd :: acc.commandLogs,
                registers = IsoValueRegister(dest).set(
                  value = lhs,
                  registers = acc.registers
                )
              ),
              xs
            )

          case Set(target, value) =>
            loop(
              acc.copy(
                commandLogs = cmd :: acc.commandLogs,
                registers = IsoValueRegister(target).set(
                  value = value,
                  registers = acc.registers
                )
              ),
              xs
            )

          case Jmp(label) =>
            val nextPc = commands.zipWithIndex.find {
              case (Label(name), _) =>
                name == label
              case _ =>
                false
            }.map(_._2).get

            loop(acc, commands.drop(nextPc))

          case Jz(label) =>
            val nextPc = commands.zipWithIndex.find {
              case (Label(name), _) =>
                name == label
              case _ =>
                false
            }.map(_._2).get

            loop(
              acc,
              if (acc.registers.flags.zero) {
                commands.drop(nextPc)
              } else {
                xs
              }
            )

          case Label(_) =>
            loop(acc, xs)
        }
    }

    loop(
      MachineState(
        registers = initRegisters,
        commandLogs = Nil
      ),
      commands
    )
  }
}
