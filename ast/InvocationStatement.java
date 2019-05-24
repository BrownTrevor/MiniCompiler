package ast;

import java.util.*;
import cfg.*;
import llvm.*;

public class InvocationStatement
   extends AbstractStatement
{
   private final Expression expression;

   public InvocationStatement(int lineNum, Expression expression)
   {
      super(lineNum);
      this.expression = expression;
   }

   public Expression getExpression() {
      return this.expression;
   }

   @Override
   public String toString() {
      return "Invocation statement";
   }

   /*
   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBlock) {
      Value expRes = expression.generateInstructions(currentBlock);
// %u110 = call i32 @ackermann(i32 %u107, i32 %u109)

      currentBlock.addInstruction("print x");

      return currentBlock;
   }
   */
}
