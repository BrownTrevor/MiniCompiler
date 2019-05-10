package ast;

import cfg.*;

public class WhileStatement
   extends AbstractStatement
{
   private final Expression guard;
   private final Statement body;

   public WhileStatement(int lineNum, Expression guard, Statement body)
   {
      super(lineNum);
      this.guard = guard;
      this.body = body;
   }

   public Expression getGuard() {
      return this.guard;
   }

   public Statement getBody() {
      return this.body;
   }

   @Override
   public String toString() {
      return "While statement";
   }

   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBlock) {
      // TODO: make instructions for the guard and add to current block

      CFGNode conditionalBlock = new CFGNode();
      CFGNode bodyBlock = new CFGNode();
      CFGNode joinBlock = new CFGNode();

      currentBlock.addChild(conditionalBlock);
      conditionalBlock.addChild(bodyBlock);
      conditionalBlock.addChild(joinBlock);

      return joinBlock;
   }
}
