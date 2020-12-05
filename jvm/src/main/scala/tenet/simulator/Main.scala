package tenet.simulator

import tenet.simulator.asm.Asm.Add
import tenet.simulator.asm.Asm.Cmp
import tenet.simulator.asm.Asm.Jmp
import tenet.simulator.asm.Asm.Jz
import tenet.simulator.asm.Asm.Label
import tenet.simulator.asm.Asm.Mov
import tenet.simulator.asm.Asm.Mul
import tenet.simulator.eval.Eval
import tenet.simulator.register.RegisterName.Eax
import tenet.simulator.register.RegisterName.Ebx
import tenet.simulator.register.Registers

object Main {
  def main(args: Array[String]): Unit = {
    val eval = new Eval

    val commands = List(
      Mov(Eax(), Ebx()),
      Mov(Ebx(), Eax()),
      Mul(Ebx(), -1),
      Mov(Eax(), Ebx()),
      Label("Decrement"),
      Cmp(Eax(), 0),
      Jz("End"),
      Add(Eax(), -1),
      Jmp("Decrement"),
      Label("End")
    )

    // We want that it turns to 0
    val secret: Int = 5

    println("======= Secret value is in `EAX` =======")
    pprint.pprintln(Registers.initRegister.copy(eax = secret))

    println("======= Codes to be executed =====")
    pprint.pprintln(commands)

    println("======= Forward execution =======")
    val forwardResult = eval.run(
      Registers.initRegister.copy(eax = secret),
      commands
    )
    pprint.pprintln(
      forwardResult
    )
    println("======= Backward execution =======")
    val backwardResult = eval.run(
      Registers.initRegister,
      forwardResult.commandLogs,
    )
    pprint.pprintln(
      backwardResult
    )
    println("=================================\n")

  }
}
