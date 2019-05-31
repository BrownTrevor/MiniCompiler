package ast;

public class VoidType
   implements Type
{

   @Override
   public String toString() {
      return "VoidType";
   }

   public String llvmType() {
      return "void";
   }
}
