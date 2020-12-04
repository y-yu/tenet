import asm.Asm._
import eval.Eval
import register.RegisterName._
import register.Registers

object Main {
  def main(args: Array[String]): Unit = {
    val eval = new Eval

    val commands = List(
      Mov[Eax, Ebx],
      Mov[Ebx, Eax],
      Mul[Ebx](-1),
      Mov[Eax, Ebx],
      Label("Decrement"),
      Cmp[Eax](0),
      Jz("End"),
      Add[Eax](-1),
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
