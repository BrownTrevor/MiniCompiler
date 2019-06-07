package ast;

import cfg.*;
import llvm.*;

public class ConditionalStatement
   extends AbstractStatement
{
   private final Expression guard;
   private final Statement thenBlock;
   private final Statement elseBlock;

   public ConditionalStatement(int lineNum, Expression guard,
      Statement thenBlock, Statement elseBlock)
   {
      super(lineNum);
      this.guard = guard;
      this.thenBlock = thenBlock;
      this.elseBlock = elseBlock;
   }

   public Expression getGuard() {
      return this.guard;
   }

   public Statement getThenBlock() {
      return this.thenBlock;
   }

   public Statement getElseBlock() {
      return this.elseBlock;
   }

   @Override
   public String toString() {
      return "Conditional statement";
   }
   
   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBlock) {
      // br i1 %u97, label %LU43, label %LU45

      Value guardVal = handleGaurd(currentBlock);

      CFGNode thenBlock = new CFGNode();
      CFGNode elseBlock = new CFGNode();

      Llvm branch = new Br(guardVal.getValue(), thenBlock.getLabel().getId(),
       elseBlock.getLabel().getId());
      currentBlock.addInstruction(branch);

      CFGNode thenResBlock = this.thenBlock.generateCFG(thenBlock, exitBlock);
      CFGNode elseResBlock = this.elseBlock.generateCFG(elseBlock, exitBlock);

      CFGNode joinBlock = new CFGNode();
      Llvm joinInstr = new Bru(joinBlock.getLabel().getId());
      
      // only add if
      if (!thenResBlock.isTerminal()) {
         thenResBlock.addInstruction(joinInstr);
      } 
      
      if (!elseResBlock.isTerminal()) {
         elseResBlock.addInstruction(joinInstr);
      }   

      thenResBlock.addChild(joinBlock);
      elseResBlock.addChild(joinBlock);

      currentBlock.addChild(thenBlock);
      currentBlock.addChild(elseBlock);

      thenBlock.addPred(currentBlock);
      elseBlock.addPred(currentBlock);
      joinBlock.addPred(thenBlock);
      joinBlock.addPred(elseBlock);

      return joinBlock;
   }


   private Value handleGaurd(CFGNode block) {
      Value truncMe = guard.generateInstructions(block);

      Value result = new Register("i1", block);
      Llvm trunc = new Trunc(result.getValue(), truncMe.getLlvmType(), truncMe.getValue(), result.getLlvmType());

      block.addInstruction(trunc);

      return result;
   }
}
