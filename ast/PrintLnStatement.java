package ast;

import cfg.*;
import llvm.*;

public class PrintLnStatement
   extends AbstractStatement
{
   private final Expression expression;

   public PrintLnStatement(int lineNum, Expression expression)
   {
      super(lineNum);
      this.expression = expression;
   }

   public Expression getExpression() {
      return this.expression;
   }

   @Override
   public String toString() {
      return "PrintLn statement";
   }


   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBlock) {
      Value expRes = expression.generateInstructions(currentBlock);

      String instruction = "call i32 @println( " + expRes.getLlvmType()
         + " " + expRes.getValue() + " )";

      Llvm print = new Generic(instruction);
      currentBlock.addInstruction(print);

      return currentBlock;
   }
}
