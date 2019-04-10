import java.util.HashMap;


public class Struct 
{
   // hold the field information for this generic struct 
   private String name;
   private HashMap<String, Type> fields; 

   public Struct(String name)
   {
      this.name = name;
      fields = new HashMap<String, Type>();
   }

   public addField(String fieldName, Type fieldData)
   {
      fields.put(fieldName, fieldData);
   }
   public removeField(String fieldName)
   {
      fields.remove(fieldName);
   }

   public String getName() 
   {
      return this.name;
   }
}
