package ast;

import cfg.*;
import llvm.*;
import globals.*;

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

   public Type getRetType() {
      return this.retType;
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
      CFGNode exitBlock = new CFGNode();


      // construct exit block
      if (this.retType.llvmType().equals("void")) {
         exitBlock.addInstruction(new RetVoid());
      } else {
         String rtype = this.retType.llvmType();
         Value reg = new Register(rtype, exitBlock);
         exitBlock.addInstruction(new Load(reg.getValue(), rtype + "*", "%_retval_"));
         exitBlock.addInstruction(new Ret(rtype, reg.getValue()));
      }

      //Set functions header
      String functionDeclStr = getFunctionDeclStr();
      rootBlock.setHeader(functionDeclStr);

      //allocate space for a return value if it is needed
      if(!this.retType.llvmType().equals("void")){
         String retInstr = "%_retval_ = alloca " + retType.llvmType();
         rootBlock.addInstruction(new llvm.Generic(retInstr));
      }

      // add instructions to the root block for various decls
      rootBlock = this.visitFunctionParams(params, rootBlock);
      rootBlock = this.visitFunctionDecls(locals, rootBlock);

      // recursively generate the rest of the function
      CFGNode lastBlock = this.body.generateCFG(rootBlock, exitBlock);

   
      // ensure that all paths lead to the exit block
      lastBlock.addInstruction(new Bru(exitBlock.getLabel().getId()));
      lastBlock.addChild(exitBlock);
      exitBlock.addPred(lastBlock);
      
      sealCFG(rootBlock);

      return rootBlock;
   }

   private String getFunctionDeclStr() {
      
      String instruction = "define " + retType.llvmType() + " @" + this.getName() + "(";
      String specialParamStr = " %_p_";
      if (Flags.isRegisterBased())
      {
         specialParamStr = " %";
      }
      
      for (Declaration d : params) {
         instruction += (d.getType().llvmType() + specialParamStr 
            + d.getName() + ", ");
      }
      
   
      if (params.size() >0 ){
         instruction = instruction.substring(0, instruction.length() - 2);
      }
      instruction += ")\n";


      return instruction;
   }


   private CFGNode visitFunctionParams(List<Declaration> params, CFGNode current) {
      if (Flags.isRegisterBased()) {
         return regBasedDeclaration(params, current); 
      }

      for (Declaration d : params) {
         Tables.addToSymbolTable(new Symbol(d));
         current.addInstruction(this.paramAllocToInstruction(d));
      }

      for (Declaration d : params) {
         current.addInstruction(this.paramStoreToInstruction(d));
      }

      return current;
   }

   private Llvm paramAllocToInstruction(Declaration d) {
      return new llvm.Alloca("%" + d.getName(), d.getType().llvmType());
   }

   private Llvm paramStoreToInstruction(Declaration d) {
      String localType = d.getType().llvmType() + "*";
      String localName = "%" + d.getName();
      String paramType = d.getType().llvmType();
      String paramName = "%_p_" + d.getName();

      return new llvm.Store(paramType, paramName, localType, localName);
      //"store %" + d.getName() + ", p_" + d.getName();
      //store i32 %num, i32* %_P_num
   }

   private CFGNode regBasedDeclaration(List<Declaration> params, CFGNode current) {
      for (Declaration d : params) {
         Tables.addToSymbolTable(new Symbol(d));
         SSA.writeVariable(d.getName(), current, 
            new Immediate(d.getType().llvmType(), "%" + d.getName() , current));
      }
      return current;
   }



   private CFGNode visitFunctionDecls(List<Declaration> decls, CFGNode current) {
      if (Flags.isRegisterBased()) {
         return regBasedDeclaration(decls, current);
      }

      for (Declaration d : decls) {
         Tables.addToSymbolTable(new Symbol(d));
         current.addInstruction(this.visitFunctionDecl(d));
      }

      return current;
   }

   private Llvm visitFunctionDecl(Declaration d) {
      return new llvm.Alloca("%" + d.getName(), d.getType().llvmType());
      // "%" + d.getName() + " = alloca " + typeToLlvmStr(d.getType());
   }

   public String llvmType() {
      return "function";
   }

   private void sealCFG(CFGNode block) {
      SSA.sealBlock(block);

      for(CFGNode n : block.getChildren()) {
         if (!n.isSealed()){
            sealCFG(n);
         }
      }
   }
}
