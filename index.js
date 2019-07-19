var stdio = require('stdio');
var path = require('path');
var fs = require('fs');
var validUrl = require('valid-url');
var shell = require('shelljs');

var ops = stdio.getopt({
    'bazel': {key:'b', args:1, mandatory:true, description:"Full path to bazel root folder"},
    'desugar_jdk_libs':{key:'d', args:1, mandatory:true, description:"Full path to desugar_jdk_libs root folder"},
    'output':{key:'o', args:1, mandatory:true, description:"Path to save tools"}
});

function printDelimiter() {
    console.log("------");
}

function createFolderIfNotExist(pathFolder) {
    if (!fs.existsSync(pathFolder)){
        fs.mkdirSync(pathFolder);
    }
    return pathFolder;
}

function handleInputPath(input) {
    if(fs.existsSync(path.join(input))) {
        return path.join(input);
    } else {
        console.error("Wrong bazel input");
        return undefined;
    }
}

function buildBazel(bazelPath) {
    shell.cd(bazelPath);
    shell.exec('bazel build //src:bazel').stdout;
    return path.join(bazelPath, "bazel-bin/src/bazel")
}

function buildDesugarSdkLibrary(bazelBin, desugarSdkLibraryPath) {
    shell.cd(desugarSdkLibraryPath);
    shell.exec(bazelBin+" "+"build desugar_jdk_libs").stdout;
    return path.join(desugarSdkLibraryPath, "bazel-bin/src/share/classes/java/libjava.jar");
}

function copyJdkLibrary(bazelPath, desugarSdkLibPath) {
    var originalBazelBazelLink = fs.readlinkSync(path.join(bazelPath,"bazel-bazel"));
    var destinationSdkLibPath = path.join(originalBazelBazelLink,"..","..",'external/android_tools/desugar_jdk_libs.jar');

    fs.copyFileSync(desugarSdkLibPath, destinationSdkLibPath);

}

function buildDesugaredLibrary(bazelBin, bazelPath) {
    shell.cd(bazelPath)
    shell.exec(bazelBin+" build @bazel_tools//tools/android:desugar_java8_legacy_libs").stdout;

    var originalBazelBazelLink = fs.readlinkSync(path.join(bazelPath,"bazel-bazel"));
    return path.join(originalBazelBazelLink, "bazel-out/darwin-fastbuild/bin/external/bazel_tools/tools/android/desugared_java8_legacy_libs.jar");
}

function buildDesugareTool(bazelBin, bazelPath) {
    shell.cd(bazelPath)
    shell.exec(bazelBin+" build //src/tools/android/java/com/google/devtools/build/android/desugar:Desugar").stdout;

    var originalBazelBinLink = fs.readlinkSync(path.join(bazelPath,"bazel-bin"));
    return path.join(originalBazelBinLink, "src/tools/android/java/com/google/devtools/build/android/desugar/");
}

function copyFileSync( source, target ) {

    var targetFile = target;

    if(fs.lstatSync(source).isSymbolicLink()) {
        var originalSource = fs.readlinkSync(source);
        source = originalSource;
    }

    //if target is a directory a new file with the same name will be created
    if ( fs.existsSync( target ) ) {
        if ( fs.lstatSync( target ).isDirectory() ) {
            targetFile = path.join( target, path.basename( source ) );
        }
    }

    fs.copyFileSync(source, targetFile)
    // fs.writeFileSync(targetFile, fs.readFileSync(source));
}

function copyFolderRecursiveSync( source, target ) {
    var files = [];

    //check if folder needs to be created or integrated
    var targetFolder = path.join( target, path.basename( source ) );
    if ( !fs.existsSync( targetFolder ) ) {
        fs.mkdirSync( targetFolder );
    }

    //copy
    if ( fs.lstatSync( source ).isDirectory() ) {
        files = fs.readdirSync( source );
        files.forEach( function ( file ) {
            var curSource = path.join( source, file );
            if ( fs.lstatSync( curSource ).isDirectory() ) {
                copyFolderRecursiveSync( curSource, targetFolder );
            } else {
                copyFileSync( curSource, targetFolder );
            }
        } );
    }
}

var bazelPath = undefined;
var bazelJdkLibsPath = undefined;
var outputPath = undefined;

bazelPath = handleInputPath(ops.bazel);
if(bazelPath == undefined){
    console.error("Wrong bazel input");
    process.exit(1);
}

bazelJdkLibsPath = handleInputPath(ops.desugar_jdk_libs);
if(bazelJdkLibsPath == undefined){
    console.error("Wrong desugar_jdk_libs input");
    process.exit(1);
}

var outputPath = createFolderIfNotExist(path.join(ops.output));
if(outputPath == undefined){
    console.error("Wrong output input");
    process.exit(1);
}

function handleDesugarJDKLibrary(bazelBinPath, bazelPath, desugarSdkLibPath) {
//Copy builded sdk to bazel repository and run android desugar jar builder
    copyJdkLibrary(bazelPath, desugarSdkLibPath);
    printDelimiter();

// Build desugar jdk lib
    var desugaredJdkLibs = buildDesugaredLibrary(bazelBinPath, bazelPath);
    printDelimiter();

// Copy desugar jdk lib to output
    var outputPathOfDesugaredLib = path.join(outputPath, "inject");
    createFolderIfNotExist(outputPathOfDesugaredLib);
    var destinationFile = path.join(outputPathOfDesugaredLib, path.basename(desugaredJdkLibs));
    fs.copyFileSync(desugaredJdkLibs, destinationFile);
    printDelimiter();
}


function handleDesugarTool(bazelBin, bazelPath) {
    // Build desugar tool
    var desugarFolder = buildDesugareTool(bazelBin, bazelPath);
    console.log(desugarFolder);
    printDelimiter();
    // Copy all files with recursion
    copyFolderRecursiveSync(desugarFolder, outputPath);
    console.log("Finished copy")
}

//Cloned bazel repository using provided url and build them
// Navigate to bazel path and build them
var bazelBinPath = buildBazel(bazelPath);
printDelimiter();


//Cloned desugar_sdk_library using provided url and build desugar sdk
// Navigate to bazel desugar jdk library and build them
var desugarSdkLibPath = buildDesugarSdkLibrary(bazelBinPath, bazelJdkLibsPath);
printDelimiter();

handleDesugarJDKLibrary(bazelBinPath, bazelPath, desugarSdkLibPath);
handleDesugarTool(bazelBinPath, bazelPath);

// Copy original desugar.sh script
var originalDesugarSHPath = path.join(bazelPath, "tools/android/desugar.sh");
fs.copyFileSync(originalDesugarSHPath, path.join(outputPath, "desugar_original.sh"));

// Copy modified desugar.sh script
var modifiedDesugarSHPath = path.join(__dirname, "desugar.sh");
fs.copyFileSync(modifiedDesugarSHPath, path.join(outputPath, "desugar.sh"));

console.log("Finished migration");







