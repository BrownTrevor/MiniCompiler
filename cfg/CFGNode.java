package cfg;

import java.util.*;
import llvm.*;

public class CFGNode {
   private ArrayList<CFGNode> children;
   private ArrayList<Llvm> instructions;
   private Label label;

   public CFGNode() {
      this.label = new Label();
      children = new ArrayList<CFGNode>();
      instructions = new ArrayList<Llvm>();
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


   @Override
   public String toString() {
      HashSet<CFGNode> seen = new HashSet<CFGNode>();
      ArrayList<CFGNode> queue = new ArrayList<CFGNode>();
      StringBuilder sb = new StringBuilder();

      queue.add(this);
      while (!(queue.isEmpty())) {
         CFGNode current = queue.remove(0);
         seen.add(current);

         sb.append(current.getLabel().getId() + ":\n");
         
         List<Llvm> currentInstructions = current.getInstructions();
         for (Llvm instruction : currentInstructions) {
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
   }
}