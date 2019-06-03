import os

os.system("make clean")
os.system("make")
directory = "benchmarks/"

for benchmark in os.listdir(directory):
   path = os.path.join(directory, benchmark)
   for filename in os.listdir(path):
      if filename.endswith(".mini"):
         mini_path = os.path.join(path, filename)

         os.system("java MiniCompiler " + mini_path)
         os.system("clang -m32 " + mini_path + ".out.ll  'utility.c' ")
         #os.system("diff a.out " + mini_path + ".out.ll")


