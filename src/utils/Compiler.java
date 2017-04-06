package utils;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Petec on 5.11.2016.
 */
public class Compiler {

    public Compiler(){
    }

    public boolean compile(List<FileJavaSource> sourceJavaFiles, String libsDir, String compOutPath) {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        Iterable options = Arrays.asList(
                "-encoding", "UTF-8",
                "-classpath", buildClassPath(libsDir),
                "-d", compOutPath
        );
        boolean result = javaCompiler.getTask(null, null, diagnostics, options, null, sourceJavaFiles).call();

        System.out.println("Compilation diagnostics: " + diagnostics.getDiagnostics() + '\n');
        System.out.println("Compilation result: " + result + '\n');

        return result;
    }

    private static String buildClassPath(String libsDir) {
        String classPath = "";
        File[] listOfFiles = new File(libsDir).listFiles();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (listOfFile.getName().endsWith(".jar")) {
                    classPath = classPath + listOfFile.getAbsolutePath() + ";";
                }
            }
        }
        return classPath;
    }
}