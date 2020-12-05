package tenet.simulator.eval

import tenet.simulator.asm.Asm
import tenet.simulator.register.Registers

/**
  * @param commandLogs commands executed (reverse order)
  * @param registers register
  */
case class MachineState(
  commandLogs: List[Asm],
  registers: Registers
) {
  def pp: String = {
    pprint.apply(this).plainText
  }
}