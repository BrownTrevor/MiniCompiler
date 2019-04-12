import java.util.HashMap;

public class SymbolTable 
{
   HashMap<String, Symbol> table;

   public SymbolTable()
   {
      table = new HashMap<String, Symbol>();
   }

   public void addSymbol(String symbolName, Symbol symbolInstance)
   {
      if (table.get(symbolName) == null)
      {
         table.put(symbolName, symbolInstance);
      }
      else 
      {
         System.out.println("Error: Redeclaration of symbol: " + symbolName);
      }
   }

   public Symbol removeSymbol(String symbolName)
   {
      return table.remove(structName);
   }

   public Symbol getSymbol(String symbolName)
   {
      return this.table.get(symbolName);
   }
}



