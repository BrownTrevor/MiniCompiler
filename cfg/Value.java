package cfg;

import ast.*;

public interface Value {

   public String getLlvmType();
   public String getValue();
   public boolean isRegister();

   /**
   private String register;
   private String llvmType;
   
   public Value(String t, String reg) {
      llvmType = t;
      register = reg;
   } 

   public String getLlvmType() {
      return this.llvmType;
   }

   public String getRegister() {
      return this.register; 
   } */
}