import java.util.*;

public class Symbol {
   private String name;
   private ast.Type type;

   public Symbol(String name, ast.Type type) {
      this.name = name;
      this.type = type;
   }

   public Symbol(ast.Declaration decl) {
      this.name = decl.getName();
      this.type = decl.getType();
   }

   public String getName() {
      return this.name;
   }

   public ast.Type getType() {
      return this.type;
   }

   @Override
   public String toString() {
      String s = "Name: " + this.name + '\n';
      s += "Type: " + this.type.toString() + '\n';

      return s;
   }

   private static void error(String msg)
   {
      System.err.println(msg);
      System.exit(1);
   }
}
