package tenet.simulator.parse

case class ParseError(
  message: String = null,
  cause: Throwable = null
) extends Throwable(message, cause)
