package ast;

import cfg.*;
import llvm.*;

public class AssignmentStatement extends AbstractStatement {
   private final Lvalue target;
   private final Expression source;

   public AssignmentStatement(int lineNum, Lvalue target, Expression source) {
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
      if (this.target instanceof LvalueId) {
         Value rVal = this.source.generateInstructions(currentBlock);
         String targetName = ((LvalueId) target).getId();
         Symbol sym = Tables.getFromSymbolTable(targetName);

         String leftType = sym.getType().llvmType() + "*";
         String sc = specialChar(targetName);

         rVal = handleNull(rVal, leftType);

         Llvm idInstr = new llvm.Store(rVal.getLlvmType(), rVal.getValue(),
            leftType, sc + targetName);

         currentBlock.addInstruction(idInstr);
         return currentBlock;
      }

      Value rVal = this.source.generateInstructions(currentBlock);
      Value lVal = this.target.generateInstructions(currentBlock);

      rVal = handleNull(rVal, lVal.getLlvmType());

      Llvm instruction = new llvm.Store(rVal.getLlvmType(), rVal.getValue(),
          lVal.getLlvmType(), lVal.getValue());

      currentBlock.addInstruction(instruction);
      return currentBlock;
   }

   private Value handleNull(Value v, String leftType) {
      if (!v.getLlvmType().equals("null")) {
         return v;
      }

      leftType = leftType.substring(0, leftType.length()-1);

      return new Immediate(leftType, "null");
   }

   private String specialChar(String name) {
      // look up the index of the table,
      // if index = 0 then return @
      if (Tables.isGloblal(name)) {
         return "@";
      }
      return "%";
   }
}
