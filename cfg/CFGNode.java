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

   public String getLabel() {
      return label.getId();
   } 

}