package ast;

import cfg.*;
import globals.Flags;
import globals.Tables;
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

      expRes = handleNull(expRes, currentBlock, exitBlock);

      if (Flags.isRegisterBased()) {
         SSA.writeVariable("%_retval_", currentBlock, expRes);
      }
      else {
         Llvm store = new Store(expRes.getLlvmType(), expRes.getValue(), 
            expRes.getLlvmType() + "*", "%_retval_");
         currentBlock.addInstruction(store);
      }

      currentBlock.addChild(exitBlock);
      exitBlock.addPred(currentBlock);
      Llvm branch = new Bru(exitLabel);

      currentBlock.addInstruction(branch);
      currentBlock.setTerminal(true);

      return currentBlock;
   }

   private Value handleNull(Value v, CFGNode currentBlock, CFGNode exitBlock) {
      if (!v.getLlvmType().equals("null")) {
         return v;
      }

      // need to figure out the return type. This wont work anymore
      Llvm instr = exitBlock.getInstructions().get(0);

      if (instr instanceof Ret) {
         Ret ret = (Ret) instr;
         String retType = ret.getType().substring(0, ret.getType().length() - 1);
         return new Immediate(retType, "null", currentBlock);
      }

      if(instr instanceof Load) {
         Load load = (Load) instr;
         String retType = load.getType().substring(0, load.getType().length()-1);
         return new Immediate(retType, "null", currentBlock);
      }

      return v;
   }
}
