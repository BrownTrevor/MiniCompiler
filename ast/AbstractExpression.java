package ast;

public abstract class AbstractExpression
   implements Expression
{
   private final int lineNum;

   public AbstractExpression(int lineNum)
   {
      this.lineNum = lineNum;
   }

   public int getLineNum() {
      return this.lineNum;
   }

   public String toString() {
      return "On line: " + this.lineNum;
   }
}
