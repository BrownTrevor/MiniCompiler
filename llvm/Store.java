
public class Store implements Llvm {
   private String value;
   private String valType;
   private String pointer;
   private String pointerType;

   public Store(String value, String valType, String pointer, String pointerType) {
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