import java.util.*;
import cfg.*;


public class MiniTypeChecker {
   private ArrayList<StructTable> structTables;
   private ArrayList<SymbolTable> symbolTables;
   private int num_dyn_alloc = 0;
   
   public MiniTypeChecker() {
      this.structTables = new ArrayList<StructTable>();
      this.symbolTables = new ArrayList<SymbolTable>();
   }

   public void visit(ast.Program p) {
      this.structTables.add(new StructTable());
      this.visitTypeList(p.getTypes());

      this.symbolTables.add(new SymbolTable());
      this.visitDeclarationList(p.getDecls());

      this.visitFunctionList(p.getFuncs());

      this.visitMainFunction();


      //System.out.println(this.structTables.toString());
      //System.out.println(this.symbolTables.toString());
   }


   /*
    * Types
    */

   private void visitTypeList(List<ast.TypeDeclaration> typeDecls) {
      for(ast.TypeDeclaration typeDecl : typeDecls) {
         this.visitType(typeDecl);
      }
   }

   private Struct visitType(ast.TypeDeclaration typeDecl) {
      Struct newType = new Struct(typeDecl);
      this.addToStructTables(newType.getName(), newType);

      return newType;
   }
   

   /*
    * Declarations
    */
   
   private void visitDeclarationList(List<ast.Declaration> decls) {
      for(ast.Declaration decl : decls) {
         this.visitDeclaration(decl);
      }
   }

   private Symbol visitDeclaration(ast.Declaration decl) {
      Symbol newSymbol = new Symbol(decl);
      this.addToSymbolTables(newSymbol.getName(), newSymbol);

      return newSymbol;
   }


   /*
    * Functions
    */
   
   private void visitFunctionList(List<ast.Function> funcs) {
      for(ast.Function func : funcs) {
         this.visitFunction(func);
      }
   }

   private Symbol visitFunction(ast.Function func) {
      Symbol newSymbol = new Symbol(func.getName(), func);
      this.addToSymbolTables(newSymbol.getName(), newSymbol);

      //Add new symbolTable for this function's scope
      this.symbolTables.add(new SymbolTable());

      this.visitDeclarationList(func.getParams()); // List<Declaration>
      this.visitDeclarationList(func.getLocals()); // List<Declaration>
      System.out.println("visiting function: " + func.getName());
      ast.Type retType = this.visitStatement(func.getBody(), func.getType()); // Statement

      if(!(this.isSameType(retType, func.getType()))) {
         this.error("Expected return type: " + func.getType().toString() + " found: " + retType.toString());
      }

      //Remove the symbolTable for this function's scope
      this.symbolTables.remove(this.symbolTables.size()-1);

      return newSymbol;
   }

   private void visitMainFunction() {
      if(!(this.isInSymbolTables("main"))) {
         this.error("No main function defined");
      }

      if(!(this.getFromSymbolTables("main").getType() instanceof ast.Function)) {
         this.error("No main function defined");
      }

      ast.Function func = (ast.Function)(this.getFromSymbolTables("main").getType());

      if(!(func.getParams().size() == 0)) {
         this.error("Main function cannot have parameters");
      }

      if(!(func.getType() instanceof ast.IntType)) {
         this.error("Main function must return an integer");
      }
   }

   /*
    * Statements
    */

   private ast.Type visitStatement(ast.Statement statement, ast.Type retType) {
      if(statement instanceof ast.AssignmentStatement) {
         return this.visitAssignmentStatement(statement, retType);
      }
      else if(statement instanceof ast.BlockStatement) {
         return this.visitBlockStatement(statement, retType);
      }
      else if(statement instanceof ast.ConditionalStatement) {
         return this.visitConditionalStatement(statement, retType);
      }
      else if(statement instanceof ast.DeleteStatement) {
         return this.visitDeleteStatement(statement, retType);
      }
      else if(statement instanceof ast.InvocationStatement) {
         return this.visitInvocationStatement(statement, retType);
      }
      else if(statement instanceof ast.PrintLnStatement) {
         return this.visitPrintLnStatement(statement, retType);
      }
      else if(statement instanceof ast.PrintStatement) {
         return this.visitPrintStatement(statement, retType);
      }
      else if(statement instanceof ast.ReturnEmptyStatement) {
         return this.visitReturnEmptyStatement(statement, retType);
      }
      else if(statement instanceof ast.ReturnStatement) {
         return this.visitReturnStatement(statement, retType);
      }
      else if(statement instanceof ast.WhileStatement) {
         return this.visitWhileStatement(statement, retType);
      }

      return new ast.VoidType();
   }

