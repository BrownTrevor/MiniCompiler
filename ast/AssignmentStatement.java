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
      if(this.target instanceof LvalueId) {
         Value right = this.source.generateInstructions(currentBlock);
         String targetName = ((LvalueId)target).getId();
         Symbol sym = Tables.getFromSymbolTable(targetName);
         String targetType = sym.getType().llvmType() + "*";

         Llvm idInstr = new llvm.Store(right.getLlvmType(), right.getValue(), 
         targetType, "%" + targetName);
         
         currentBlock.addInstruction(idInstr);
         return currentBlock;
      }
      
      
      Value rVal = this.source.generateInstructions(currentBlock);
      Value target = this.target.generateInstructions(currentBlock);
      
      Llvm instruction = new llvm.Store(rVal.getLlvmType(), rVal.getValue(),
         target.getLlvmType(), target.getValue());

      currentBlock.addInstruction(instruction);
      return currentBlock;
   }
}
