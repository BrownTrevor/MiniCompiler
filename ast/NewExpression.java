package ast;

import cfg.*;
import llvm.*;
import globals.*;

public class NewExpression
   extends AbstractExpression
{
   private final String id;

   public NewExpression(int lineNum, String id)
   {
      super(lineNum);
      this.id = id;
   }

   public String getId() {
      return this.id;
   }

   public Value generateInstructions(CFGNode currentBlock) {
      /**
       * %u13 = call i8* @malloc(i32 4) 
       * %u14 = bitcast i8* %u13 to %struct.simple*
       */

      Struct struct = Tables.getFromStructTable(id);
      String size = (struct.getStructSize()*4) + "";
      String structType = struct.llvmType();

      Value mallocReg = new Register("i8*", currentBlock);
      Value bitcastReg = new Register(structType, currentBlock);

      Llvm malloc = new Generic(mallocReg.getValue() + " = call i8* @malloc(i32 " + size + ") ");
      Llvm bitcast = new BitCast(bitcastReg.getValue(), "i8*", 
         mallocReg.getValue(), structType);

      currentBlock.addInstruction(malloc);
      currentBlock.addInstruction(bitcast);

      return bitcastReg;
   }
}
