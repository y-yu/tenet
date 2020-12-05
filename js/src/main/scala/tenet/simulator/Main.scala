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
  def parse(
    source: String
  ): String = try {
    parser.parse(source).mkString("\n")
  } catch {
    case NonFatal(e) =>
      e.getMessage
  }

  @JSExport
  def eval(
    source: String
  ): js.Tuple3[String, String, Boolean] = try {
    val asm = parser.parse(source)
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
      forwardResult.registers.pp,
      backwardResult.registers.pp,
      forwardResult.registers.eax == 0 &&
        backwardResult.registers.eax == randomNumber
    )
  } catch {
    case NonFatal(e) =>
      (e.getMessage,
        e.getMessage,
        false)
  }
}