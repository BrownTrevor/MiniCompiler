package cfg;

import java.util.*;

public class CFGNode {
   private ArrayList<CFGNode> children;
   private ArrayList<String> instructions;
   private Label label;

   public CFGNode() {
      this.label = new Label();
      children = new ArrayList<CFGNode>();
      instructions = new ArrayList<String>();
   }

   public void addChild(CFGNode node) {
      children.add(node);
   }

   public List<CFGNode> getChildren() {
      return children;
   }

   public List<String> getInstructions() {
      return instructions;
   }

   public void addInstruction(String s) {
      instructions.add(s);
   }

   public String getLabel() {
      return label.getId();
   } 

}