package cfg;

import java.util.*;

public class Struct {
   private String name;
   private ArrayList<StructField> fields;

   public Struct(String name) {
      this.name = name;
      this.fields = new ArrayList<StructField>();
   }

   public Struct(ast.TypeDeclaration typeDecl) {
      this.name = typeDecl.getName();
      this.fields = new ArrayList<StructField>();

      for(ast.Declaration decl : typeDecl.getFields()) {
         this.addField(decl.getName(), decl.getType());
      }
   }

   public void addField(String fieldName, ast.Type fieldType) {
      if (getFieldWithoutErrorCheck(fieldName) == null) {
         this.fields.add(new StructField(fieldName, fieldType));
      }
      else {
         error("Redeclaration of field: " + fieldName + " in struct: " + this.name);
      } 
   }

   public void removeField(String fieldName) {
      for(int i = 0; i < fields.size(); i++) {
         if (fields.get(i).getName().equals(fieldName)) {
            fields.remove(i);
            return;
         }
      }
   }

   public int getStructSize() {
      return fields.size();
   }

   public ast.Type getFieldType(String fieldName) {
      for(int i = 0; i < fields.size(); i++) {
         if (fields.get(i).getName().equals(fieldName)) {
            return fields.get(i).getType();
         }
      }

      error("Cannot retrieve field: " + fieldName + " in struct: " + this.name + " because it does not exist");
      return null;
   }

   public int getFieldIndex(String fieldName) {
      for (int i = 0; i < fields.size(); i++) {
         if (fields.get(i).getName().equals(fieldName)) {
            return i;
         }
      }

      error("Cannot retrieve field: " + fieldName + " in struct: " + this.name + " because it does not exist");
      return -1;
   }

   public String getFieldName(String fieldName) {
      for (int i = 0; i < fields.size(); i++) {
         if (fields.get(i).getName().equals(fieldName)) {
            return fields.get(i).getName();
         }
      }

      error("Cannot retrieve field: " + fieldName + " in struct: " + this.name + " because it does not exist");
      return null;
   }

   public StructField getField(String fieldName) {
      for (int i = 0; i < fields.size(); i++) {
         if (fields.get(i).getName().equals(fieldName)) {
            return fields.get(i);
         }
      }

      error("Cannot retrieve field: " + fieldName + " in struct: " + this.name + " because it does not exist");
      return null;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public String toString() {
      String s = "Name: " + this.name + '\n';

      for(StructField field : this.fields) {
         s += field.getName() + "\t" + field.getType() + '\n';
      }

      return s;
   }

   public String llvmType() {
      return "%struct." + this.name + "*";
   }

   private static void error(String msg)
   {
      System.err.println(msg);
      System.exit(1);
   }

   private StructField getFieldWithoutErrorCheck(String fieldName) {
      for (int i = 0; i < fields.size(); i++) {
         if (fields.get(i).getName().equals(fieldName)) {
            return fields.get(i);
         }
      }
      return null;
   }
}
