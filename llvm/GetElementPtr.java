package llvm;

public class GetElementPtr implements Llvm{
   private String target;
   private String type;
   private String pointer;
   private String index;

   public GetElementPtr(String target, String type, String pointer, String index) {
      this.target = target;
      this.type = type;
      this.pointer = pointer;
      this.index = index;
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
   
   public String getIndex() {
      return index;
   }

   @Override
   public String toString() {
      return target + " = getelementptr " + type + " " + pointer +  ", i1 0, i32 " + index;
   }
   
}