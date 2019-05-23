package llvm;

public class BitCast implements Llvm {
   private String target;
   private String type1;
   private String type2;
   private String value;

   public BitCast(String target, String type1, String value, String type2) {
      this.target = target;
      this.type1 = type1;
      this.type2 = type2;
      this.value = value;
   }

   public String getTarget() {
      return target;
   }

   public String getType1() {
      return type1;
   }

   public String getType2() {
      return type2;
   }

   public String getValue() {
      return value;
   }

   @Override
   public String toString() {
      return target + " = bitcast " + type1 + " " + value + " to " + type2;
   }

}