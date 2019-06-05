package cfg;

import cfg.CFGNode;

public class Undefined implements Value {
   CFGNode block;

   public Undefined(CFGNode block) {
      this.block = block;
   }

   public String getValue() {
      return "undefined";
   }

   public String getLlvmType() {
      return "undefined";
   }

   public boolean isRegister() {
      return false;
   }

   public CFGNode getBlock() {
      return this.block;
   }

   @Override
   public String toString() {
      return "Undefined value in " + block.getLabel().getId();
   }

}