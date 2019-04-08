import ast.TypeDeclaration;
import ast.WhileStatement;
import jdk.jshell.TypeDeclSnippet;

public class TypeCheckVisitor implements Visitor 
{

   // Types
   public VoidType visit(VoidType x) 
   {
      return x;
   }

   // Expressions
   public UnaryExpression visit(UnaryExpression x) 
   {
      this.visit(x.getOperator());
      this.visit(x.getOperand());

      return x;
   }


   // Statements
   public WhileStatement visit(WhileStatement x) 
   {
      this.visit(x.getBody()); 
      this.visit(x.getGuard()); 

      return x;
   }

   public TypeDeclaration visit(TypeDeclaration x)
   {
      this.visit(x.getName());
      for (Declaration d : x.getFields())
      {
         this.visit(d);
      }
   }
   



}

