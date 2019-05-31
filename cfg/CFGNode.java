package cfg;

import java.util.*;
import llvm.*;

public class CFGNode {
   private ArrayList<CFGNode> children;
   private ArrayList<Llvm> instructions;
   private Label label;
   private String header;

   public CFGNode() {
      this.label = new Label();
      children = new ArrayList<CFGNode>();
      instructions = new ArrayList<Llvm>();
      header = null;
   }

   public void addChild(CFGNode node) {
      children.add(node);
   }

   public List<CFGNode> getChildren() {
      return children;
   }

   public List<Llvm> getInstructions() {
      return instructions;
   }

   public void addInstruction(Llvm s) {
      instructions.add(s);
   }

   public Label getLabel() {
      return this.label;
   } 

   public void setHeader(String header) {
      this.header = header;
   }

   public boolean hasHeader() {
      if(header == null) {
         return false;
      }
      return true;
   }

   public String getHeader() {
      return this.header;
   }


   @Override
   public String toString() {
      /*
      HashSet<CFGNode> seen = new HashSet<CFGNode>();
      ArrayList<CFGNode> queue = new ArrayList<CFGNode>();
      StringBuilder sb = new StringBuilder();

      queue.add(this);
      while (!(queue.isEmpty())) {
         CFGNode current = queue.remove(0);
         seen.add(current);

         if(current.hasHeader()) {
            sb.append(current.getHeader());
         }

         sb.append(current.getLabel().getId() + ":\n");
         
         List<Llvm> currentInstructions = current.getInstructions();
         for (Llvm instruction : currentInstructions) {
            sb.append("\t");
            sb.append(instruction.toString());
            sb.append("\n");
         }

         sb.append("\n");

         for (CFGNode child : current.getChildren()) {
            if (!(seen.contains(child))) {
               queue.add(child);
            }
         }
      }

      return sb.toString();
      */

      StringBuilder sb = new StringBuilder();
      CFGNode current = this;

      if(current.hasHeader()) {
         sb.append(current.getHeader() + "{\n");
      }

      sb.append(current.getLabel().getId() + ":\n");
      
      List<Llvm> currentInstructions = current.getInstructions();
      for (Llvm instruction : currentInstructions) {
         sb.append("\t");
         sb.append(instruction.toString());
         sb.append("\n");
      }


      return sb.toString();
   }
}
