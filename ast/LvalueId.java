package ast;

import cfg.*;
import llvm.*;

public class LvalueId
   implements Lvalue
{
   private final int lineNum;
   private final String id;

   public LvalueId(int lineNum, String id)
   {
      this.lineNum = lineNum;
      this.id = id;
   }

   public String getId() {
      return this.id;
   }



   public Value generateInstructions(CFGNode currentBlock) {
      // type must be retrieved by the this.id
      Symbol sym = Tables.getFromSymbolTable(this.id);
      String type =  sym.getType().llvmType() + "*";
      Value register = new Register(type);
      Llvm instruction = new Load(register.getValue(), type, this.id);

      currentBlock.addInstruction(instruction);

      return (register);
   }

}
