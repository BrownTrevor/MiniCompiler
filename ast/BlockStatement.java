package ast;

import cfg.*;
import java.util.List;
import java.util.ArrayList;

public class BlockStatement
   extends AbstractStatement
{
   private final List<Statement> statements;

   public BlockStatement(int lineNum, List<Statement> statements)
   {
      super(lineNum);
      this.statements = statements;
   }

   public static BlockStatement emptyBlock()
   {
      return new BlockStatement(-1, new ArrayList<>());
   }

   public List<Statement> getStatements() {
      return this.statements;
   }

   @Override
   public String toString() {
      return "Block statement";
   }

   public CFGNode generateCFG(CFGNode currentBlock, CFGNode exitBlock) {
      for (Statement s : this.statements) {
         currentBlock = s.generateCFG(currentBlock, exitBlock);
      }
      return currentBlock;
   }
}
