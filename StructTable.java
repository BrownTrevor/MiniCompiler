import java.util.HashMap;

public class StructTable
{
   HashMap<String, Struct> table;

   public StructTable() 
   {
      table = new HashMap<String, Struct>();
   }

   public addStruct(String structName, Struct structInstance)
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

   public removeStruct(String structName)
   {
      if(table.remove(structName) == null) {
         System.err.println("Error: Struct " + s + " does not exist.");
         System.exit(1);
      }
   }

   public Struct getStruct(String id)
   {
      Struct s = table.get(id);
      if (s)
      {
         return s;
      }

      System.err.println("Error: Struct " + s + " does not exist.");
      System.exit(1);
   }
}

