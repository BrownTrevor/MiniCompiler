package cfg;

import java.util.*;
import llvm.*;
import cfg.*;

public class OutputFormater {

   public static String buildFile(List<cfg.CFGNode> cfgList){
      HashSet<cfg.CFGNode> seen = new HashSet<cfg.CFGNode>();
      ArrayList<cfg.CFGNode> queue = new ArrayList<cfg.CFGNode>();
      StringBuilder sb = new StringBuilder();

      sb.append(buildGlobals(cfgList.get(0)));

      for (int i = 1; i < cfgList.size(); i++) {
         CFGNode topCfg = cfgList.get(i);
      
         if (!(seen.contains(topCfg))) {
            seen.add(topCfg);
            queue.add(topCfg);
         }

         while (!(queue.isEmpty())) {
            cfg.CFGNode current = queue.remove(0);

            sb.append(current.toString());

            for (cfg.CFGNode child : current.getChildren()) {
               if (!(seen.contains(child))) {
                  seen.add(child);
                  queue.add(child);
               }
            }
         }
         sb.append("}\n\n");
      }

      //add in Library functions
      sb.append(buildLibrary());

      return sb.toString();
   }

   private static String buildGlobals(CFGNode firstNode) {
      List<Llvm> instrList = firstNode.getInstructions();

      if (instrList.get(0).toString().contains("define")) {
         return "";
      }

      StringBuilder sb = new StringBuilder();
      
      for(Llvm instr : instrList) {
         sb.append(instr.toString() + "\n");
      }

      sb.append("\n\n");
      return sb.toString();
   }

   private static String buildLibrary(){
      String s = "declare i8* @malloc(i32)\n";
      s += "declare void @free(i8*)\n";
      s += "declare i32 @printf(i8*, ...)\n";
      s += "declare i32 @scanf(i8*, ...)\n";

      return s;
   }
}