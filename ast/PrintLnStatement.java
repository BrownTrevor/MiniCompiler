package ast;

public class PrintLnStatement extends AbstractStatement 
{
   private final Expression expression;

   public PrintLnStatement(int lineNum, Expression expression) 
   {
      super(lineNum);
      this.expression = expression;
   }

   /**
    * @return the expression
    */
   public Expression getExpression() 
   {
      return expression;
   }
}
