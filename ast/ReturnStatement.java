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

      expRes = handleNull(expRes, exitBlock);

      Llvm store = new Store(expRes.getLlvmType(), expRes.getValue(), 
            expRes.getLlvmType() + "*", "%_retval_");
      Llvm branch = new Bru(exitLabel);

      currentBlock.addInstruction(store);
      currentBlock.addInstruction(branch);

      return currentBlock;
   }

   private Value handleNull(Value v, CFGNode exitBlock) {
      if (!v.getLlvmType().equals("null")) {
         return v;
      }

      Llvm instr = exitBlock.getInstructions().get(0);

      if(instr instanceof Load) {
         Load load = (Load) instr;
         String retType = load.getType().substring(0, load.getType().length()-1);
         return new Immediate(retType, "null");
      }

      return v;
   }
}
