package llvm;

public class Alloca implements Llvm {
   private String target;
   private String type;

   public Alloca(String target, String type) {
      this.target = target;
      this.type = type;
   }

   public String getTarget() {
      return target;
   }

   public String getType() {
      return type;
   }

   @Override
   public String toString() {
      return target + " = alloca " + type;
   }
}