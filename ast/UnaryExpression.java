package ast;

import cfg.*;
import llvm.*;

public class UnaryExpression
   extends AbstractExpression
{
   private final Operator operator;
   private final Expression operand;

   private UnaryExpression(int lineNum, Operator operator, Expression operand)
   {
      super(lineNum);
      this.operator = operator;
      this.operand = operand;
   }

   public Operator getOperator() {
      return this.operator;
   }

   public Expression getOperand() {
      return this.operand;
   }

   public static UnaryExpression create(int lineNum, String opStr,
      Expression operand)
   {
      if (opStr.equals(NOT_OPERATOR))
      {
         return new UnaryExpression(lineNum, Operator.NOT, operand);
      }
      else if (opStr.equals(MINUS_OPERATOR))
      {
         return new UnaryExpression(lineNum, Operator.MINUS, operand);
      }
      else
      {
         throw new IllegalArgumentException();
      }
   }

   private static final String NOT_OPERATOR = "!";
   private static final String MINUS_OPERATOR = "-";

   public static enum Operator
   {
      NOT, MINUS
   }

   public Value generateInstructions(CFGNode currentBlock) { 
      Value operVal = this.operand.generateInstructions(currentBlock);
      
      String operName = operVal.getValue();
      String operType = operVal.getLlvmType();


      // which operator is this?
      if(this.operator == Operator.NOT)  { 
         Value truncReg = truncResult(currentBlock, operVal);
         Value xorReg = new Register(operType, currentBlock);
         Llvm instruction = new Xor(xorReg.getValue(), "i1", truncReg.getValue(), "1");
         currentBlock.addInstruction(instruction);
         Value zextReg = zextResult(currentBlock, xorReg);
         return zextReg;
      }
      else if(this.operator == Operator.MINUS){
         Value reg = new Register(operType, currentBlock);
         Llvm instruction = new Sub(reg.getValue(), operType, "0", operName);
         currentBlock.addInstruction(instruction);
         return reg;
      }
      else {
         System.err.println("Error: operator not defined");
         System.exit(1);
         return null;
      }      
   }

   private Value zextResult(CFGNode current, Value v) {
      Value result = new Register("i32", current);

      Llvm zext = new Zext(result.getValue(), "i1", v.getValue(), "i32");
      current.addInstruction(zext);

      return result;
   }

   private Value truncResult(CFGNode current, Value v) {
      Value result = new Register("i1", current);

      Llvm trunc = new Trunc(result.getValue(), "i32", v.getValue(), "i1");
      current.addInstruction(trunc);

      return result;
   }
}
