package llvm;

public class Generic implements Llvm {
   private String instruction;

   public Generic(String instruction) {
      this.instruction = instruction;
   }

   public String toString() {
      return instruction;
   }
   
}