   /*
    * Rules:
    * 1. variable to be assigned must be in scope
    * 2. variable's type must equal type of assignment expression
    * 3. don't do anything with retType
    * TODO: not sure this works; work on LvalueDot
    */
   private ast.Type visitAssignmentStatement(ast.Statement statement, ast.Type retType) {
      //System.out.println("asgn st");

      ast.AssignmentStatement asgnStatement = (ast.AssignmentStatement)statement;
      ast.Lvalue target = asgnStatement.getTarget();
      ast.Expression source = asgnStatement.getSource();
      ast.Type sourceType = this.visitExpression(source);

      if(target instanceof ast.LvalueId) {
         String id = ((ast.LvalueId)target).getId();
         Symbol targetSymbol = this.getFromSymbolTables(id);
         ast.Type targetType = targetSymbol.getType();

         if(!(this.isSameType(targetType, sourceType))) {
            this.error("Cannot assign source of type: " + sourceType.toString() + " to target of type: " + targetType.toString());
         }
      }
      else if(target instanceof ast.LvalueDot) {
         String id = ((ast.LvalueDot)target).getId();
         ast.Expression left = ((ast.LvalueDot)target).getLeft();
         
         if(!(left instanceof ast.DotExpression)) {
            this.error("Cannot have an LvalueDot without a DotExpression");
         }

         ast.Type leftType = this.visitExpression(left);

         if(!(leftType instanceof ast.StructType)) {
            this.error("Cannot have an LvalueDot with a non-struct Lvalue");
         }

         ast.StructType leftStruct = (ast.StructType)leftType;

         if(!(this.isInStructTables(leftStruct.getName()))) {
            this.error("Cannot find struct with name: " + leftStruct.getName());
         }

         Struct struct = this.getFromStructTables(leftStruct.getName());
         ast.Type targetType = struct.getFieldType(id);
         
         if(!(this.isSameType(targetType, sourceType))) {
            this.error("Cannot assign source of type: " + sourceType.toString() + " to target of type: " + leftType.toString());
         }
      }

      //return sourceType;
      
      return new ast.VoidType();
   }

   /*
    * Rules:
    * 1. visit each statement
    * 2. verify retType
    */
   private ast.Type visitBlockStatement(ast.Statement statement, ast.Type retType) {
      ast.Type actualRetType = new ast.VoidType();

      for(ast.Statement subStatement : ((ast.BlockStatement)statement).getStatements()) {
         actualRetType = this.visitStatement(subStatement, retType);
      }

      return actualRetType;
   }

   /*
    * Rules:
    * 1. guard must evaluate to booltype
    * 2. valid returns:
    *    a. then and else are both VoidType
    *    b. then and else are both retType
    */
   private ast.Type visitConditionalStatement(ast.Statement statement, ast.Type retType) {
      //System.out.println("cond st");
   
      ast.ConditionalStatement condStatement = (ast.ConditionalStatement)statement;
      ast.Expression guard = condStatement.getGuard();
      ast.Statement thenBlock = condStatement.getThenBlock();
      ast.Statement elseBlock = condStatement.getElseBlock();

      if(!(this.visitExpression(guard) instanceof ast.BoolType)) {
         this.error("Invalid conditional statement: guard must evaluate to a boolean");
      }
      
      ast.Type thenRetType = this.visitStatement(thenBlock, retType);
      ast.Type elseRetType = this.visitStatement(elseBlock, retType);

      if((this.isSameType(thenRetType, elseRetType)) &&
         ((thenRetType instanceof ast.VoidType) || (this.isSameType(thenRetType, retType)))) {
         return thenRetType;
      }
      else {
         this.error("Expected return type: " + retType.toString() + "\n" +
                    "Found return type: " + thenRetType.toString() + " in then block " + "\n" +
                    "Found return type: " + elseRetType.toString() + " in else block"
                    );
      }

      return new ast.VoidType();
   }


