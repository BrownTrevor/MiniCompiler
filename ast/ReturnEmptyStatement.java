package ast;

import cfg.*;
import llvm.*;

public class ReturnEmptyStatement
   extends AbstractStatement
{
   public ReturnEmptyStatement(int lineNum)
   {
      super(lineNum);
   }

   @Override
   public String toString() {
      return "Empty statement";
   }

   
   
   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBlock) {
      String exitLabel = exitBlock.getLabel().getId();
      Llvm branch = new Bru(exitLabel);
      currentBlock.addInstruction(branch);  
      currentBlock.setTerminal(true);

      currentBlock.addChild(exitBlock);
      exitBlock.addPred(currentBlock);

      return currentBlock;
   }
}
