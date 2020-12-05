package tenet.simulator

import tenet.simulator.eval.Eval
import tenet.simulator.parse.Parser
import tenet.simulator.register.Registers
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.util.Random
import scala.util.control.NonFatal

@JSExportTopLevel("TenetSimulator")
object Main {
  private val parser = new Parser()
  private val eval = new Eval()
  private val random = new Random()

  @JSExport
  def execute(
    source: String
  ): js.Tuple5[String, String, String, String, Boolean] = try {
    val asm = parser.parse(source).get
    val randomNumber = random.nextInt(100).abs

    val forwardResult = eval.run(
      Registers.initRegister.copy(
        eax = randomNumber
      ),
      asm
    )
    val backwardResult = eval.run(
      Registers.initRegister,
      forwardResult.commandLogs
    )

    (
      asm.mkString("\n"),
      forwardResult.registers.pp,
      backwardResult.registers.pp,
      forwardResult.commandLogs.mkString("\n"),
      forwardResult.registers.eax == 0 &&
        backwardResult.registers.eax == randomNumber
    )
  } catch {
    case NonFatal(e) =>
      // TODO: Gently error reporting
      (
        e.getMessage,
        e.getMessage,
        e.getMessage,
        e.getMessage,
        false
      )
  }
}