   /*
    * Rules:
    * TODO: does this delete from symtable or structable? pretty sure it's symbol
    * 1. expression must be an allocated structure
    * 2. removes structure from scope
    */
   private ast.Type visitDeleteStatement(ast.Statement statement, ast.Type retType) {
      //System.out.println("del st");

      ast.DeleteStatement deleteStatement = (ast.DeleteStatement)statement;
      ast.Expression exp = deleteStatement.getExpression();
      ast.Type reference = this.visitExpression(exp);

      if(!(reference instanceof ast.StructType)) {
         this.error("Can only delete references to structs");
      }

      ast.StructType structReference = (ast.StructType)reference;

      if(!(this.isInSymbolTables(structReference.getName()))) {
         this.error("Cannot delete reference with name: " + structReference.getName() + " because it does not exist or is not in scope");
      }

      this.removeFromSymbolTables(structReference.getName());

      return new ast.VoidType();
   }


   /*
    * Rules:
    * 1. must have InvocationExpression; typecheck InvocationExpression
    * 2. return type function evaluates to
    */
   private ast.Type visitInvocationStatement(ast.Statement statement, ast.Type retType) {
      //System.out.println("invo st");

      ast.InvocationStatement invocationStatement = (ast.InvocationStatement)statement;
      ast.Expression exp = invocationStatement.getExpression();

      if(!(exp instanceof ast.InvocationExpression)) {
         this.error("Cannot invoke statement that doesn't contain an invocation expression");
      }

      return this.visitExpression(exp);
   }

   /*
    * Rules:
    * 1. requires integer argument
    * 2. print followed by newline
    */
   private ast.Type visitPrintLnStatement(ast.Statement statement, ast.Type retType) {
      //System.out.println("println st");

      ast.PrintLnStatement printLnStatement = (ast.PrintLnStatement)statement;
      ast.Expression exp = printLnStatement.getExpression();
      ast.Type result = this.visitExpression(exp);

      if(!(result instanceof ast.IntType)) {
         this.error("Cannot print expression that doesn't result in an integer");
      }

      return new ast.VoidType();
   }

   /*
    * Rules:
    * 1. requires integer argument
    * 2. print followed by space
    */
   private ast.Type visitPrintStatement(ast.Statement statement, ast.Type retType) {
      //System.out.println("print st");

      ast.PrintStatement printStatement = (ast.PrintStatement)statement;
      ast.Expression exp = printStatement.getExpression();
      ast.Type result = this.visitExpression(exp);

      if(!(result instanceof ast.IntType)) {
         this.error("Cannot print expression that doesn't result in an integer");
      }

      return new ast.VoidType();
   }

   /*
    * Rules:
    * 1. ?
    */
   private ast.Type visitReturnEmptyStatement(ast.Statement statement, ast.Type retType) {
      //System.out.println("empty st");
      if(!(retType instanceof ast.VoidType)) {
         this.error("Missing return statement of type: " + retType.toString());
      }

      return retType;
   }

   /*
    * Rules:
    * 1. if expression is not null, then evaluate expression type
    * 2. verify expression type is equal to retType
    * 3. if expression is null, return/verify void type
    */
   private ast.Type visitReturnStatement(ast.Statement statement, ast.Type retType) {
      //System.out.println("ret st");

      ast.ReturnStatement returnStatement = (ast.ReturnStatement)statement;
      ast.Expression exp = returnStatement.getExpression();

      if(exp instanceof ast.NullExpression) {
         if(!(retType instanceof ast.VoidType)) {
            this.error("Missing return statement of type: " + retType.toString());
         }
      }
      else {
         ast.Type result = this.visitExpression(exp);
         if(!(this.isSameType(result, retType))) {
            this.error("Requires return of type: " + retType.toString() + " found: " + result.toString());
         }
      }

      return retType;
   }

   /*
    * Rules:
    * 1. require boolean guard; verify expression evaluates to boolean
    * 2. type check body
    */
   private ast.Type visitWhileStatement(ast.Statement statement, ast.Type retType) {
      //System.out.println("while st");

      ast.WhileStatement whileStatement = (ast.WhileStatement)statement;
      ast.Expression guard = whileStatement.getGuard();
      ast.Statement body = whileStatement.getBody();

      if(!(this.visitExpression(guard) instanceof ast.BoolType)) {
         this.error("Invalid while statement: guard must evaluate to a boolean");
      }

      this.visitStatement(body, retType);

      return new ast.VoidType();
   }

   /*
    * Expressions
    */

