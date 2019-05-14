package ast;

public class NullType
   implements Type
{

   @Override
   public String toString() {
      return "NullType";
   }

   public String llvmType() {
      return "TODO";
   }
}
