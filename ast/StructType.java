package ast;

public class StructType
   implements Type
{
   private final int lineNum;
   private final String name;

   public StructType(int lineNum, String name)
   {
      this.lineNum = lineNum;
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public String toString() {
      return "StructType; name: " + this.name;
   }

   public String llvmType() {
      return "%struct." + this.name + "*";
   }
}
