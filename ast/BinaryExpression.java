package ast;

import cfg.*;
import llvm.*;

public class BinaryExpression
   extends AbstractExpression
{
   private final Operator operator;
   private final Expression left;
   private final Expression right;

   private BinaryExpression(int lineNum, Operator operator,
      Expression left, Expression right)
   {
      super(lineNum);
      this.operator = operator;
      this.left = left;
      this.right = right;
   }

   public Operator getOperator() {
      return this.operator;
   }

   public Expression getLeft() {
      return this.left;
   }

   public Expression getRight() {
      return this.right;
   }

   public static BinaryExpression create(int lineNum, String opStr,
      Expression left, Expression right)
   {
      switch (opStr)
      {
         case TIMES_OPERATOR:
            return new BinaryExpression(lineNum, Operator.TIMES, left, right);
         case DIVIDE_OPERATOR:
            return new BinaryExpression(lineNum, Operator.DIVIDE, left, right);
         case PLUS_OPERATOR:
            return new BinaryExpression(lineNum, Operator.PLUS, left, right);
         case MINUS_OPERATOR:
            return new BinaryExpression(lineNum, Operator.MINUS, left, right);
         case LT_OPERATOR:
            return new BinaryExpression(lineNum, Operator.LT, left, right);
         case LE_OPERATOR:
            return new BinaryExpression(lineNum, Operator.LE, left, right);
         case GT_OPERATOR:
            return new BinaryExpression(lineNum, Operator.GT, left, right);
         case GE_OPERATOR:
            return new BinaryExpression(lineNum, Operator.GE, left, right);
         case EQ_OPERATOR:
            return new BinaryExpression(lineNum, Operator.EQ, left, right);
         case NE_OPERATOR:
            return new BinaryExpression(lineNum, Operator.NE, left, right);
         case AND_OPERATOR:
            return new BinaryExpression(lineNum, Operator.AND, left, right);
         case OR_OPERATOR:
            return new BinaryExpression(lineNum, Operator.OR, left, right);
         default:
            throw new IllegalArgumentException();
      }
   }

   private static final String TIMES_OPERATOR = "*";
   private static final String DIVIDE_OPERATOR = "/";
   private static final String PLUS_OPERATOR = "+";
   private static final String MINUS_OPERATOR = "-";
   private static final String LT_OPERATOR = "<";
   private static final String LE_OPERATOR = "<=";
   private static final String GT_OPERATOR = ">";
   private static final String GE_OPERATOR = ">=";
   private static final String EQ_OPERATOR = "==";
   private static final String NE_OPERATOR = "!=";
   private static final String AND_OPERATOR = "&&";
   private static final String OR_OPERATOR = "||";

   public static enum Operator
   {
      TIMES, DIVIDE, PLUS, MINUS, LT, GT, LE, GE, EQ, NE, AND, OR
   }

   public Value generateInstructions(CFGNode currentBlock) {
      Value rightVal = this.right.generateInstructions(currentBlock);
      Value leftVal = this.left.generateInstructions(currentBlock);

      String type = rightVal.getLlvmType();
      Value register = new Register(type);
      String llvmOp = this.operatorToLLVM();
      String reg = register.getValue();
      String op1 = rightVal.getValue();
      String op2 = leftVal.getValue();
      Llvm instruction = null;

      switch (llvmOp) {
         case "add":
            instruction = new Add(reg, type, op1, op2);
            break;
         case "sub":
            instruction = new Sub(reg, type, op1, op2);
            break;
         case "mul":
            instruction = new Mul(reg, type, op1, op2);
            break;
         case "sdiv":
            instruction = new Sdiv(reg, type, op1, op2);
            break;
         case "and":
            instruction = new And(reg, type, op1, op2);
            break;
         case "or":
            instruction = new Or(reg, type, op1, op2);
            break;
         case "imcp slt":
            instruction = new Icmp(reg, "slt",type, op1, op2);
            break; 
         case "imcp sle":
            instruction = new Icmp(reg, "sle", type, op1, op2);
            break;
         case "imcp sgt":
            instruction = new Icmp(reg, "sgt", type, op1, op2);
            break;
         case "imcp sge":
            instruction = new Icmp(reg, "sge", type, op1, op2);
            break;
         case "imcp eq":
            instruction = new Icmp(reg, "eq", type, op1, op2);
            break;
         case "imcp ne":
            instruction = new Icmp(reg, "ne", type, op1, op2);
            break;
         default: 
            System.err.println("Error: Operator mismatch");
            System.exit(1);
      }

      currentBlock.addInstruction(instruction);

      return register;
   }


   public String operatorToLLVM () {
      switch (this.operator) {
         case TIMES:
            return "mul";
         case DIVIDE:
            return "sdiv";
         case PLUS:
            return "add";
         case MINUS:
            return "sub";
         case LT:
            return "imcp slt";
         case LE:
            return "imcp sle";
         case GT:
            return "imcp sgt";
         case GE:
            return "imcp sge";
         case EQ:
            return "imcp eq";
         case NE:
            return "imcp ne";
         case AND:
            return "and";
         case OR:
            return "or";
         default:
            System.err.println("Error: Operator mismatch");
            System.exit(1);
      }
      return null;
   }
}
