
package llvm;

public class Load implements Llvm {
   private String target;
   private String type;
   private String pointer;

   public Load(String target, String type, String pointer) {
      this.target = target;
      this.type = type;
      this.pointer = pointer;
   }

   public String getTarget() {
      return target;
   }

   public String getType() {
      return type;
   }

   public String getPointer() {
      return pointer;
   }


   @Override
   public String toString() {
      return target + " = load " + type + " " + pointer;
   }

}