   private ast.Type visitExpression(ast.Expression exp) {
      if(exp instanceof ast.BinaryExpression) {
         return this.visitBinaryExpression(exp);
      }
      else if(exp instanceof ast.DotExpression) {
         return this.visitDotExpression(exp);
      }
      else if(exp instanceof ast.FalseExpression) {
         return this.visitFalseExpression(exp);
      }
      else if(exp instanceof ast.IdentifierExpression) {
         return this.visitIdentifierExpression(exp);
      }
      else if(exp instanceof ast.IntegerExpression) {
         return this.visitIntegerExpression(exp);
      }
      else if(exp instanceof ast.InvocationExpression) {
         return this.visitInvocationExpression(exp);
      }
      else if(exp instanceof ast.NewExpression) {
         return this.visitNewExpression(exp);
      }
      else if(exp instanceof ast.NullExpression) {
         return this.visitNullExpression(exp);
      }
      else if(exp instanceof ast.ReadExpression) {
         return this.visitReadExpression(exp);
      }
      else if(exp instanceof ast.TrueExpression) {
         return this.visitTrueExpression(exp);
      }
      else if(exp instanceof ast.UnaryExpression) {
         return this.visitUnaryExpression(exp);
      }

      return new ast.VoidType();
   }

   /*
    * Rules:
    * 1. arithmetic and relational operators must have integer operands
    * 2. equality operators must have integer or structure operands of matching type
    * 3. boolean operators must have boolean operands
    * 4. boolean operators do not short circuit
    */
   private ast.Type visitBinaryExpression(ast.Expression exp) {
      ast.BinaryExpression binaryExp = (ast.BinaryExpression)exp;

      ast.BinaryExpression.Operator op = binaryExp.getOperator();
      ast.Expression left = binaryExp.getLeft();
      ast.Expression right = binaryExp.getRight();
      ast.Type typeLeft = this.visitExpression(left);
      ast.Type typeRight = this.visitExpression(right);


      // arithmetic {*, /, +, -}
      if((op == ast.BinaryExpression.Operator.TIMES) || (op == ast.BinaryExpression.Operator.DIVIDE) ||
         (op == ast.BinaryExpression.Operator.PLUS) || (op == ast.BinaryExpression.Operator.MINUS)) {
         if(!((typeLeft instanceof ast.IntType) && (typeRight instanceof ast.IntType))) {
            this.error("Invalid arithmetic expression: requires integer operands");
         }

         return new ast.IntType();
      }
      // relational {<, >, <=, >=}
      else if((op == ast.BinaryExpression.Operator.LT) || (op == ast.BinaryExpression.Operator.GT) ||
         (op == ast.BinaryExpression.Operator.LE) || (op == ast.BinaryExpression.Operator.GE)) {
         if(!((typeLeft instanceof ast.IntType) && (typeRight instanceof ast.IntType))) {
            this.error("Invalid relational expression: requires integer operands");
         }
         
         return new ast.BoolType();
      }
      // equality {==, !=}
      else if((op == ast.BinaryExpression.Operator.EQ) || (op == ast.BinaryExpression.Operator.NE)) {
         if(!(((typeLeft instanceof ast.IntType) && (typeRight instanceof ast.IntType)) ||
            ((typeLeft instanceof ast.StructType) && (typeRight instanceof ast.StructType)))) {
            this.error("Invalid equality expression: requires both integer or both structure operands");
         }
         else {
            //if structs, verify they are in scope
            if(typeLeft instanceof ast.StructType) {
               ast.StructType structLeft = (ast.StructType)typeLeft;
               ast.StructType structRight = (ast.StructType)typeRight;

               if(!(this.isInStructTables(structLeft.getName()) && this.isInStructTables(structRight.getName()))) {
                  this.error("Invalid equality expression: struct(s) referenced do not exist");
               }
            }
         }

         return new ast.BoolType();
      }
      // boolean {&&, ||}
      else if((op == ast.BinaryExpression.Operator.AND) || (op == ast.BinaryExpression.Operator.OR)) {
         if(!((typeLeft instanceof ast.BoolType) && (typeRight instanceof ast.BoolType))) {
            this.error("Invalid boolean expression: requires boolean operands");
         }

         return new ast.BoolType();
      }

      return new ast.VoidType();
   }

