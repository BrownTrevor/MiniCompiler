package globals;


public class Flags {
   private static boolean _isStack = false;
   private static boolean _isReg = false;

   public static boolean isStackBased() {
      return _isStack;
   }

   public static boolean isRegisterBased() {
      return _isReg;
   }

   public static void setStackFlag(boolean b) {
      _isStack = b;
   }

   public static void setRegFlag(boolean r) {
      _isReg = r;
   }
}


