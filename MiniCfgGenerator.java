import java.util.*;

import cfg.*;
import ast.*;


public class MiniCfgGenerator {

   private static ArrayList<CFGNode> cfgList;

   public static void generateCFG(ast.Program x) {
      // Initialize Tables
      Tables.initStructTable();
      Tables.initSymbolTable();
      cfgList = new ArrayList<CFGNode>();

      // Populate Global Declaration instructions
      CFGNode globalBlock = new CFGNode();
      globalBlock = visitStructDecls(x.getTypes(), globalBlock);
      globalBlock = visitGlobalVars(x.getDecls(), globalBlock);

      cfgList.add(globalBlock);

      //System.err.println(Tables.getStructTable().toString());

      visitFunctions(x.getFuncs());
   }

   // =========================================================================
   // Declarations
   // =========================================================================
//compare null
   // Visit Struct decls
   public static CFGNode visitStructDecls(List<TypeDeclaration> structs, CFGNode globalBlock) {
      // Generate global instructions and add to global block
      for (TypeDeclaration struct : structs) {
         globalBlock.addInstruction(new llvm.Generic(visitStructDecl(struct)));
      }

      return globalBlock;
   }

   // Visit single Struct decl
   public static String visitStructDecl(TypeDeclaration decl) {
      // instruction example: %struct.A = type{i32, i32}
      String instruction = "%struct.";
      instruction += decl.getName();
      instruction += " = type{";

      for (Declaration d : decl.getFields()) {
         instruction += (d.getType().llvmType() + ", ");
      }

      // Trim the extra comma and the closing bracket
      instruction = instruction.substring(0, instruction.length() - 2) + "}";

      Struct struct = new Struct(decl);
      Tables.addToStructTable(struct); 

      return instruction;
   }

   // Visit all global var decls
   public static  CFGNode visitGlobalVars(List<Declaration> decls, CFGNode globalBlock) {

      // Generate global instructions and add to global block
      for (Declaration d : decls) {
         globalBlock.addInstruction(new llvm.Generic(visitGlobalDecl(d)));
      }

      return globalBlock;
   }

   // Visit a single decl and generate the instruction
   public static  String visitGlobalDecl(Declaration d) {
      String instruction = "@" + d.getName() + " = common global ";
      String llvmType = d.getType().llvmType();
      instruction += (llvmType + " ");
      instruction += typeToDefaultValue(llvmType);
      instruction += ", align 4";
      Symbol newSymbol = new Symbol(d);
      Tables.addToSymbolTable(newSymbol);

      return instruction;
   }

   private static String typeToDefaultValue(String type) {
      if(type.contains("*")) {
         return "null"; 
      }
      return "0";
   }

   // Visit all functions
   public static void visitFunctions(List<Function> funcs) {
      for (Function f : funcs) {
         Tables.addToSymbolTable(new Symbol(f.getName(), f));
         Tables.pushSymbolTable();
         CFGNode functionNode = f.generateCFG();
         cfgList.add(functionNode);
         Tables.popSymbolTable();
      }
   }

   public static List<CFGNode> getCfgList() {
      return cfgList;
   }

}