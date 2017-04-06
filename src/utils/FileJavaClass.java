package utils;

import javax.tools.SimpleJavaFileObject;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Petec on 5.11.2016.
 */
public class FileJavaClass extends SimpleJavaFileObject {
    private File file;
    private Class aClass;
    private List<Annotation> annotations;
    private boolean isDuClass;

    public FileJavaClass(File file) {
        super(file.toURI(), Kind.CLASS);
        this.file = file;
        annotations = new ArrayList<>();
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public boolean isDuClass() {
        return isDuClass;
    }

    public void setDuClass(boolean duClass) {
        isDuClass = duClass;
    }
}
