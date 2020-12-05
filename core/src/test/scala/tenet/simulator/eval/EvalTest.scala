package tenet.simulator.eval

import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec
import tenet.simulator.asm.Asm.Add
import tenet.simulator.asm.Asm.Cmp
import tenet.simulator.asm.Asm.Jmp
import tenet.simulator.asm.Asm.Jz
import tenet.simulator.asm.Asm.Label
import tenet.simulator.asm.Asm.Mov
import tenet.simulator.asm.Asm.Mul
import tenet.simulator.register.Flags
import tenet.simulator.register.RegisterName.Eax
import tenet.simulator.register.RegisterName.Ebx
import tenet.simulator.register.Registers

class EvalTest extends AnyFlatSpec with Diagrams {
  trait SetUp {
    val sut = new Eval()
  }

  "run" should "eval commands" in new SetUp {
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

    val expected = MachineState(
      commandLogs = List(
        Cmp(target = Eax(), value = 0),
        Add(target = Eax(), value = -1),
        Cmp(target = Eax(), value = 0),
        Add(target = Eax(), value = -1),
        Cmp(target = Eax(), value = 0),
        Add(target = Eax(), value = -1),
        Cmp(target = Eax(), value = 0),
        Add(target = Eax(), value = -1),
        Cmp(target = Eax(), value = 0),
        Add(target = Eax(), value = -1),
        Cmp(target = Eax(), value = 0),
        Mov(src = Eax(), dest = Ebx()),
        Mul(target = Ebx(), value = -1),
        Mov(src = Ebx(), dest = Eax()),
        Mov(src = Eax(), dest = Ebx())
      ),
      registers = Registers(
        eax = 0, ebx = 5, ecx = 0, flags = Flags(zero = true, carry = false)
      )
    )

    val actual = sut.run(
      Registers.initRegister.copy(eax = 5),
      commands
    )

    assert(actual === expected)
  }
}
