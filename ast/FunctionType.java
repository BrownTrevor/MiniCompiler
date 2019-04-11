import ast.Function;

public class FunctionType implements Type {

   private final String name;
   private final Type retType;
   private final List<Declaration> params;

   public FunctionType(String name, Type retType, List<Declaration> params) {
      this.name = name;
      this.retType = retType;
      this.params = params;
   }

   public FunctionType(Function f) {
      this.name = f.getName();
      this.retType = f.getRetType();
      this.params = f.getParams();
   }

   public String getName() {
      return this.name;
   }

   public Type getRetType() {
      return this.retType;
   }

   public List<Declaration> getParams() {
      return this.params;
   }
}
