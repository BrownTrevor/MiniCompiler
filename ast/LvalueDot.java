package ast;

import llvm.*;
import cfg.*;
import globals.*;

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
      String structType = leftVal.getLlvmType();
      String structName = structNameFromLlvmType(structType);
      
      Struct struct = Tables.getFromStructTable(structName);
      StructField target = struct.getField(this.id);
      String type = leftVal.getLlvmType();

      // <result> = getelementptr <pty>* <ptrval> <ty> <idx>

      Register register = new Register(target.getType().llvmType() + "*", currentBlock);
      String reg = register.getValue();
      String pointer = leftVal.getValue();
      String index = struct.getFieldIndex(this.id) + "";
      
      Llvm instruction = new GetElementPtr(reg, type, pointer, index);
      currentBlock.addInstruction(instruction);
      
      // I'm not sure if this type is right
      return register;
   }
   
   private String structNameFromLlvmType(String structType) {
      int begin = structType.indexOf(".") + 1;
      int end = structType.indexOf("*");
      String name = structType.substring(begin, end);

      return name;
   }
}

