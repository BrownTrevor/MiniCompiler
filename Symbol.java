public class Symbol
{
   public Type type;
   public String name;
   public boolean isStatic;
   public boolean isMutable;
   public boolean isPrivate;

   public Symbol(Type type, String name, boolean isStatic, boolean isMutable, 
     boolean isPrivate) 
   {
      this.type = type;
      this.name = name;
      this.isStatic = isStatic;
      this.isMutable = isMutable;
      this.isPrivate = isPrivate;
   }

   /**
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * @return the type
    */
   public Type getType() {
      return type;
   }

}
