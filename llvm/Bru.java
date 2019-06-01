package llvm;

public class Bru implements Llvm, LlvmBranching{
   private String dest;

   public Bru(String dest) {
      this.dest = dest;
   }

   public String getLabel() {
      return dest;
   }

   @Override
   public String toString() {
      return "br label %" + dest;
   }
   
}