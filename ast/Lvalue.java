package ast;
import cfg.*;

public interface Lvalue
{
   public Value generateInstructions(CFGNode currentBlock);
}
