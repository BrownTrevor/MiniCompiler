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

   private Symbol getFromSymTables(String name) {
      for(SymbolTable symTable : symTables) {
         if(symTable.containsKey(name)) {
            return symTable.get(name);
         }
      }

      return null;
   }


   public void visit(Program x)
   {
      symTables.add(new SymbolTable());
      structTables.add(new StructTable());

      this.visitList(x.getTypes());
      this.visitList(x.getDecls());
      this.visitList(x.getFuncs());
   }

   /*
    * ----------------------
    *  Visit Types
    * ----------------------
    */

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

   private void visitList(List<TypeDeclaration> lx) {
      for (TypeDeclaration x : lx) {
         this.visit(x);
      }
   }

   /*
    * ----------------------
    *  Visit Declarations
    * ----------------------
    */

   public void visit(Declaration x)
   {
      // Add to symbol table
      bool isStatic = false;
      bool isMutable = false;
      bool isPrivate = false;
      Symbol newSymbol = new Symbol(x.getType(), x.getName(), isStatic, isMutable, isPrivate);

      symTables.get(symTables.size()-1).addSymbol(x.getName(), newSymbol);
   }

   private void visitList(List<Declaration> lx) {
      for (Declaration d : lx) {
         this.visit(d);
      }
   }
   
   /*
    * ----------------------
    *  Visit Functions
    * ----------------------
    */

   public void visit(Function x)
   {
      // Add header information to the symbol table for reference during invokation 

      Type func = new FunctionType(x.getName(), x.getRetType(), x.getParams());
      bool isStatic = false;
      bool isMutable = false;
      bool isPrivate = false;
      Symbol newSymbol = new Symbol(func, x.getName(), isStatic, isMutable, isPrivate);

      symTables.get(symTables.size() - 1).addSymbol(x.getName(), newSymbol);

      //Add new symTable for this function's scope
      symTables.add(new SymbolTable());

      this.visitList(x.getParams()); // List<Declaration>
      this.visitList(x.getLocals()); // List<Declaration>
      this.visit(x.getBody()); // Statement

      //Remove the symTable for this function's scope
      symTables.remove(symTables.size()-1);
   }

   private void visitList(List<Function> lx) {
      for (Function x : lx) {
         this.visit(x);
      }
   }

   /*
    * ----------------------
    *  Visit Statements
    * ----------------------
    */

   public void visit(BlockStatement x) {
      for (Statement s : x.getStatements()) {
         this.visit(s);
      }
   }

   /*
    * Logic:
    * 1. target must be declared in scope; this is accomplished by visit(lvalue)
    * 1a. this.visit(target) will short-circuit exit if target is not declared in scope,
    *     and this.visit(source) will not be called
    * 2. target type must equal type that source evaluates to
    */
   public void visit(AssignmentStatement x) {
      Lvalue target = x.getLValue();
      Expression source = x.getSource();

      if(!(this.visit(target) instanceof this.visit(source))) {
         System.out.println("Target: " + target.getId() + " type does not equal source type");
         System.exit(1);
      }
   }

   /*
    * Logic:
    * 1. exp must evaluate to integer
    */
   public void visit(PrintStatement x) {
      Expression exp = x.getExpression();

      if(!(this.visit(exp) instanceof Integer)) {
         System.out.println("Println requires an integer expression");
         System.exit(1);
      }
   }

   /*
    * Logic:
    * 1. guard must evaluate to boolean
    * 2. else block is optional; represented as an empty block statement
    */
   public void visit(ConditionalStatement x) {
      Expression guard = x.getGuard();
      Statement thenBlock = x.getThenBlock();
      Statement elseBlock = x.getElseBlock();

      if(this.visit(guard) instanceof Boolean) {
         this.visit(thenBlock);
         this.visit(elseBlock);
      }
      else {
         System.out.println("Conditional guard expression must evaluate to a boolean");
         System.exit(1);
      }

   }

   /*
    * Logic:
    * 1. guard must evaluate to boolean
    */
   public void visit(WhileStatement x) {
      Expression guard = x.getGuard();
      Statement body = x.getBody();

      if(this.visit(guard) instanceof Boolean) {
         this.visit(body);
      }
      else {
         System.out.println("Loop guard expression must evaluate to a boolean");
         System.exit(1);
      }
   }

   public Type visit(DeleteStatement x) {
      Expression exp = x.getExpression();
      // TODO: figure out how to deallocate the referenced structure

      return this.visit(exp);
   }

   // TODO: is this correct and full?
   public Type visit(ReturnStatement x) {
      Expression exp = x.getExpression();

      return this.visit(exp);
   }

   /*
    * Logic:
    * 1. name must exist as a function in scope
    * 2. arguments must be of proper number and type for function invoked
    * 3. return the return type of the function
    */
   public Type visit(InvocationStatement x) {
      InvocationExpression e = x.getExpression();

      String name = e.getName();
      Symbol func = this.getFromSymTables(name);

      if(func == null) {
         System.out.println("Function with name: " + name + " does not exist");
         exit(1);
      }

      if(!(func.type instanceof FunctionType)) {
         System.out.println("Structure with name: " + name + " cannot be invoked");
         exit(1);
      }

      List<Expression> arguments = e.getArguments();
      List<Type> argTypes = this.visitList(arguments); // TODO: make visitList for expressions; return list of evaluated types

      List<Declaration> funcParams = func.type.getParams(); 
      List<Type> paramTypes = new ArrayList<Type>();
      for(Declaration param : funcParams) {
         paramTypes.add(param.getType());
      }

      if(argTypes.size() != paramTypes.size()) {
         System.out.println("Function: " + name + " invocation requires: " + paramTypes.size() + " arguments, but was given: " + argTypes.size());
         exit(1);
      }

      // TODO: this contains call might not work; must check type of each Type in lists
      for(Type t : paramTypes) {
         if(!argTypes.contains(t)) {
            System.out.println("Function: " + name + " invocation given improper arguments");
            exit(1);
         }
      }

      return func.type.getRetType();
   }




}

