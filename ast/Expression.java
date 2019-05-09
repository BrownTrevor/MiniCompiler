package ast;

public interface Expression
{
   public Value generateInstructions(CFGNode current);
}
