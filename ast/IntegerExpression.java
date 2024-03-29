package ast;

import cfg.*;

public class IntegerExpression
   extends AbstractExpression
{
   private final String value;

   public IntegerExpression(int lineNum, String value)
   {
      super(lineNum);
      this.value = value;
   }

   public String getValue() {
      return this.value;
   }


   public Value generateInstructions(CFGNode currentBlock){
      return new Immediate("i32", value, currentBlock);
   }


}
