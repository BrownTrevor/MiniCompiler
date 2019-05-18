package llvm;

public class Icmp implements Llvm, LlvmBranching {
   private String target;
   private String type;
   private String op1;
   private String op2;
   private String cond;

   public Icmp(String target, String cond, String type, String op1, String op2) {
      this.target = target;
      this.type = type;
      this.op1 = op1;
      this.op2 = op2;
      this.cond = cond;
   }

   public String getTarget() {
      return target;
   }

   public String getType() {
      return type;
   }

   public String getLeftOp() {
      return op1;
   }

   public String getRightOp() {
      return op2;
   }

   public String getCond() {
      return cond;
   }

   @Override
   public String toString() {
      return target + " = icmp " + cond + " " + type + " " + op1 + ", " + op2;
   }
}