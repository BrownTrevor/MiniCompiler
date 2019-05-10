package ast;

import cfg.*;

public class ConditionalStatement
   extends AbstractStatement
{
   private final Expression guard;
   private final Statement thenBlock;
   private final Statement elseBlock;

   public ConditionalStatement(int lineNum, Expression guard,
      Statement thenBlock, Statement elseBlock)
   {
      super(lineNum);
      this.guard = guard;
      this.thenBlock = thenBlock;
      this.elseBlock = elseBlock;
   }

   public Expression getGuard() {
      return this.guard;
   }

   public Statement getThenBlock() {
      return this.thenBlock;
   }

   public Statement getElseBlock() {
      return this.elseBlock;
   }

   @Override
   public String toString() {
      return "Conditional statement";
   }
   
   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBlock) {
      // TODO: make instructions for the guard and add to current block

      CFGNode thenBlock = new CFGNode();
      CFGNode thenResBlock = this.thenBlock.generateCFG(thenBlock, exitBlock);

      CFGNode elseBlock = new CFGNode();
      CFGNode elseResBlock = this.elseBlock.generateCFG(elseBlock, exitBlock);

      CFGNode joinBlock = new CFGNode();
      thenResBlock.addChild(joinBlock);
      elseResBlock.addChild(joinBlock);

      currentBlock.addChild(thenBlock);
      currentBlock.addChild(elseBlock);

      return joinBlock;
   }
}
