package ast;

import cfg.*;
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

   public CFGNode generateCFG() {
      CFGNode rootBlock = new CFGNode();
      CFGNode exitBlock = new CFGNode(); // TODO: setup exit instructions

      String functionDeclStr = getFunctionDeclStr();
      String retTypeString = typeToLlvmStr(retType);
      String retInstr = "%retval = alloca " + retTypeString;

      rootBlock.addInstruction(functionDeclStr);
      rootBlock.addInstruction(retInstr);
      rootBlock = this.visitFunctionParams(params, rootBlock);
      rootBlock = this.visitFunctionDecls(locals, rootBlock);

      CFGNode lastBlock = this.body.generateCFG(rootBlock, exitBlock);

      return rootBlock;
   }

   private String getFunctionDeclStr() {
      String retTypeStr = typeToLlvmStr(retType);
      String instruction = "define " + retTypeStr + " @" + this.getName() + "( ";

      for (Declaration d : params) {
         instruction += (typeToLlvmStr(d.getType()) + " %p_" + d.getName() + ", ");
      }

      instruction = instruction.substring(0, instruction.length() - 2) + ")";

      return instruction;
   }

   private CFGNode visitFunctionParams(List<Declaration> params, CFGNode current) {

      for (Declaration d : params) {
         current.addInstruction(this.paramAllocToInstruction(d));
      }

      for (Declaration d : params) {
         current.addInstruction(this.paramStoreToInstruction(d));
      }

      return current;
   }

   private String paramAllocToInstruction(Declaration d) {
      String instruction = "%" + d.getName() + " = alloca " + typeToLlvmStr(d.getType());
      return instruction;
   }

   private String paramStoreToInstruction(Declaration d) {
      return "store %" + d.getName() + ", p_" + d.getName();
   }

   private CFGNode visitFunctionDecls(List<Declaration> decls, CFGNode current) {

      for (Declaration d : decls) {
         current.addInstruction(this.visitFunctionDecl(d));
      }

      return current;
   }

   private String visitFunctionDecl(Declaration d) {
      return "%" + d.getName() + " = alloca " + typeToLlvmStr(d.getType());
   }

   // Converts Type objects to llvm strings
   private String typeToLlvmStr(Type t) {
      if (t instanceof BoolType) {
         return "i32";
      } else if (t instanceof IntType) {
         return "i32";
      } else if (t instanceof NullType) {
         return "maybe void?";
      } else if (t instanceof StructType) {
         // might be a space instead of a period after struct
         StructType st = (StructType) t;
         return "%struct." + st.getName() + "*";
      } else if (t instanceof VoidType) {
         return "void";
      } else {
         System.out.println("ERROR: Invalid type to string conversion");
         System.exit(1);
      }
      // my ide didn't like that i didnt have a ret stmt here
      return "-1";
   }


   public String llvmType() {
      return "function";
   }
}
