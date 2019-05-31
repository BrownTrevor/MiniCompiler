package ast;

public class BoolType
   implements Type
{
   @Override
   public String toString() {
      return "BoolType";
   }

   public String llvmType() {
      return "i32";
   }
}
