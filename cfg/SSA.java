package cfg;

import java.util.ArrayList;
import java.util.List;

import cfg.CFGNode;
import cfg.Value;
import llvm.*;
import globals.*;

public class SSA {

   private static List<CFGNode> sealedBlocks = new ArrayList<CFGNode>();

   public static void writeVariable(String id, CFGNode block, Value value) {
      block.addToValueTable(id, value);
   }

   public static Value readVariable(String id, CFGNode block) {
      Value v = block.getFromValueTable(id);
      if(v != null) {
         return v;
      }
      return readVariableFromPredecessors(id, block);
   }

   public static Value readVariableFromPredecessors(String id, CFGNode block) {
      Register val = null;
      Phi phi = null;

      String type = Tables.getFromSymbolTable(id).getType().llvmType();
      
      // * if block not in sealedBlocks:
      if(!block.isSealed()){
         val = new Register(type, block); // we don't know the type until we know the type of at least one operand
         phi = new Phi(id, val);
         block.addInstruction(0, phi);
         block.addToIncompletePhis(phi);
         // add to incomplete phis
      }
      else if (block.getPreds().size() == 0) { 
         error(id + " is undefined");
      }
      else if (block.getPreds().size() == 1) {
         return readVariable(id, block.getPreds().get(0));
      }
      else {
         val = new Register(type, block); // we don't know the type until we know the type of at least one operand
         phi = new Phi(id, val);
         writeVariable(id, block, val); // breaks cycles
         addPhiOperands(phi);
         block.addInstruction(0, phi);
      }
    
      writeVariable(id, block, val);
      return val;
   }

   /*
   // This might be 
   public void removeTrivialPhis (CFGNode block) {
      List<Phi> allPhis = block.getPhis(); 

      for (Phi p : allPhis)
      {
         removeTrivialPhi(p);
      }

   }

   public void removeTrivialPhi(Phi phi, CFGNode block) {
      Value phiOp = null;

      // check that all operands are the same
      for (Value op : phi.getOperands()) {
         if(phiOp == null) {
            phiOp = op;
         }
         else if(phiOp != op) {
            return;
         }
      }
      //remove this phi instruction from the block
      
      


   }

   public void progatePhiRemoval(Value phiReg, Value trivialVal, CFGNode block) {
      for (Llvm instr : block.getInstructions) {
         for(String currValue : instr.getOps()) {
            if(phiReg.getValue().equals(currValue)) {

            }
         }
      }
   }


   //what makes a phi trivial?
      a phi is trivial if all its operands are the same
   // if a phi is removed for being trivial, try to remove all its users as well
   // when replacing a trivial phi, replace it with its operand
   */




   public static void sealBlock(CFGNode block) {
      for (Phi p : block.getIncompletePhis()) {
         addPhiOperands(p);
      }

      //seal the block
      block.sealBlock();
   }

   public static void addPhiOperands(Phi phi) {
      // sets the type of the register that holds the phi instr
      for(CFGNode pred : phi.getBlock().getPreds()) {
         phi.addOperand(readVariable(phi.getId(), pred), pred);
      } 
   }


   public static void error(String s) {
      new Exception().printStackTrace(); 
      System.err.println(s);
      System.exit(1);
   }
}