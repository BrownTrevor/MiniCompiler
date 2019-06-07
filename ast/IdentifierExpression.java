package ast;

import cfg.*;
import llvm.*;
import globals.*;

public class IdentifierExpression
   extends AbstractExpression
{
   private final String id;

   public IdentifierExpression(int lineNum, String id)
   {
      super(lineNum);
      this.id = id;
   }

   public String getId() {
      return this.id;
   }

   public Value generateInstructions(CFGNode currentBlock) {
      // if this reg flag is set and this is not a global var, defer 

     // System.err.println(id + ": " + Flags.isRegisterBased() + " " +
     //    !Tables.isGlobal(this.id));
      if(Flags.isRegisterBased() && !Tables.isGlobal(this.id)) {
         return generateRegBasedInstructions(currentBlock);
      }


      Symbol sym = Tables.getFromSymbolTable(id);
      String type = sym.getType().llvmType() + "*";
      Value reg = new Register(sym.getType().llvmType(), currentBlock);

      Llvm load = new Load(reg.getValue(), type, specialChar() + sym.getName());
      currentBlock.addInstruction(load);
      
      return reg;
   }

   public Value generateRegBasedInstructions(CFGNode current) {
      Value v = SSA.readVariable(this.id, current);
      //System.err.println(v.getValue() + " | " +  v.getLlvmType() + " | " +
      //   v.getBlock().getLabel().getId());
      return v;
   }

   private String specialChar() {
      // look up the index of the table,
      // if index = 0 then return @
      if (Tables.isGlobal(id)) {
         return "@";
      }
      return "%";
   }
}
