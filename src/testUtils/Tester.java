package testUtils;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import utils.*;
import utils.Compiler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Petec on 12.11.2016.
 */
public class Tester {

    private Tools tools = new Tools();

    public Tester() {
    }

    public void compilationTest(Label testFilesLabel, Label libsDirLabel, Label compOutLabel, List<FileJavaSource> testSourceFiles, TextArea logArea, List<StudentClass> studentArray, Compiler compiler) {
        tools.getFilesFromDir(testFilesLabel.getText(), testSourceFiles, "java",logArea);
        if(testSourceFiles.isEmpty()){
            logArea.insertText(0, "No source files found in test files DIR." + '\n');
            return;
        }else{
            testSourceFiles.forEach(fileJavaSource -> {
                String name = tools.getFileName(fileJavaSource);
                StudentClass student = new StudentClass(name);
                student.getSourceFiles().add(fileJavaSource);
                studentArray.add(student);
                student.getJsonObject().put("Name: ", name);
            });
            studentArray.forEach(studentClass -> {
                boolean result = compiler.compile(studentClass.getSourceFiles(), libsDirLabel.getText(), compOutLabel.getText());
                studentClass.setCompiled(result);
                String name = studentClass.getSourceFiles().get(0).getName();
                logArea.insertText(0, "File: " + name + " Compilation: " + result + '\n');
                studentClass.getJsonObject().put("Compilation result: ", result);
            });
        }
    }

    public void annotationTests(FileJavaClass fileJavaClass) {
        fileJavaClass.getAnnotations().forEach(annotation -> {
            try {
                switch (annotation.annotationType().getCanonicalName()) {
                    case "ClassName":
                        String regex = getAnnotationField(annotation, "regex").toString();
                        testClassName(regex);
                        writeTestResult();
                        break;
                    default:
                        break;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private void writeTestResult() {
    }

    private void testClassName(String regex) {

    }

    public void endTesting(List<StudentClass> studentArray, Label testFilesLabel, TextArea logArea) {
        studentArray.forEach(studentClass -> {
            studentClass.closeJsonFile(testFilesLabel.getText());
            logArea.insertText(0, "Testing completed: " + studentClass.getName() + '\n');
        });
    }

    private static Annotation getAnnotation(Class aClass, Class annotationClass){
        for(Annotation a : aClass.getAnnotations()){
            String annCanNameArr[] = annotationClass.getCanonicalName().split("\\.");
            if(a.annotationType().getCanonicalName().equals(annCanNameArr[annCanNameArr.length - 1])){
                return a;
            }
        }
        return null;
    }

    private static Object getAnnotationField(Annotation annotation, String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return annotation.annotationType().getMethod(fieldName).invoke(annotation);
    }

//    try {
//        System.out.println(getAnnotationFieldWithReflection(a, "regex"));
//    } catch (NoSuchMethodException e) {
//        e.printStackTrace();
//    } catch (InvocationTargetException e) {
//        e.printStackTrace();
//    } catch (IllegalAccessException e) {
//        e.printStackTrace();
//    }
}
