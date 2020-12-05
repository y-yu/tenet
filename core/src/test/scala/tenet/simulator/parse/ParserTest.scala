package tenet.simulator.parse

import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec
import tenet.simulator.asm.Asm._
import tenet.simulator.register.RegisterName._

class ParserTest extends AnyFlatSpec with Diagrams {
  trait SetUp extends ParserSyntax.Commands {
    val registers = List("eax", "Ebx", "ecX")
  }

  "parse" should "parse `Cmx` opcode" in new SetUp {
    val string = "Cmp eax, 10"

    val actual = parse(cmp, string)

    assert(actual.successful)
    assert(actual.get === Cmp(Eax(), 10))
  }

  it should "parse `Add` opcode" in new SetUp {
    val string = "aDd ecX, 155"
    val actual = parse(add, string)

    assert(actual.successful)
    assert(actual.get === Add(Ecx(), 155))
  }

  it should "parse `Mul` opcode" in new SetUp {
    val string = "mUL         EbX,-5"
    val actual = parse(mul, string)

    assert(actual.successful)
    assert(actual.get === Mul(Ebx(), -5))
  }

  it should "parse `Mov` opcode" in new SetUp {
    val string = "moV EAX,         ecx"
    val actual = parse(mov, string)

    assert(actual.successful)
    assert(actual.get === Mov(Eax(), Ecx()))
  }

  it should "parse `Set` opcode" in new SetUp {
    val string = "SET ecx,1"
    val actual = parse(set, string)

    assert(actual.successful)
    assert(actual.get === Set(Ecx(), 1))
  }

  it should "parse `Jmp` opcode" in new SetUp {
    val string = "jMp Aeee_ooee"
    val actual = parse(jmp, string)

    assert(actual.successful)
    assert(actual.get === Jmp("Aeee_ooee"))
  }

  it should "parse `Jz` opcode" in new SetUp {
    val string = "JZ Aeee_oOO134ee"
    val actual = parse(jz, string)

    assert(actual.successful)
    assert(actual.get === Jz("Aeee_oOO134ee"))
  }

  it should "parse `Label` opcode" in new SetUp {
    val string = "LAbEl: 11112"
    val actual = parse(label, string)

    assert(actual.successful)
    assert(actual.get === Label("11112"))
  }

  it should "parse whole codes" in new SetUp {
    val string =
      """mov eax, ebx
        |mov ebx, eax
        |mul ebx, -1
        |mov eax, ebx
        |
        |label: Decrement
        |cmp eax, 0
        |jz End
        |add eax, -1
        |jmp Decrement
        |
        |label: End
        |""".stripMargin

    val expected = List(
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

    val actual = parse(commands, string)

    assert(actual.successful)
    assert(actual.get === expected)
  }
}
