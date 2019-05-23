package ast;

import cfg.*;
import llvm.*;

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
      String size = struct.getStructSize() + "";
      String structType = struct.llvmType();

      Value mallocReg = new Register("i8*");
      Value bitcastReg = new Register(structType);

      Llvm malloc = new Generic("call i8* @malloc(i32 4 " + size + ") ");
      Llvm bitcast = new BitCast(bitcastReg.getValue(), "i8*", 
         mallocReg.getValue(), structType);

      currentBlock.addInstruction(malloc);
      currentBlock.addInstruction(bitcast);

      return bitcastReg;
   }
}
