package ast;

import cfg.*;
import llvm.*;

public class PrintStatement
   extends AbstractStatement
{
   private final Expression expression;

   public PrintStatement(int lineNum, Expression expression)
   {
      super(lineNum);
      this.expression = expression;
   }

   public Expression getExpression() {
      return this.expression;
   }

   @Override
   public String toString() {
      return "Print statement";
   }


   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBlock) {
      Value expRes = expression.generateInstructions(currentBlock);

      String instruction = "call i32 (i8*, ...)* @printf(i8* getelementptr " 
         + "inbounds ([5 x i8]* @.println, i32 0, i32 0), i32 " 
         + expRes.getValue() + ")";

      Llvm print = new Generic(instruction);
      currentBlock.addInstruction(print);  

      return currentBlock;
   }
}
