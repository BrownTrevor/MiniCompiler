package cfg;

public class Immediate implements Value {
   private String llvmType;
   private String immediate;
   private CFGNode block;

   public Immediate(String t, String i, CFGNode block) {
      this.llvmType = t;
      this.immediate = i;
      this.block = block;
   }

   public String getValue() {
      return immediate;
   }

   public String getLlvmType() {
      return llvmType;
   }

   public boolean isRegister() {
      return false;
   }

   public CFGNode getBlock() {
      return this.block;
   }

   @Override
   public String toString() {
      return "Type: " + llvmType + "\nValue: " + immediate;
   }

}