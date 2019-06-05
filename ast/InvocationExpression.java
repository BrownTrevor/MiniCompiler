package ast;

import java.util.*;
import cfg.*;
import llvm.*;
import globals.*;

public class InvocationExpression
   extends AbstractExpression
{
   private final String name;
   private final List<Expression> arguments;

   public InvocationExpression(int lineNum, String name,
      List<Expression> arguments)
   {
      super(lineNum);
      this.name = name;
      this.arguments = arguments;
   }

   public String getName() {
      return this.name;
   }

   public List<Expression> getArguments() {
      return this.arguments;
   }

   public Value generateInstructions(CFGNode currentBlock) {
      //call i32 @ackermann(i32 %u107, i32 %u109)

      Symbol functionSymbol = Tables.getFromSymbolTable(this.name);
      Type functionType = functionSymbol.getType();

      if (!(functionType instanceof Function)){
         System.err.println("Error: found a symbol that is not a function");
         System.exit(1);
      }
     
      Function function = (Function) functionType;
      String retString = function.getRetType().llvmType();
      String funcName = function.getName();
      List<String> list = parseExpressionList(function, currentBlock);
      Value reg = null;

      Llvm invoc = null;

      if (retString.equals("void")) {
         invoc = new Call(funcName, retString, list);
      }
      else {
         reg = new Register(retString, currentBlock);
         invoc = new Call(reg.getValue(), funcName, retString, list);
      }
      currentBlock.addInstruction(invoc);
     
      return reg;
   }

   private List<String> parseExpressionList(Function function, CFGNode currentBlock) {
      List<String> paramTypes = new ArrayList<String>();
      for (ast.Declaration param : function.getParams()) {
         paramTypes.add(param.getType().llvmType());
      }

      List<String> list = new ArrayList<String>();

      for (int i = 0; i < this.arguments.size(); i++) {
         Expression e = this.arguments.get(i);
         Value v = e.generateInstructions(currentBlock);

         if(v.getLlvmType() == "null") {
            list.add(paramTypes.get(i) + " " + "null");
         }
         else {
            list.add(v.getLlvmType() + " " + v.getValue());
         }
      }

      return list;
   }
}
