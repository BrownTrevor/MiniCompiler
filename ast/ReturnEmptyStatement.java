package ast;

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
}
