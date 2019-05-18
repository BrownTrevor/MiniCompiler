
public class Mul implements Llvm, LlvmArithmetic {
   private String target;
   private String type;
   private String op1;
   private String op2;

   public Mul(String target, String type, String op1, String op2) {
      this.target = target;
      this.type = type;
      this.op1 = op1;
      this.op2 = op2;
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

   @Override
   public String toString() {
      return target + " = mul\t" + type + " " + op1 + ", " + op2;
   }
}