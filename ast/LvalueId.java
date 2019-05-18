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



   public Value generateInstructions(CFGNode currentBlock) {
      String instruction = Register.newRegister() + " = load ";
      // <result> = load <ty>* <pointer> will the type always be i32?

      // type must be retrieved by the this.id
      Symbol sym = Tables.getFromSymbolTable(this.id);
      String targetType =  sym.getType().llvmType();

      instruction += (targetType + "* %" + this.id);

      currentBlock.addInstruction(instruction);

      return (new Value(targetType, Register.currentRegister()));
   }

}
