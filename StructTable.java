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
      table.remove(structName);
   }
}

