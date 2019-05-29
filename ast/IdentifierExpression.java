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
      String type = sym.getType().llvmType();
      Value reg = new Register(type);

      Llvm load = new Load(reg.getValue(), type, sym.getName());
      currentBlock.addInstruction(load);
      
      return reg;
   }
}
