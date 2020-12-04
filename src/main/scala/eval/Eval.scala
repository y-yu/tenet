package eval

import asm.Asm
import asm.Asm._
import eval.Eval.MachineState
import monad.State
import monad.State._
import register.Registers

class Eval {
  def run(
    initRegisters: Registers,
    commands: List[Asm]
  ): MachineState = {
    // fucking non tail recursive!!!!
    def loop(cs: List[Asm]): State[MachineState, Unit] = cs match {
      case Nil =>
        unit
      case x :: xs =>
        val evalHead = x match {
          case cmp @ Cmp(rhs) =>
            for {
              state <- get[MachineState]
              lhs = cmp.iso.get(state.registers)
              _ <-
                set(
                  state.copy(
                    registers = state.registers.copy(
                      flags = state.registers.flags.copy(zero = lhs == rhs)
                    )
                  )
                )
              _ <- loop(xs)
            } yield ()

          case add @ Add(rhs) =>
            for {
              state <- get[MachineState]
              lhs = add.iso.get(state.registers)
              _ <-
                set(
                  state.copy(
                    registers = add.iso.set(
                      value = lhs + rhs,
                      registers = state.registers
                    )
                  )
                )
              _ <- loop(xs)
            } yield ()

          case mul @ Mul(rhs) =>
            for {
              state <- get[MachineState]
              lhs = mul.iso.get(state.registers)
              _ <-
                set(
                  state.copy(
                    registers = mul.iso.set(
                      value = rhs * lhs,
                      registers = state.registers
                    )
                  )
                )
              _ <- loop(xs)
            } yield ()

          case mov @ Mov() =>
            for {
              state <- get[MachineState]
              src = mov.isoSrc.get(state.registers)
              _ <-
                set(
                  state.copy(
                    registers = mov.isoDest.set(
                      value = src,
                      registers = state.registers
                    )
                  )
                )
              _ <- loop(xs)
            } yield ()

          case Jmp(label) =>
            val nextPc = commands.zipWithIndex.find {
              case (Label(name), _) =>
                name == label
              case _ =>
                false
            }.map(_._2).get

            loop(commands.drop(nextPc))

          case Jz(label) =>
            val nextPc = commands.zipWithIndex.find {
              case (Label(name), _) =>
                name == label
              case _ =>
                false
            }.map(_._2).get

            for {
              state <- get[MachineState]
              _ <- if (state.registers.flags.zero) {
                loop(commands.drop(nextPc))
              } else {
                loop(xs)
              }
            } yield ()

          case Label(_) =>
            loop(xs)
        }
        for {
          state <- get[MachineState]
          _ <- x match {
            case Jmp(_) | Label(_) | Jz(_) =>
              unit[MachineState]
            case _ =>
              set[MachineState](
                state.copy(
                  commandLogs = x :: state.commandLogs
                )
              )
          }
          _ <- evalHead
        } yield ()
    }

    loop(commands).runState(
      MachineState(
        registers = initRegisters,
        commandLogs = Nil
      )
    )._1
  }
}

object Eval {

  /**
    * @param commandLogs commands executed
    * @param registers register
    */
  case class MachineState(
    commandLogs: List[Asm],
    registers: Registers,
  )
}
