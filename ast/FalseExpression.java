package ast;

import cfg.*;

public class FalseExpression
   extends AbstractExpression
{
   public FalseExpression(int lineNum)
   {
      super(lineNum);
   }

   public Value generateInstructions(CFGNode current) {
      return new Immediate("i1", "0");
   }
}
