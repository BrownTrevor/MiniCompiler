package cfg;

import java.util.ArrayList;

public class Tables {
   
   private static StructTable structTable;
   private static ArrayList<SymbolTable> symbolTable;


   // Adds structName and structInstance to the current/top struct table
   public static void addToStructTable(Struct struct) {
      structTable.addStruct(struct.getName(), struct);
   }

   // Retrieves the struct with name structName at the most recent scope
   public static Struct getFromStructTable(String structName) {
      if (!structTable.containsStruct(structName)) {
         error("Cannot retrieve struct: " + structName + " because it does not exist");
      }

      return structTable.getStruct(structName); 
   }

   // true if in struct tables; false otherwise
   public static boolean isInStructTable(String structName) {
      if (structTable.containsStruct(structName)) {
         return true;
      }

      return false;
   }

   // Adds symbolName and symbolInstance to current/top symbol table
   public static void addToSymbolTable(Symbol symbol) {
      symbolTable.get(symbolTable.size() - 1).addSymbol(symbol.getName(), symbol);
   }

   // Retrieves the symbol with name symbolName at the most recent scope
   public static Symbol getFromSymbolTable(String symbolName) {
      Symbol topSymbol = null;

      for (SymbolTable table : symbolTable) {
         if (table.containsSymbol(symbolName)) {
            topSymbol = table.getSymbol(symbolName);
         }
      }

      if (topSymbol == null) {
         error("Cannot retrieve symbol: " + symbolName + " because it does not exist");
      }

      return topSymbol;
   }

   // true if in symbol tables; false otherwise
   public static boolean isInSymbolTable(String symbolName) {
      for (SymbolTable table : symbolTable) {
         if (table.containsSymbol(symbolName)) {
            return true;
         }
      }
      return false;
   }

   // removes most recent (highest scope) symbol matching symbolName from
   // symbolTables
   public static void removeFromSymbolTable(String symbolName) {
      for (int i = symbolTable.size() - 1; i >= 0; i--) {
         SymbolTable table = symbolTable.get(i);
         if (table.containsSymbol(symbolName)) {
            table.removeSymbol(symbolName);
            return;
         }
      }
   }

   public static void pushSymbolTable(){
      symbolTable.add(new SymbolTable());
   }

   public static void popSymbolTable() {
      if(symbolTable.size() > 0) {
         symbolTable.remove(symbolTable.size() - 1);
      }
   }

   public static void setSymbolTable(ArrayList<SymbolTable> sym) {
      symbolTable = sym;
   }

   public static void setStructTable(ArrayList<StructTable> struct) {
      structTable = struct.get(0);
   }

   public static void setStructTable(StructTable struct) {
      structTable = struct;
   }

   public static void initStructTable() {
      structTable = new StructTable();
   }

   public static void initSymbolTable() {
      symbolTable = new ArrayList<SymbolTable>();
      symbolTable.add(new SymbolTable());
   }

   private static void error(String msg) {
      System.err.println(msg);
      System.exit(1);
   }
}