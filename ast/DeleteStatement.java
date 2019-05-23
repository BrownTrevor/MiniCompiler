package ast;

import cfg.*;
import llvm.*;


public class DeleteStatement
   extends AbstractStatement
{
   private final Expression expression;

   public DeleteStatement(int lineNum, Expression expression)
   {
      super(lineNum);
      this.expression = expression;
   }

   public Expression getExpression() {
      return this.expression;
   }

   @Override
   public String toString() {
      return "Delete statement";
   }

    public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBblock) {
      Value pointer = this.expression.generateInstructions(currentBlock);
      
      // %u83 = bitcast %struct.foo* %u82 to i8*
      // call void @free(i8* %u83)

      Value bitcastReg = new Register(pointer.getLlvmType());

      Llvm bitcast = new BitCast(bitcastReg.getValue(), 
         pointer.getLlvmType(), pointer.getValue(), "i8*");
      Llvm generic = new Generic("call void @free(i8* " + bitcastReg.getValue() + ")");

      currentBlock.addInstruction(bitcast);
      currentBlock.addInstruction(generic);

      return currentBlock;
   }
}
