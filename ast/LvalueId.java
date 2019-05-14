package ast;

import cfg.*;

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

   public Value generateInstructions(CFGNode currentBlock, String structId) {
      String prevRegister = Register.currentRegister();
      String instruction = Register.newRegister() +  " = load ";
      String type = "";
      // <result> = load  <ty>* <pointer>

      // is this id the end of a dot chain? if not just look in the symbol table
      if (structId == null) {
         Symbol sym = Tables.getFromSymbolTable(this.id); 
         type = sym.getType().llvmType();
         instruction += (type +"* " + this.id);
      }
      else {
         Struct struct = Tables.getFromStructTable(structId);
         type = struct.getField(this.id).llvmType();
         instruction += (type + "* " + prevRegister);
      }

      currentBlock.addInstruction(instruction);

      return (new Value(type, Register.currentRegister()));
   }

}
