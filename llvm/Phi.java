package llvm;

import cfg.*;
import ast.*;
import java.util.*;

public class Phi implements Llvm{

   private String id; // the mini variable that phi is computing
   private Register target; // the reg holding the result of the phi
   private List<Value> list; // all possible values for the phi

   public Phi(String id, Register target) {
      this.target = target;
      this.id = id;
      list = new ArrayList<Value>();
   }

   public void addOperand(Value v) {
      String targetType = target.getLlvmType();
      if (targetType == null) {
         target.setType(v.getLlvmType());
      }

      list.add(v);
   }

   public String getId() {
      return this.id;
   }

   public CFGNode getBlock() {
      return target.getBlock();
   }

   public String toString() {
      // %num0 = phi i32 [%_u54, %LU9], [%num, %LU8]
      String s = target.getValue() + " = phi " + target.getLlvmType();

      for (Value v : list) {
         s += (" [" + v.getValue() +", %"+ v.getBlock().getLabel().getId() + "],");
      }
      return s.substring(0,s.length()-1);
   }
}