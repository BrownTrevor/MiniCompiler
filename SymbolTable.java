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
      Symbol symbol = this.table.remove(structName);

      if (symbol != null) {
         return symbol;
      }

      System.err.println("Error: Symbol " + symbolName + " not found in table");
      System.exit(1);
   }

   public Symbol getSymbol(String symbolName)
   {
      Symbol symbol = this.table.get(structName);

      if (symbol != null) {
         return symbol;
      }

      System.err.println("Error: Symbol " + symbolName + " not found in table");
      System.exit(1);
   }
}



