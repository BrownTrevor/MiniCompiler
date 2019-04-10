import java.util.HashMap;


public class Struct 
{
   // hold the field information for this generic struct 
   String name;
   HashMap<String, Field> fields; 

   public Struct(String name)
   {
      this.name = name;
      fields = new HashMap<String, Field>();
   }

   public addField(String fieldName, Field fieldData)
   {
      fields.put(fieldName, fieldData);
   }
   public removeField(String fieldName)
   {
      fields.remove(fieldName);
   }
}
