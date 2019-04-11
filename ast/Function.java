package ast;

import java.util.List;

import javafx.css.Declaration;

public class Function {
   private final int lineNum;
   private final String name;
   private final Type retType;
   private final List<Declaration> params;
   private final List<Declaration> locals;
   private final Statement body;

   public Function(int lineNum, String name, List<Declaration> params, Type retType, List<Declaration> locals,
         Statement body) {
      this.lineNum = lineNum;
      this.name = name;
      this.params = params;
      this.retType = retType;
      this.locals = locals;
      this.body = body;
   }

   public String getName() 
   {
      return this.name;
   }

   public Type getRetType() 
   {
      return this.retType;
   }

   public List<Declaration> getParams() 
   {
      return this.params;
   }

   public List<Declaration> getLocals() 
   {
      return this.locals;
   }

   public Statement getBody()
   {
      return this.body;
   }

   public int getLineNum() 
   {
      return this.lineNum;
   }

}

