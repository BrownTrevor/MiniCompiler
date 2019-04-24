import java.util.*;

public class StructTable {
   private HashMap<String, Struct> table;

   public StructTable()
   {
      this.table = new HashMap<String, Struct>();
   }

   public void addStruct(String structName, Struct structInstance) {
      if (this.table.get(structName) == null) {
         this.table.put(structName, structInstance);
      }
      else {
         this.error("Redeclaration of struct: " + structName);
      }
   }

   public void removeStruct(String structName) {
      if(this.table.remove(structName) == null) {
         this.error("Cannot remove struct: " + structName + " because it does not exist");
      }
   }

   public Struct getStruct(String structName) {
      Struct s = this.table.get(structName);

      if(s == null) {
         this.error("Cannot retrieve struct: " + structName + " because it does not exist");
      }

      return s;
   }

   public boolean containsStruct(String structName) {
      return this.table.containsKey(structName);
   }

   @Override
   public String toString() {
      String s = "";
      for(Map.Entry<String, Struct> struct : this.table.entrySet()) {
         s += struct.getValue().toString() + '\n';
      }

      return s;
   }

   private static void error(String msg)
   {
      System.err.println(msg);
      System.exit(1);
   }
}
