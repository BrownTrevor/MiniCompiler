package ast;

public class StructType implements Type {
   private final int lineNum;
   private final String name;

   public StructType(int lineNum, String name) {
      this.lineNum = lineNum;
      this.name = name;
   }

   public StructType(Struct struct) {
      this.lineNum = 0;
      this.name = struct.getName();
   }

   public String getName() {
      return this.name;
   }
}
