package llvm;

public class Store implements Llvm {
   private String value;
   private String valType;
   private String pointer;
   private String pointerType;

   public Store(String valType, String value, String pointerType, String pointer) {
      this.value = value;
      this.valType = valType;
      this.pointer = pointer;
      this.pointerType = pointerType;
   }

   public String getValue() {
      return value;
   }

   public String getValueType() {
      return valType;
   }

   public String getPointer() {
      return pointer;
   }

   public String getPointerType() {
      return pointerType;
   }

   @Override
   public String toString() {
      return "store " + valType + " " + value + ", " + pointerType + " " + pointer;
   }

}