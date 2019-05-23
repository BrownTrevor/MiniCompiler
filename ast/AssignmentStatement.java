package ast;

import cfg.*;
import llvm.*;


public class AssignmentStatement
   extends AbstractStatement
{
   private final Lvalue target;
   private final Expression source;

   public AssignmentStatement(int lineNum, Lvalue target, Expression source)
   {
      super(lineNum);
      this.target = target;
      this.source = source;
   }

   public Lvalue getTarget() {
      return this.target;
   }

   public Expression getSource() {
      return this.source;
   }

   @Override
   public String toString() {
      return "Assignment statement";
   }

   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBblock) {
      Value rVal = this.source.generateInstructions(currentBlock);
      Value target = this.target.generateInstructions(currentBlock);
      
      Llvm instruction = new llvm.Store(target.getLlvmType(), 
         target.getValue(), rVal.getLlvmType(), rVal.getValue());

      currentBlock.addInstruction(instruction);
      return currentBlock;
   }
}