   /*
    * Rules:
    * 1. ?
    */
   private ast.Type visitDotExpression(ast.Expression exp) {
      ast.DotExpression dotExp = (ast.DotExpression)exp;
      String id = dotExp.getId();
      ast.Expression left = dotExp.getLeft();

      ast.Type leftType = this.visitExpression(left);

      if(!(leftType instanceof ast.StructType)) {
         this.error("Cannot have an LvalueDot with a non-struct Lvalue");
      }

      ast.StructType leftStruct = (ast.StructType)leftType;

      if(!(this.isInStructTables(leftStruct.getName()))) {
         this.error("Cannot find struct with name: " + leftStruct.getName());
      }

      Struct struct = this.getFromStructTables(leftStruct.getName());
      ast.Type targetType = struct.getFieldType(id);
         
      return targetType;
   }

   private ast.Type visitFalseExpression(ast.Expression exp) {
      return new ast.BoolType();
   }

   /*
    * Rules:
    * 1. must be in symbol tables
    */
   private ast.Type visitIdentifierExpression(ast.Expression exp) {
      ast.IdentifierExpression identifierExp = (ast.IdentifierExpression)exp;
      String id = identifierExp.getId();

      if(!(this.isInSymbolTables(id))) {
         this.error("Cannot evaluate identifier: " + id + " because it has not been instantiated");
      }

      Symbol symbol = this.getFromSymbolTables(id);
      return symbol.getType();
   }

   private ast.Type visitIntegerExpression(ast.Expression exp) {
      ast.IntegerExpression integerExp = (ast.IntegerExpression)exp;
      String value = integerExp.getValue();

      return new ast.IntType();
   }

   /*
    * Rules:
    * 1. expression must contain id (valid function in scope) and arguments
    * 2. arguments must match required for function
    */
   private ast.Type visitInvocationExpression(ast.Expression exp) {
      ast.InvocationExpression invocationExp = (ast.InvocationExpression)exp;

      String name = invocationExp.getName();
      List<ast.Expression> arguments = invocationExp.getArguments();

      if(!(this.isInSymbolTables(name))) {
         this.error("Cannot invoke function: " + name + " because it has not been instantiated or is not in scope");
      }
      
      Symbol symbol = this.getFromSymbolTables(name);

      if(!(symbol.getType() instanceof ast.Function)) {
         this.error("Cannot invoke: " + name + " because it is not a function");
      }

      ast.Function func = (ast.Function)(symbol.getType());
      List<ast.Declaration> params = func.getParams();

      if(!(arguments.size() == params.size())) {
         this.error("Cannot invoke function: " + name + ". Expected: " + params.size() + " arguments. Found: " + arguments.size());
      }

      int numNullArguments = 0;
      Set<ast.Type> argumentTypes = new HashSet<ast.Type>();
      for(ast.Expression argument : arguments) {
         ast.Type typeArgument = this.visitExpression(argument);
         if(typeArgument instanceof ast.NullType) {
            numNullArguments += 1;
         }
         argumentTypes.add(typeArgument);
      }

      List<ast.Type> paramTypes = new ArrayList<ast.Type>();
      for(ast.Declaration param : params) {
         paramTypes.add(param.getType());
      }

      /*
      System.out.println("---- about to check args/params ----");
      System.out.println("function name: " + name);
      System.out.println("----params----");
      System.out.println(paramTypes);
      System.out.println("----args----");
      System.out.println(argumentTypes);
      */

      for(int i = 0; i < paramTypes.size(); i++) {
         boolean foundMatch = false;
         
         Iterator<ast.Type> argumentTypesIterator = argumentTypes.iterator();
         while(argumentTypesIterator.hasNext()) {
            ast.Type nextArgumentType = argumentTypesIterator.next();

            if(this.isSameType(paramTypes.get(i), nextArgumentType)) {
               foundMatch = true;
               argumentTypes.remove(nextArgumentType);
               break;
            }
         }

         if(!foundMatch) {
            if(numNullArguments > 0) {
               numNullArguments -= 1;
               continue;
            }
            else {
               this.error("Cannot invoke function: " + name + invocationExp.toString() +
                          " because expected parameter: " + paramTypes.get(i).toString() +
                          " was not found in arguments");
            }
         }
      }

      return func.getType();
   }

