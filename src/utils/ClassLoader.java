package utils;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * Created by Petec on 23.11.2016.
 */
public class ClassLoader {

    private static ClassLoader instance = null;

    protected ClassLoader() {

    }

    public static ClassLoader getInstance() {
        if(instance == null) {
            instance = new ClassLoader();
        }
        return instance;
    }

    public void loadClasses(List<FileJavaClass> solutionFiles){
        if(solutionFiles.isEmpty()){
            System.out.println("Error: No source files.");
        }else{
            String sourcePath = solutionFiles.get(0).getFile().getAbsolutePath();
            String sourceDir = sourcePath.substring(0, sourcePath.length() - solutionFiles.get(0).getFile().getName().length());
            System.out.println("Source DIR: " + sourceDir);

            URL[] urls = null;

            try {
                URL sourceURL = new URL("file:/" + sourceDir);
                urls = new URL[] {sourceURL};
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            URLClassLoader cls = new URLClassLoader(urls);

            solutionFiles.forEach(fileJavaClass -> {
                try {
                    String name = fileJavaClass.getFile().getName().substring(0, fileJavaClass.getFile().getName().length() - 6);
                    fileJavaClass.setaClass(cls.loadClass(name));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    
    public void loadAnnotations(List<FileJavaClass> solutionFiles){
        solutionFiles.forEach(fileJavaClass -> {
                Class aClass = fileJavaClass.getaClass();
                for (Annotation annotation : aClass.getDeclaredAnnotations()) {
                    fileJavaClass.getAnnotations().add(annotation);
                    if(annotation.annotationType().getName().equals("DU")){
                        fileJavaClass.setDuClass(true);
                    }
                }
            }
        );
    }
}
