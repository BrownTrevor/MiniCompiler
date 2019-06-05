package cfg;

import ast.*;

public interface Value {

   public String getLlvmType();
   public String getValue();
   public boolean isRegister();
   public CFGNode getBlock();
  
}