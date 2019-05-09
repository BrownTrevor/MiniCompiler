package cfg;

import ast.*;

public class Value {
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
   }
}