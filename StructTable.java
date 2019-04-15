import java.util.HashMap;

public class StructTable
{
   HashMap<String, Struct> table;

   public StructTable() 
   {
      table = new HashMap<String, Struct>();
   }

   public void addStruct(String structName, Struct structInstance)
   {
      if (table.get(structName) == null)
      {
         table.put(structName, structInstance);
      }
      else 
      {
         System.out.println("Error: Redeclaration of struct: " + structName);
      }
   }

   public void removeStruct(String structName)
   {
      if(table.remove(structName) == null) {
         System.err.println("Error: Struct " + s + " does not exist.");
         System.exit(1);
      }
   }

   public Struct getStruct(String id)
   {
      Struct struct = this.table.get(id);

      if (struct != null) {
         return struct;
      }

      System.err.println("Error: Struct " + s + " does not exist.");
      System.exit(1);
   }
}

