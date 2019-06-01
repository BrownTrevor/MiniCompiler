package ast;

import cfg.*;
import llvm.*;

public class WhileStatement
   extends AbstractStatement
{
   private final Expression guard;
   private final Statement body;

   public WhileStatement(int lineNum, Expression guard, Statement body)
   {
      super(lineNum);
      this.guard = guard;
      this.body = body;
   }

   public Expression getGuard() {
      return this.guard;
   }

   public Statement getBody() {
      return this.body;
   }

   @Override
   public String toString() {
      return "While statement";
   }

   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBlock) {

      CFGNode conditionalBlock = new CFGNode();
      CFGNode bodyBlock = new CFGNode();
      CFGNode joinBlock = new CFGNode();

      Value guardVal = guard.generateInstructions(conditionalBlock);
      
      Llvm enterLoop = new Bru(conditionalBlock.getLabel().getId());
      Llvm branch = new Br(guardVal.getValue(), bodyBlock.getLabel().getId(), 
         joinBlock.getLabel().getId());

      CFGNode bodyBlockRes = this.body.generateCFG(bodyBlock, exitBlock);
      Llvm loop = new Bru(conditionalBlock.getLabel().getId());

      currentBlock.addInstruction(enterLoop);
      conditionalBlock.addInstruction(branch);
      bodyBlockRes.addInstruction(loop);

      currentBlock.addChild(conditionalBlock);
      conditionalBlock.addChild(bodyBlock);
      conditionalBlock.addChild(joinBlock);

      return joinBlock;
   }
}
