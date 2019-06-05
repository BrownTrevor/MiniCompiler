package cfg;

import cfg.*;

public class Register implements Value {
   private static int registerNumber = 0;
   private String register;
   private String llvmType;
   private CFGNode block;

   public static String currentRegister() {
      return ("%u" + registerNumber); 
   }

   public static String newRegister() {
      registerNumber++;
      return ("%u" + registerNumber);
   }

   
   public Register(String t, CFGNode block) {
      registerNumber++;
      this.llvmType = t;
      this.block = block;
      this.register = "%u" + registerNumber;
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

   public CFGNode getBlock() {
      return this.block;
   }

   public void setType(String type) {
      this.llvmType = type;
   }

   @Override
   public String toString() {
      return "Type: " + llvmType + "\nValue: " + register;
   }
   
}