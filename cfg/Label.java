package cfg;

public class Label {
   private static int count = 0;
   private String id;

   public Label() {
      this.id = "L" + count;
      count++;
   }

   public String getId() {
      return this.id;
   }
}


