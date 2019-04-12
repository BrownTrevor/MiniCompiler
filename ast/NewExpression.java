package ast;

public class NewExpression extends AbstractExpression 
{
   private final String id;

   public NewExpression(int lineNum, String id)
   {
      super(lineNum);
      this.id = id;
   }

   /**
    * @return the id
    */
   public String getId() 
   {
      return this.id;
   }
}
