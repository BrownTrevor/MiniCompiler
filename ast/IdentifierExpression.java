package ast;

import cfg.*;
import llvm.*;

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
      Symbol sym = Tables.getFromSymbolTable(id);
      String type = sym.getType().llvmType() + "*";
      Value reg = new Register(sym.getType().llvmType());

      Llvm load = new Load(reg.getValue(), type, specialChar() + sym.getName());
      currentBlock.addInstruction(load);
      
      return reg;
   }

   private String specialChar() {
      // look up the index of the table,
      // if index = 0 then return @
      if (Tables.isGloblal(id)) {
         return "@";
      }
      return "%";
   }
}
