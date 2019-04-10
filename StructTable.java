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
      table.put(structName, structInstance);
   }

   public removeStruct(String structName)
   {
      table.remove(structName);
   }
}

