package ssa;

import cfg.*;
import llvm.*;
import ast.*;

public class Variable {

   private String value; 
   private String id;
   private String type;
   private String label;


   public Variable(String value, String id, String type, CFGNode node) {
      this.value = value;
      this.id = id;
      this.type = type;
      label = node.getLabel().getId();
   }

   public String getType() {
      return type;
   }

   public String getId() {
      return id;
   }

   public String getValue() {
      return value;
   }

   public String getLabel() {
      return label;
   }

}