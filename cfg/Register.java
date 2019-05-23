package cfg;

import cfg.*;

public class Register implements Value {
   private static int registerNumber = 0;
   private String register;
   private String llvmType;

   public static String currentRegister() {
      return ("%u" + registerNumber); 
   }

   public static String newRegister() {
      registerNumber++;
      return ("%u" + registerNumber);
   }

   
   public Register(String t) {
      registerNumber++;
      llvmType = t;
      register = "%u" + registerNumber;
   }

   public String getLlvmType() {
      return this.llvmType;
   }

   public String getValue() {
      return this.register;
   }

   public boolean isRegister() {
      return true;
   }
   
}