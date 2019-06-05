package ast;

import cfg.*;
import llvm.*;

public class ReadExpression
   extends AbstractExpression
{
   public ReadExpression(int lineNum)
   {
      super(lineNum);
   }


   public Value generateInstructions(CFGNode currentBlock) {

      Value reg = new Register("i32", currentBlock);
      String instruction = reg.getValue() +  " = call i32 @read()";

      Llvm print = new Generic(instruction);
      currentBlock.addInstruction(print);

      return reg;
   }
}
