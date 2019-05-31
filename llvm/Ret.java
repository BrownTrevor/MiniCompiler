package llvm;

public class Ret implements Llvm {

   private String type;
   private String reg;

   public Ret(String type, String reg) {
      this.type = type;
      this.reg = reg;
   }

   @Override
   public String toString() {
	   //ret i32 %u10
      return "ret " + type + " " + reg;
   }
}