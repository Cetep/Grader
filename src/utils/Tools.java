package utils;

import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Created by Petec on 4.4.2017.
 */
public class Tools {
    public Tools() {
    }

    public void getFilesFromDir(String dir, List list, String fileType, TextArea logArea){
        list.clear();
        File[] listOfFiles = new File(dir).listFiles();
        if(listOfFiles != null){
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    if (listOfFile.getName().endsWith("." + fileType)) {
                        if(fileType.equals("class")){
                            list.add(new FileJavaClass(listOfFile));
                            logArea.insertText(0, "Class file loaded: " + listOfFile.getName() + "\n");
                        }else{
                            list.add(new FileJavaSource(listOfFile));
                            logArea.insertText(0, "Source file loaded: " + listOfFile.getName() + "\n");
                        }
                    }
                } else if (listOfFile.isDirectory()) {
                    logArea.insertText(0, "Directory found: " + listOfFile.getName() + "\n");
                }
            }
        }else{
            System.out.println("No files found in: " + dir);
        }
    }

    public void addDirChooser(MenuItem menuItem, Label dirLabel, Stage primaryStage, String noDirText){
        menuItem.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(primaryStage);

            if(selectedDirectory == null){
                dirLabel.setText(noDirText);
            }else{
                dirLabel.setText(selectedDirectory.getAbsolutePath());
            }
        });
    }

    public String getFileName(FileJavaSource fileJavaSource) {
        int start = fileJavaSource.getName().lastIndexOf("/") + 1;
        int end = fileJavaSource.getName().lastIndexOf(".");
        return fileJavaSource.getName().substring(start, end);
    }

    public boolean createDir(String dir){
        File theDir = new File(dir);
        boolean result = false;

        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());


            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }
        return result;
    }
}
