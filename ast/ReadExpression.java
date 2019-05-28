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

      String instruction = " call i32 (i8*, ...)* @scanf(i8* getelementptr inbounds ([4 x i8]* @.read, i32 0, i32 0), i32* %a)";

      Llvm print = new Generic(instruction);
      currentBlock.addInstruction(print);

      return null;
   }
}
