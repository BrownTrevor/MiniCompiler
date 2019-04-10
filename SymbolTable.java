import java.util.HashMap;

public class SymbolTable 
{
   HashMap<String, Symbol> table;

   public SymbolTable()
   {
      table = new HashMap<String, Symbol>();
   }

   public addSymbol(String symbolName, Symbol symbolInstance)
   {
      table.put(symbolName, symbolInstance);
   }

   public removeSymbol(String symbolName)
   {
      table.remove(structName);
   }
}


