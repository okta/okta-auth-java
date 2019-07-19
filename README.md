
# Instruction how to exclude tools for desugaring from bazel repository

Install bazel using binary installer follow instruction https://docs.bazel.build/versions/master/install.html . It's possible to compile bazel from source code, but it's require additional steps which are no convenient.

Clone bazel repository from https://github.com/bazelbuild/bazel.git to build everything without error, please checkout on some release version

Clone implementation of Java8 API's for previous Java versions from https://github.com/ihormartsekha-okta/desugar_jdk_libs.git

To exclude tools from bazel repository you should run follow commands:
```bash
npm install // install dependencies for script
node index.js -b "full path to bazel repository path" -d "Full path to desugar_jdk_libs repository path" -o "full path where to persist ready tools"
```

This script will persist following tools: 
* desugar java tool in ```${output_folder}/desugar```
* implementation of java8 API's in ```${output_folder}/inject/desugared_java8_legacy_libs.jar```
* shell script for desugaring jar files ```${output_folder}/desugar.sh```

To desugar any jar file you should do follow steps:

```bash
desugar.sh <output folder where persist desugar tool folвer> --input <source jar file> --output <output jar file> --classpath_entry <path to Jar file which --input Jar file is depends on  directly> --classpath_entry <> ... --bootclasspath_entry <path to jar file that was used to compile the --input Jar with, like javac's -bootclasspath_entry flag (required)> --bootclasspath_entry <> ...
```

