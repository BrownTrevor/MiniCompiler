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

      visitFunctions(x.getFuncs());
   }

   // =========================================================================
   // Declarations
   // =========================================================================

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
      instruction += "type{ ";

      for (Declaration d : decl.getFields()) {
         instruction += (typeToLlvmStr(d.getType()) + ", ");
      }

      // Trim the extra comma and the closing bracket
      instruction = instruction.substring(0, instruction.length() - 1) + "}";

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
      String instruction = "%global" + d.getName() + " external global ";
      instruction += typeToLlvmStr(d.getType());

      Symbol newSymbol = new Symbol(d);
      Tables.addToSymbolTable(newSymbol);

      return instruction;
   }

   // Visit all functions
   public static void visitFunctions(List<Function> funcs) {
      for (Function f : funcs) {
         //TODO: Verify table pushing and poping is the right move here
         Tables.pushSymbolTable();
         CFGNode functionNode = f.generateCFG();
         cfgList.add(functionNode);
         Tables.popSymbolTable();
      }
   }

   // Converts Type objects to llvm strings
   private static String typeToLlvmStr(Type t) {
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
      return "shouldn't have gotten here: typeToLLVMstr";
   }

}