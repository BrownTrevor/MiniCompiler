package ast;

import llvm.*;
import cfg.*;

public class LvalueDot
   implements Lvalue
{
   private final int lineNum;
   private final Expression left;
   private final String id;

   public LvalueDot(int lineNum, Expression left, String id)
   {
      this.lineNum = lineNum;
      this.left = left;
      this.id = id;
   }

   public Expression getLeft() {
      return this.left;
   }

   public String getId() {
      return this.id;
   }

   public Value generateInstructions(CFGNode currentBlock) {
      Value leftVal = left.generateInstructions(currentBlock);
      String structName = leftVal.getLlvmType().substring(0, 7);
      Struct struct = Tables.getFromStructTable(structName);
      StructField target = struct.getField(this.id);
      // <result> = getelementptr <pty>* <ptrval> <ty> <idx>

      String reg = Register.newRegister();
      String type = leftVal.getLlvmType() + "*";
      String pointer = leftVal.getRegister();
      String index = struct.getFieldIndex(this.id);
      
      Llvm instruction = new GetElementPtr(reg, type, pointer, index);
      currentBlock.addInstruction(instruction);
      
      // I'm not sure if this type is right
      return new Value(Register.currentRegister(), leftVal.getLlvmType());
   }
}

