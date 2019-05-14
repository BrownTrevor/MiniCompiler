package cfg;

import java.util.*;

public class Struct {
   private String name;
   private HashMap<String, ast.Type> fields;

   public Struct(String name) {
      this.name = name;
      this.fields = new HashMap<String, ast.Type>();
   }

   public Struct(ast.TypeDeclaration typeDecl) {
      this.name = typeDecl.getName();
      this.fields = new HashMap<String, ast.Type>();

      for(ast.Declaration decl : typeDecl.getFields()) {
         this.addField(decl.getName(), decl.getType());
      }
   }

   public void addField(String fieldName, ast.Type fieldType) {
      if (this.fields.get(fieldName) == null) {
         this.fields.put(fieldName, fieldType);
      }
      else {
         this.error("Redeclaration of field: " + fieldName + " in struct: " + this.name);
      } 
   }

   public void removeField(String fieldName) {
      if(this.fields.remove(fieldName) == null) {
         this.error("Cannot remove field: " + fieldName + " in struct: " + this.name + " because it does not exist");
      }
   }

   public ast.Type getField(String fieldName) {
      ast.Type t = this.fields.get(fieldName);

      if(t == null) {
         this.error("Cannot retrieve field: " + fieldName + " in struct: " + this.name + " because it does not exist");
      }

      return t;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public String toString() {
      String s = "Name: " + this.name + '\n';

      for(Map.Entry<String, ast.Type> field : this.fields.entrySet()) {
         s += field.getKey().toString() + "\t" + field.getValue().toString() + '\n';
      }

      return s;
   }

   private static void error(String msg)
   {
      System.err.println(msg);
      System.exit(1);
   }
}
