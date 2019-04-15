import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.List;

import ast.*;
import ast.UnaryExpression.Operator;
import ast.BinaryExpression.Operator;

public class TypeCheckVisitor implements Visitor 
{

   ArrayList<SymbolTable> symTables = new ArrayList<SymbolTable>(); 
   ArrayList<StructTable> structTables = new ArrayList<StructTable>(); 


   public void visit(Program x)
   {
      symTables.add(new SymbolTable());
      structTables.add(new StructTable());

      this.visitList(x.getTypes());
      this.visitList(x.getDecls());
      this.visitList(x.getFuncs());

      if(this.getFromSymTables("main") == null) {
         System.out.println("Must contain main function");
         exit(1);
      }
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

      this.assertType(this.visit(source), this.visit(target));
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
   
   /*
    * Logic: 
    * 1. Visit the expression and ensure that it results in a struct type
    * 2. Remove struct from struct table
    */
   public void visit(DeleteStatement x) {
      Expression exp = x.getExpression();
      Type t = this.visit(exp);

      if (t instanceof StructType) {
         structTables.removeStructFromTables(t.getName());
      }
      return this.visit(exp);
   }

   /*
    * Logic: 
    * 1. Visit the expression 
    * 2. Return the type of the expression for the function to check
    */
   public Type visit(ReturnStatement x) {
      Expression exp = x.getExpression();
      // Need to talk about this one. It might force us to return NullType on statements 
      return this.visit(exp);
   }

   /*
    * Logic:
    * 1. name must exist as a function in scope
    * 2. arguments must be of proper number and type for function invoked
    * 3. return the return type of the function
    */
   public Type visit(InvocationStatement x) {
      Expression e = x.getExpression();

      if(!(e instanceof InvocationExpression)) {
         System.out.println("Invocation of function must contain an invocation expression");
         System.exit(1);
      }

      String name = e.getName();
      Symbol func = this.getFromSymTables(name);

      if(func == null) {
         System.out.println("Function with name: " + name + " does not exist");
         System.exit(1);
      }

      if(!(func.type instanceof FunctionType)) {
         System.out.println("Structure with name: " + name + " cannot be invoked");
         System.exit(1);
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
         System.exit(1);
      }

      // TODO: this contains call might not work; must check type of each Type in lists
      for(Type t : paramTypes) {
         if(!argTypes.contains(t)) {
            System.out.println("Function: " + name + " invocation given improper arguments");
            System.exit(1);
         }
      }

      return func.type.getRetType();
   }

   public Type visit(LvalueId x) {
      //Looks up the id of LvalueId in the symbol table
      return getSymbolFromTables(x.getId()).getType();
   }

   /*
    * Logic: 
    * 1. Get a structtype fromt he left side of the dot 
    * 2. Lookup Struct int the struct table
    * 3. Lookup the right side of the dot in the struct type
    * 4. Return its type
    */
   public Type visit(LvalueDot x)
   {
      Type leftType = this.visit(x.getLeft());
      assertType(new StructType(-1, "test"), leftType);
      Struct struct = getStructFromTables(leftType.getName());
      
      return struct.getField(x.getId());

   }


   public Type visit(BinaryExpression x)  
   {
      Type left = this.visit(x.getLeftExpression());
      Type right = this.visit(x.getRightExpression());
      Operator o = x.getOperator();
      
      switch (o) {
         case BinaryExpression.Operator.TIMES: 
            assertType(new IntType(), left);
            assertType(new IntType(), right);
            return right;
            break;
         case BinaryExpression.Operator.DIVIDE:
            assertType(new IntType(), left);
            assertType(new IntType(), right);
            return right;
            break;
         case BinaryExpression.Operator.PLUS:
            assertType(new IntType(), left);
            assertType(new IntType(), right);
            return right;
            break;
         case BinaryExpression.Operator.MINUS: 
            assertType(new IntType(), left);
            assertType(new IntType(), right);
            return right;
            break;
         case BinaryExpression.Operator.LT:
            assertType(new IntType(), left);
            assertType(new IntType(), right);
            return right;
            break;
         case BinaryExpression.Operator.GT:
            assertType(new IntType(), left);
            assertType(new IntType(), right);
            return right;
            break;
         case BinaryExpression.Operator.LE:
            assertType(new IntType(), left);
            assertType(new IntType(), right);
            return right;
            break;
         case BinaryExpression.Operator.GE:
            assertType(new IntType(), left);
            assertType(new IntType(), right);
            return right;
            break;
         case BinaryExpression.Operator.EQ:
            assertType(left, right);
            return right;
            break;
         case BinaryExpression.Operator.NE:
            assertType(left, right);
            return right;
            break;
         case BinaryExpression.Operator.AND: 
            assertType(new BoolType(), left);
            assertType(new BoolType(), right);
            return right;
            break;
         case BinaryExpression.Operator.OR:
            assertType(new BoolType(), left);
            assertType(new BoolType(), right);
            return right;
            break;
         default:
            System.err.println("Error: Not a binary operator.");
            System.exit(1);
      }
   }

   public Type visit(FalseExpression x) {
      return new BoolType();
   }

   public Type visit(IdentifierExpression x) {
      Symbol s = getFromSymTables(x.getId());
      return s.getType();
   }

   public Type visit(IntegerExpression x) {
      return new IntType();
   }


   /*
    * Logic: 
    * 1. Lookup the innvocation name in the symbol table and make sure it is
    *    of type function.
    * 2. Loop over the argument types in the symbol table and the argument types 
    *    in the invocation asserting that they are of the same type
    * 3. Return the return type
    */
   public Type visit(InvocationExpression x) {
      Symbol funcSymbol = getFromSymTables(x.getName());
      assertType(new FunctionType(null, null, null), funcSymbol.getType());
      FunctionType funcDecl = funcSymbol.getType();

      assertTypeLists(this.expListToTypeList(x.getArguments()), 
         this.declListToTypeList(funcDecl.getParams()));

      return funcDecl.getRetType();
   }

   /*
    * Logic: 
    * 1. Lookup struct in the table and assert it exists
    * 2. Return a new StructType from what was found in the struct table
    */
   public Type visit(NewExpression x) {
      return new StructType(getStructFromTables(x.getId()));
   }

   /*
    * Logic: 
    * 1. Return new NullType
    */
   public Type visit(NullExpression x) {
      return new NullType();
   }

   /*
    * Logic: 
    * 1. Return new IntType
    */
   public Type visit(ReadExpression x) {
      return new IntType();
   }

   /*
    * Logic: 
    * 1. Return new BoolType
    */
   public Type visit(TrueExpression x) {
      return new BoolType();
   }

   /*
    * Logic: 
    * 1. Visit operand and get its type
    * 2. Match type based off unary cases
    */
   public Type visit(UnaryExpression x) {
      Type operandType = this.visit(x.getOperand());
      if (x.getOperator() == UnaryExpression.Operator.NOT) {
         assertType(new BoolType(), operandType);
         return operandType;
      }
      else if (x.getOperator() == UnaryExpression.Operator.MINUS) {
         assertType(new IntType(), operandType);
         return operandType;
      }
      
      System.err.println("Error: Not a unary operator.");
      System.exit(1);
   }



   private Symbol getSymbolFromTables(String id) {
      for (int i = symTables.size()-1; i >= 0; i--) {
         Symbol symbol = symTables.get(i).getSymbol(id);
         if(symbol != null) {
            return symbol;
         }
      }

      System.err.println("Error: Identifier " + id + " not found in symbol table.");
      System.exit(1);
   }

   private Struct getStructFromTables(String id) {
      Struct s = structTables.get(structTables.size()-1).getStruct(id);
      if (s != null) {
         return s;
      }

      System.err.println("Error: Struct " + id + " not found in struct table.");
      System.exit(1);
   }

   private Struct removeStructFromTables(String id) {
      structTables.get(structTables.size()-1).removeStruct(id);

      System.err.println("Error: Struct " + id + " not found in struct table.");
      System.exit(1);
   }

   private void assertType(Type expected, Type actual) {
      if(actual.getClass().equals(expected.getClass())) {
         System.err.println("Error: Type Mismatch");
      }
   }

   private void visitList(List<Declaration> lx) {
      for (Declaration d : lx) {
         this.visit(d);
      }
   }

   private List<Type> expListToTypeList(List<Expression> list) {
      List<Type> typeList = new ArrayList<Type>();

      for (Expression e : list) {
         typeList.add(this.visit(e));
      }

      return typeList;
   }

   private List<Type> declListToTypeList(List<Declaration> list) {
      List<Type> typeList = new ArrayList<Type>();

      for (Declaration d : list) {
         typeList.add(d.getType());
      }

      return typeList;
   }

   private void assertTypeLists(List<Type> l1, List<Type> l2) {
      Iterator it1 = l1.iterator();
      Iterator it2 = l2.iterator();
      while (it1.hasNext() && it2.hasNext()) {
         Type expected = (Type)it1.next();
         Type actual = (Type)it2.next();
         assertType(expected, actual);
      }
   }

}

