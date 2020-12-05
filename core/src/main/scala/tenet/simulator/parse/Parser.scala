package tenet.simulator.parse

import tenet.simulator.asm.Asm

class Parser {
  private val parser = new ParserSyntax.Commands

  // TODO: Error handling
  def parse(
    string: String
  ): List[Asm] =
    parser.parse(parser.commands, string).get
}
