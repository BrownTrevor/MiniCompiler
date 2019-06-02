package ast;

import cfg.*;

public class NullExpression
   extends AbstractExpression
{
   public NullExpression(int lineNum)
   {
      super(lineNum);
   }

   // idk if this is how null should be represented
   public Value generateInstructions(CFGNode current) {
      return new Immediate("null", "0");
   }
}
