package ast;

public class IntType
   implements Type
{
   @Override
   public String toString() {
      return "IntType";
   }

   public String llvmType() {
      return "i32";
   }
}
