package ast;

import cfg.*;
import llvm.*;

public class DotExpression
   extends AbstractExpression
{
   private final Expression left;
   private final String id;

   public DotExpression(int lineNum, Expression left, String id)
   {
      super(lineNum);
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
      Value lValue = left.generateInstructions(currentBlock);
      String structType = lValue.getLlvmType();
      String structName = structNameFromLlvmType(structType);


      Struct struct = Tables.getFromStructTable(structName);
      String offset = struct.getFieldIndex(this.id) + "";
      String elementType = struct.getFieldType(this.id).llvmType();

      Value gepRegister = new Register(structType);
      Value loadRegister = new Register(elementType);

      // the type here might be wrong + or - a pointer
      Llvm gep = new llvm.GetElementPtr(gepRegister.getValue(), structType, 
         lValue.getValue(), offset);
      Llvm load = new llvm.Load(loadRegister.getValue(), elementType + "*", gepRegister.getValue());

      currentBlock.addInstruction(gep);
      currentBlock.addInstruction(load);

      return loadRegister;
   }

   private String structNameFromLlvmType(String structType) {
      int begin = structType.indexOf(".") + 1;
      int end = structType.indexOf("*");
      String name =  structType.substring(begin, end);

      return name;
   }
}
