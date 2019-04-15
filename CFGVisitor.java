import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.List;

import ast.*;
import ast.UnaryExpression.Operator;
import ast.BinaryExpression.Operator;

public class CFGVisitor implements Visitor{
   ArrayList<SymbolTable> symTables = new ArrayList<SymbolTable>();
   ArrayList<StructTable> structTables = new ArrayList<StructTable>();
   ArrayList<> CFGList = new ArrayList<>();


   public void visit(Program x) {
      symTables.add(new SymbolTable());
      structTables.add(new StructTable());

      this.visitList(x.getTypes());
      this.visitList(x.getDecls());
      this.visitList(x.getFuncs());

      if (this.getFromSymTables("main") == null) {
         System.out.println("Must contain main function");
         exit(1);
      }
   }












   // ===================
   // Helper Functions
   // ===================


   private Symbol getSymbolFromTables(String id) {
      for (int i = symTables.size() - 1; i >= 0; i--) {
         Symbol symbol = symTables.get(i).getSymbol(id);
         if (symbol != null) {
            return symbol;
         }
      }

      System.err.println("Error: Identifier " + id + " not found in symbol table.");
      System.exit(1);
   }

   private Struct getStructFromTables(String id) {
      Struct s = structTables.get(structTables.size() - 1).getStruct(id);
      if (s != null) {
         return s;
      }

      System.err.println("Error: Struct " + id + " not found in struct table.");
      System.exit(1);
   }

   private Struct removeStructFromTables(String id) {
      structTables.get(structTables.size() - 1).removeStruct(id);

      System.err.println("Error: Struct " + id + " not found in struct table.");
      System.exit(1);
   }


   private void visitDeclList(List<Declaration> lx) {
      for (Declaration d : lx) {
         this.visit(d);
      }
   }


}