package ast;

import cfg.*;
import llvm.*;

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
      String retInstr = "%retval = alloca " + retType.llvmType();

      rootBlock.addInstruction(new llvm.Generic(functionDeclStr));
      rootBlock.addInstruction(new llvm.Generic(retInstr));
      rootBlock = this.visitFunctionParams(params, rootBlock);
      rootBlock = this.visitFunctionDecls(locals, rootBlock);

      CFGNode lastBlock = this.body.generateCFG(rootBlock, exitBlock);

      return rootBlock;
   }

   private String getFunctionDeclStr() {
      
      String instruction = "define " + retType.llvmType() + " @" + this.getName() + "( ";

      for (Declaration d : params) {
         instruction += (d.getType().llvmType() + " %p_" + d.getName() + ", ");
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

   private Llvm paramAllocToInstruction(Declaration d) {
      return new llvm.Alloca(d.getName(), d.getType().llvmType());
   }

   private Llvm paramStoreToInstruction(Declaration d) {
      String localType = d.getType().llvmType();
      String localName = "%" + d.getName();
      String paramType = d.getType().llvmType() + "*";
      String paramName = "%_p_" + d.getName();

      return new llvm.Store(paramType, paramName, localType, localName);
      //"store %" + d.getName() + ", p_" + d.getName();
      //store i32 %num, i32* %_P_num
   }

   private CFGNode visitFunctionDecls(List<Declaration> decls, CFGNode current) {

      for (Declaration d : decls) {
         current.addInstruction(this.visitFunctionDecl(d));
      }

      return current;
   }

   private Llvm visitFunctionDecl(Declaration d) {
      return new llvm.Alloca(d.getName(), d.getType().llvmType());
      // "%" + d.getName() + " = alloca " + typeToLlvmStr(d.getType());
   }

   public String llvmType() {
      return "function";
   }
}
