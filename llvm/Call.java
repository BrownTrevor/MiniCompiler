package llvm;

import java.util.*;
import ast.Declaration;
import ast.Function;

public class Call implements Llvm {
   private String name;
   private String retType;
   private List<String> params;
   private String target;

   public Call(String target, String name, String retType, 
      List<String> params) 
   {   
      this.target = target;
      this.name = name;
      this.retType = retType;
      this.params = params;
   }

   public Call(String name, String retType, List<String> params) {
      this.target = null;
      this.name = name;
      this.retType = retType;
      this.params = params;
   }

  
   @Override
   public String toString() {
      String paramString = "";

      for(String s : params) {
         paramString += (s + ", ");
      }

      paramString = paramString.substring(0, paramString.length()-2);

      if (this.target == null) {
         return "call " + retType + " @" + name + "(" + paramString + ")";
      }

      return target + " = " + "call " + retType + " @" + name + "(" + paramString + ")";
   }

}