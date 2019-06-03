import os

os.system("make clean")
os.system("make")
directory = "benchmarks/"

for benchmark in os.listdir(directory):
   path = os.path.join(directory, benchmark)
   for filename in os.listdir(path):
      if filename.endswith(".mini"):
         mini_path = os.path.join(path, filename)
         exec_path = os.path.join(path, "exec")
         input_path = os.path.join(path, "input")
         my_output_path = os.path.join(path, "myoutput")
         test_output_path = os.path.join(path, "output")

         os.system("java MiniCompiler " + mini_path)
         os.system("clang -m32 " + mini_path + ".out.ll  'utility.c' " +
            "-o " + exec_path)
         os.system("./" + exec_path + " < " + input_path + " > " + my_output_path)
         os.system("diff " + my_output_path + " " + test_output_path)

         #os.system("diff a.out " + mini_path + ".out.ll")


