package ast;

public interface Expression
{
   public cfg.Value generateInstructions(cfg.CFGNode current);
}
