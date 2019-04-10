import java.util.Stack;

import ast.Declaration;
import ast.ReturnStatement;
import ast.StructType;
import ast.TrueExpression;
import ast.TypeDeclaration;
import ast.WhileStatement;
import jdk.jshell.TypeDeclSnippet;

public class TypeCheckVisitor implements Visitor 
{

   Stack<SymbolTable> symTable = new Stack<SymbolTable>(); 
   Stack<StructTable> structTable = new Stack<StructTable>(); 

   public void visit(Program x)
   {
      symTable.add(new SymbolTable());
      structTable.add(new StructTable());

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

      structTable.peek().addStruct(newStruct.getName(), newStruct);
   }

   public void visit(Declaration x)
   {
      // Add to symbol table
      symbTable.peek().addSymbol(x.getName(), new Symbol(x.getType(), x.getName(), 
        false, false, false));
   }
   
   public void visit(Function x)
   {
      
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

