
package cfg;

import java.util.*;

public class StructField {
   private String name;
   private ast.Type type;

   public StructField(String name, ast.Type type) {
      name = this.name;
      type = this.type;
   }

   public String getName() {
      return this.name;
   }

   public ast.Type getType() {
      return this.type;
   }
}







