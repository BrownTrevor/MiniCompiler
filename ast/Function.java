package ast;

import java.util.List;

public class Function implements Type
{
   private final int lineNum;
   private final String name;
   private final Type retType;
   private final List<Declaration> params;
   private final List<Declaration> locals;
   private final Statement body;

   public Function(int lineNum, String name, List<Declaration> params,
      Type retType, List<Declaration> locals, Statement body)
   {
      this.lineNum = lineNum;
      this.name = name;
      this.params = params;
      this.retType = retType;
      this.locals = locals;
      this.body = body;
   }

   public String getName() {
      return this.name;
   }

   public Type getType() {
      return this.retType;
   }

   public List<Declaration> getParams() {
      return this.params;
   }

   public List<Declaration> getLocals() {
      return this.locals;
   }

   public Statement getBody() {
      return this.body;
   }

   @Override
   public String toString() {
      String s = "Name: " + this.name + " Return type: " + this.retType + '\n';

      s += "Params: " + '\n';
      for(Declaration param : this.params) {
         s += param.toString();
      }

      s += "Locals: " + '\n';
      for(Declaration local : this.locals) {
         s += local.toString();
      }

      s += "Body: " + '\n';
      s += this.body.toString() + '\n';

      return s;
   }
}
