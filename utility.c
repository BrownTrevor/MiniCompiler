#include <stdio.h>

int read() {
   int input;

   scanf("%d", &input);

   return input;
}

void print(int input) {
   printf("%d ", input);
}

void println(int input) {
   printf("%d\n", input);
}
