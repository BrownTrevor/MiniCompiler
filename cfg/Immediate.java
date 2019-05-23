package cfg;


public class Immediate implements Value{
   private String llvmType;
   private String immediate;

   public Immediate(String t, String i) {
      llvmType = t;
      immediate = i;
   }

   public String getValue() {
      return immediate;
   }

   public String getLlvmType() {
      return llvmType;
   }

   public boolean isRegister(){
      return false;
   }

}