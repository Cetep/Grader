package utils;

import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Petec on 4.4.2017.
 */
public class StudentClass {
    private List<FileJavaClass> classFiles;
    private List<FileJavaSource> sourceFiles;
    private boolean compiled;
    private JSONObject jsonObject;
    private String name;
    private int points = 0;
    private int testCount = 0;

    public StudentClass(String name) {
        this.classFiles = new ArrayList<>();
        this.sourceFiles = new ArrayList<>();
        this.jsonObject = new JSONObject();
        this.name = name;
    }

    public boolean isCompiled() {
        return compiled;
    }

    public void setCompiled(boolean compiled) {
        this.compiled = compiled;
    }

    public List<FileJavaClass> getClassFiles() {
        return classFiles;
    }

    public void setClassFiles(List<FileJavaClass> classFiles) {
        this.classFiles = classFiles;
    }

    public List<FileJavaSource> getSourceFiles() {
        return sourceFiles;
    }

    public void setSourceFiles(List<FileJavaSource> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void closeJsonFile(String outputDIR){
        try (FileWriter file = new FileWriter(outputDIR + "\\" + name + ".json")) {

            file.write(jsonObject.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
