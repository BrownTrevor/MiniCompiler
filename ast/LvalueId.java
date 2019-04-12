package ast;

public class LvalueId implements Lvalue 
{
   private final int lineNum;
   private final String id;

   public LvalueId(int lineNum, String id) 
   {
      this.lineNum = lineNum;
      this.id = id;
   }

   /**
    * @return the lineNum
    */
   public int getLineNum() 
   {
      return lineNum;
   }

   /**
    * @return the id
    */
   public String getId() 
   {
      return id;
   }
}
