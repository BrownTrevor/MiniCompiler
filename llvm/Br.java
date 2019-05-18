

public class Br implements Llvm, LlvmBranching {
   private String cond;
   private String trueLabel;
   private String falseLabel;

   public Br(String cond, String trueLabel, String falseLabel) {
      this.cond = cond;
      this.trueLabel = trueLabel;
      this.falseLabel = falseLabel;
   }

   public String getConditional() {
      return cond;
   }

   public String getTrueLabel(){
      return trueLabel;
   }

   public String getFalseLabel() {
      return falseLabel;
   }

   @Override
   public String toString() {
      return "br i1 " + cond +  ", label " + trueLabel + ", label " + falseLabel;
   }
   
}