package ast;

import cfg.*;


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
      Value operandVal = this.operand.generateInstructions(currentBlock);
      String instruction = Register.newRegister() + " = ";

      // which operator is this?
      if(this.operator == Operator.NOT) {
         instruction += ("xor " + operandVal.getLlvmType() + " " + 
            operand.getRegister() + ", " + operandVal.getRegister());
      }
      else if(this.operator == Operator.MINUS){
         instruction += ("sub " + operandVal.getLlvmType() + " 0, " +
            operandVal.getRegister());
      }
      else {
         System.err.println("Error: operator not defined");
         System.exit(1);
      }      

      return new Value(operandVal.getLlvmType(), Register.getRegister());
   }
}
