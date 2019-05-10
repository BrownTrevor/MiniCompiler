package ast;

public interface Statement
{
   public cfg.CFGNode generateCFG(cfg.CFGNode current, cfg.CFGNode exit);
}
