package cfg;


public class Register {
   private static int registerNumber = 0;
   
   public static String currentRegister() {
      return ("%u" + registerNumber); 
   }

   public static String newRegister() {
      registerNumber++;
      return ("%u" + registerNumber);
   }
   
}