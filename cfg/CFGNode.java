package cfg;

import java.util.*;
import llvm.*;

public class CFGNode {
   private ArrayList<CFGNode> children;
   private ArrayList<CFGNode> preds;
   private ArrayList<Llvm> instructions;
   
   private HashMap<String, Value> valueTable;
   private List<Phi> incompletePhis;

   private Label label;
   private String header;
   private boolean sealed;
   private boolean terminal;

   public CFGNode() {
      this.label = new Label();
      children = new ArrayList<CFGNode>();
      instructions = new ArrayList<Llvm>();
      header = null;
      terminal = false;

      preds = new ArrayList<CFGNode>();
      valueTable = new HashMap<String, Value>();
      incompletePhis = new ArrayList<Phi>();
      sealed = false;
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

   public void addInstruction(int i, Llvm s) {
      instructions.add(i, s);
   }

   public void sealBlock() {
      this.sealed = true;
   }

   public boolean isSealed() {
      return this.sealed;
   }

   public void addPred(CFGNode node) {
      if(!node.isTerminal()) {
         preds.add(node);
      }
   }

   public List<CFGNode> getPreds() {
      return this.preds;
   }

   public boolean isTerminal() {
      return terminal;
   }

   public void setTerminal(boolean b) {
      this.terminal = b;
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

   public void addToValueTable(String n, Value v){
      this.valueTable.put(n,v);
   }

   public Value getFromValueTable(String n) {
      Value v = this.valueTable.get(n);

      return v;
   }

   public void addToIncompletePhis(Phi p) {
      incompletePhis.add(p);
   } 

   public List<Phi> getIncompletePhis() {
      return incompletePhis;
   }

  
   @Override
   public String toString() {
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

   private void error(String msg) {
      System.err.println(msg);
      System.exit(1);
   }



}
