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

  
   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBlock) {
      Value expRes = this.expression.generateInstructions(currentBlock);

      return currentBlock;
   }
   
}
