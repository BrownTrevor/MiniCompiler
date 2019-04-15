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

   public void addField(String fieldName, Type fieldData)
   {
      fields.put(fieldName, fieldData);
   }
   public void removeField(String fieldName)
   {
      fields.remove(fieldName);
   }

   public Type getField(String fieldName) {
      Type t = fields.get(fieldName);
      if (t != null) {
         return t;
      }

      System.err.println("Error: Field " + fieldName + " does not exist.");
      System.exit(1);
   }

   public String getName() 
   {
      return this.name;
   }
}
