package ast;

import cfg.*;
import llvm.*;

public class ReturnStatement
   extends AbstractStatement
{
   private final Expression expression;

   public ReturnStatement(int lineNum, Expression expression)
   {
      super(lineNum);
      this.expression = expression;
   }

   public Expression getExpression() {
      return this.expression;
   }

   @Override
   public String toString() {
      return "Return statement";
   }

   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBlock) {
      Value expRes = expression.generateInstructions(currentBlock);
      String exitLabel = exitBlock.getLabel().getId();

      Llvm store = new Store(expRes.getLlvmType(), expRes.getValue(), 
            expRes.getLlvmType() + "*", "%_retval_");
      Llvm branch = new Bru(exitLabel);

      currentBlock.addInstruction(store);
      currentBlock.addInstruction(branch);

      return currentBlock;
   }
}
