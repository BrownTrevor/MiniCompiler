import java.util.ArrayList;
import java.util.Stack;

import ast.Declaration;
import ast.Function;
import ast.ReturnStatement;
import ast.StructType;
import ast.TrueExpression;
import ast.TypeDeclaration;
import ast.WhileStatement;
import jdk.jshell.TypeDeclSnippet;

public class TypeCheckVisitor implements Visitor 
{

   ArrayList<SymbolTable> symTables = new ArrayList<SymbolTable>(); 
   ArrayList<StructTable> structTables = new ArrayList<StructTable>(); 

   public void visit(Program x)
   {
      symTables.add(new SymbolTable());
      structTables.add(new StructTable());

      for (Type t : x.getTypes()) 
      {
         this.visit(t);
      }

      for (Declaration d : x.getDecls())
      {
         this.visit(d);
      }

      for (Function f : x.getFuncs()) 
      {
         this.visit(f);
      }  
   }

   public void visit(TypeDeclaration x) 
   {
      // Add to Struct table
      Struct newStruct = new Struct(x.getName());

      for (Declaration d : x.getFields()) 
      {
         newStruct.addField(d.getName(), d.getType());
      }

      structTables.get(structTable.size()-1).addStruct(newStruct.getName(), newStruct);
   }

   /*
    * Don't think this is needed?
      public void visit(List<Declaration> x) 
      {
         for (Declaration d : x)
         {
            symTable.get(symTable.size()-1).addSymbol(d.getName(), d.getType());
         }
      }
   */

   public void visit(Declaration x)
   {
      // Add to symbol table
      bool isStatic = false;
      bool isMutable = false;
      bool isPrivate = false;
      Symbol newSymbol = new Symbol(x.getType(), x.getName(), isStatic, isMutable, isPrivate);

      symTables.get(symTable.size()-1).addSymbol(x.getName(), newSymbol);
   }
   
   public void visit(Function x)
   {
      // Add header information to the symbol table for reference during invokation 

      Type func = new FunctionType(x.getName(), x.getRetType(), x.getParams());
      bool isStatic = false;
      bool isMutable = false;
      bool isPrivate = false;
      Symbol newSymbol = new Symbol(func, x.getName(), isStatic, isMutable, isPrivate);

      symTable.get(symTable.size() - 1).addSymbol(x.getName(), newSymbol);

      //Add new symTable for this function's scope
      symTable.add(new SymbolTable());

      this.visit(x.getParams());
      this.visit(x.getLocals());
      this.visit(x.getBody());

      //Remove the symTable for this function's scope
      symTable.remove(symTable.size()-1);
   }






   // Statements
   public WhileStatement visit(WhileStatement x) 
   {
      this.visit(x.getBody());
      this.visit(x.getGuard());

      return x;
   }

   public ReturnStatement visit(ReturnStatement x)
   {
      this.visit(x.getExpression());

      return x;
   }

   // Expressions
   public UnaryExpression visit(UnaryExpression x) 
   {
      this.visit(x.getOperator());
      this.visit(x.getOperand());

      return x;
   }

   public TrueExpression visit(TrueExpression x) 
   {
      return x;
   }

   // Types
   public VoidType visit(VoidType x) 
   {
      return x;
   }

   public StructType visit(StructType x) 
   {
      // Store name in struct table? x.getName();
      return x;
   }



}

