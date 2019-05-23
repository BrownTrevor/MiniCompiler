package ast;

import cfg.*;

public class TrueExpression
   extends AbstractExpression
{
   public TrueExpression(int lineNum)
   {
      super(lineNum);
   }

   public Value generateInstructions(CFGNode current) {
      return new Immediate("i1", "1");
   }
}
