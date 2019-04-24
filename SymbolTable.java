import java.util.*;

public class SymbolTable {
   private HashMap<String, Symbol> table;

   public SymbolTable()
   {
      this.table = new HashMap<String, Symbol>();
   }

   public void addSymbol(String symbolName, Symbol symbolInstance) {
      if (this.table.get(symbolName) == null) {
         this.table.put(symbolName, symbolInstance);
      }
      else {
         this.error("Redeclaration of symbol: " + symbolName);
      }
   }

   public void removeSymbol(String symbolName) {
      if(this.table.remove(symbolName) == null) {
         this.error("Cannot remove symbol: " + symbolName + " because it does not exist");
      }
   }

   public Symbol getSymbol(String symbolName) {
      Symbol s = this.table.get(symbolName);

      if(s == null) {
         this.error("Cannot retrieve symbol: " + symbolName + " because it does not exist");
      }

      return s;
   }

   public boolean containsSymbol(String symbolName) {
      return this.table.containsKey(symbolName);
   }

   @Override
   public String toString() {
      String s = "";
      for(Map.Entry<String, Symbol> symbol : this.table.entrySet()) {
         s += symbol.getValue().toString() + '\n';
      }

      return s;
   }

   private static void error(String msg)
   {
      System.err.println(msg);
      System.exit(1);
   }
}