   /*
    * Rules:
    * 1. id must be name of struct
    * 2. add new struct instance to symbol table
    * 3. return type of struct
    */
   private ast.Type visitNewExpression(ast.Expression exp) {
      ast.NewExpression newExp = (ast.NewExpression)exp;
      String id = newExp.getId();

      if(!(this.isInStructTables(id))) {
         this.error("Cannot dynamically allocate a new structure with id: " + id + " because structure does not exist or is not in scope");
      }
      
      ast.StructType newStruct = new ast.StructType(newExp.getLineNum(), id);
      this.num_dyn_alloc += 1;

      Symbol newSymbol = new Symbol(String.valueOf(this.num_dyn_alloc), newStruct);
      this.addToSymbolTables(String.valueOf(this.num_dyn_alloc), newSymbol);

      return newStruct;
   }

   private ast.Type visitNullExpression(ast.Expression exp) {
      return new ast.NullType();
   }

   /*
    * Rules:
    * 1. evaluates to integer
    */
   private ast.Type visitReadExpression(ast.Expression exp) {
      return new ast.IntType();
   }

   private ast.Type visitTrueExpression(ast.Expression exp) {
      return new ast.BoolType();
   }

   /*
    * Rules:
    * 1. arithmetic operator must have integer operand
    * 2. boolean operator must have boolean operand
    */
   private ast.Type visitUnaryExpression(ast.Expression exp) {
      ast.UnaryExpression unaryExp = (ast.UnaryExpression)exp;
      ast.UnaryExpression.Operator op = unaryExp.getOperator();
      ast.Expression operand = unaryExp.getOperand();
      ast.Type typeOperand = this.visitExpression(operand);

      // arithmetic {-}
      if(op == ast.UnaryExpression.Operator.MINUS) {
         if(!(typeOperand instanceof ast.IntType)) {
            this.error("Invalid arithmetic expression: requires integer operand");
         }

         return new ast.IntType();
      }
      // boolean {!}
      else if(op == ast.UnaryExpression.Operator.NOT) {
         if(!(typeOperand instanceof ast.BoolType)) {
            this.error("Invalid boolean expression: requires boolean operand");
         }

         return new ast.BoolType();
      }

      return new ast.VoidType();
   }


   /*
   * Tools
   */

   private static void error(String msg)
   {
      System.err.println(msg);
      System.exit(1);
   }

   // Adds structName and structInstance to the current/top struct table
   private void addToStructTables(String structName, Struct structInstance) {
      this.structTables.get(this.structTables.size()-1).addStruct(structName, structInstance);
   }

   // Retrieves the struct with name structName at the most recent scope
   private Struct getFromStructTables(String structName) {
      Struct topStruct = null;

      for(StructTable table : this.structTables) {
         if(table.containsStruct(structName)) {
            topStruct = table.getStruct(structName);
         }
      }

      if(topStruct == null) {
         this.error("Cannot retrieve struct: " + structName + " because it does not exist");
      }

      return topStruct;
   }

   // true if in struct tables; false otherwise
   private boolean isInStructTables(String structName) {
      for(StructTable table : this.structTables) {
         if(table.containsStruct(structName)) {
            return true; 
         }
      }

      return false;
   }

   // Adds symbolName and symbolInstance to current/top symbol table
   private void addToSymbolTables(String symbolName, Symbol symbolInstance) {
      this.symbolTables.get(this.symbolTables.size()-1).addSymbol(symbolName, symbolInstance);
   }

   // Retrieves the symbol with name symbolName at the most recent scope
   private Symbol getFromSymbolTables(String symbolName) {
      Symbol topSymbol = null;

      for(SymbolTable table : this.symbolTables) {
         if(table.containsSymbol(symbolName)) {
            topSymbol = table.getSymbol(symbolName);
         }
      }

      if(topSymbol == null) {
         this.error("Cannot retrieve symbol: " + symbolName + " because it does not exist");
      }

      return topSymbol;
   }

   // true if in symbol tables; false otherwise
   private boolean isInSymbolTables(String symbolName) {
      for(SymbolTable table : this.symbolTables) {
         if(table.containsSymbol(symbolName)) {
            return true;
         }
      }
      return false;
   }

   // removes most recent (highest scope) symbol matching symbolName from symbolTables
   private void removeFromSymbolTables(String symbolName) {
      for(int i = this.symbolTables.size()-1; i >= 0; i--) {
         SymbolTable table = this.symbolTables.get(i);
         if(table.containsSymbol(symbolName)) {
            table.removeSymbol(symbolName);
            return;
         }
      }
   }

   // true if two ast.Type have same instanceof class
   // TODO: verify this works
   private boolean isSameType(ast.Type one, ast.Type two) {
      return (one.getClass() == two.getClass());
   }
}

