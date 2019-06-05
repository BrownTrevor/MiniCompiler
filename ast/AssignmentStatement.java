package ast;

import cfg.*;
import llvm.*;
import globals.*;

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

   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBlock) {
      if (this.target instanceof LvalueId && Flags.isRegisterBased()) {
         return handleLvalueId((LvalueId) target, currentBlock, exitBlock);
      }
      else if (this.target instanceof LvalueId) {
         return handleLvalueId((LvalueId) target, currentBlock, exitBlock);
      }

      Value rVal = this.source.generateInstructions(currentBlock);
      Value lVal = this.target.generateInstructions(currentBlock);

      rVal = handleNull(rVal, lVal.getLlvmType(), currentBlock);

      Llvm instruction = new llvm.Store(rVal.getLlvmType(), rVal.getValue(),
          lVal.getLlvmType(), lVal.getValue());

      currentBlock.addInstruction(instruction);
      return currentBlock;
   }

   public CFGNode handleLvalueId(LvalueId target, CFGNode currentBlock, CFGNode exitBlock) {
      Value rVal = this.source.generateInstructions(currentBlock);
      String targetName = target.getId();
      Symbol sym = Tables.getFromSymbolTable(targetName);

      String leftType = sym.getType().llvmType() + "*";
      String sc = specialChar(targetName);

      rVal = handleNull(rVal, leftType, currentBlock);

      Llvm idInstr = new llvm.Store(rVal.getLlvmType(), rVal.getValue(), leftType, sc + targetName);

      currentBlock.addInstruction(idInstr);
      return currentBlock;
   }

   public CFGNode handleRegisterBasedLvalueID(LvalueId target, 
      CFGNode currentBlock, CFGNode exitBlock) {
      
      Value rVal = this.source.generateInstructions(currentBlock);
      String targetName = target.getId();
      Symbol sym = Tables.getFromSymbolTable(targetName);
      
      SSA.writeVariable(target.getId(), currentBlock, rVal);

      return currentBlock;
   }

   private Value handleNull(Value v, String leftType, CFGNode current) {
      if (!v.getLlvmType().equals("null")) {
         return v;
      }

      leftType = leftType.substring(0, leftType.length()-1);

      return new Immediate(leftType, "null", current);
   }

   private String specialChar(String name) {
      // look up the index of the table,
      // if index = 0 then return @
      if (Tables.isGlobal(name)) {
         return "@";
      }
      return "%";
   }
}
