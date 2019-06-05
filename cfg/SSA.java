package cfg;

import java.util.ArrayList;
import java.util.List;

import cfg.CFGNode;
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
         block.addInstruction(phi);
      }
    
      writeVariable(id, block, val);
      return val;
   }



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
         phi.addOperand(readVariable(phi.getId(), pred));
      } 
   }


   public static void error(String s) {
      System.err.println(s);
      System.exit(1);
   }
}