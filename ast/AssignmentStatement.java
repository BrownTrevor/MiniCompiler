package ast;

import cfg.*;

public class AssignmentStatement
   extends AbstractStatement
{
   private final Lvalue target;
   private final Expression source;

   public AssignmentStatement(int lineNum, Lvalue target, Expression source)
   {
      super(lineNum);
      this.target = target;
      this.source = source;
   }

   public Lvalue getTarget() {
      return this.target;
   }

   public Expression getSource() {
      return this.source;
   }

   @Override
   public String toString() {
      return "Assignment statement";
   }

   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBblock) {
      Value rVal = this.source.generateInstructions(currentBlock);
      Value target = this.target.generateInstructions(currentBlock);

      String instruction = "store " + target.getRegister() + ", " 
         + rVal.getRegister();
      
      currentBlock.addInstruction(instruction);
      return currentBlock;
   }
}
