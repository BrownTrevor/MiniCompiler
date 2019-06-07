package llvm;

import cfg.*;
import ast.*;
import java.util.*;

public class Phi implements Llvm{

   private String id; // the mini variable that phi is computing
   private Register target; // the reg holding the result of the phi
   private List<Operand> list; // all possible values for the phi

   public Phi(String id, Register target) {
      this.target = target;
      this.id = id;
      list = new ArrayList<Operand>();
   }

   
   public void addOperand(Value v, CFGNode predBlock) {
      String targetType = target.getLlvmType();
      if (targetType == null) {
         target.setType(v.getLlvmType());
      }

      list.add(new Operand(v, predBlock));
   }

   public String getId() {
      return this.id;
   }

   public CFGNode getBlock() {
      return target.getBlock();
   }

   public List<Value> getOperandValues() {
      List<Value> temp = new ArrayList<Value>();
      
      for(Operand o : list) {
         temp.add(o.v);
      }

      return temp;
   }

   public List<Operand> getOperands() {
      return this.list;
   }

   public String toString() {
      // %num0 = phi i32 [%_u54, %LU9], [%num, %LU8]
      String s = target.getValue() + " = phi " + target.getLlvmType();

      for (Operand o : list) {
         s += (" [" + o.v.getValue() +", %"+ o.getBlockLabel() + "],");
      }
      return s.substring(0,s.length()-1);
   }



   private class Operand {
      public Value v;
      public CFGNode predBlock;

      public Operand(Value v, CFGNode predBlock) {
         this.v = v;
         this.predBlock = predBlock;
      }


      public String getBlockLabel() {
         return this.predBlock.getLabel().getId();
      }
   }

}