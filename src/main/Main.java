package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import testUtils.Tester;
import utils.ClassLoader;
import utils.Compiler;
import utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Petec on 5.3.2017.
 */

public class Main extends Application {
    private TextArea logArea;

    private ListView<String> listView;

    private Label preSolutionDirLabel;
    private Label preLibsDirLabel;
    private Label preCompOutLabel;
    private Label preTestFilesLabel;
    private Label solutionDirLabel;
    private Label libsDirLabel;
    private Label compOutLabel;
    private Label testFilesLabel;

    private BorderPane mainPane;

    private MenuItem solutionDirMenu;
    private MenuItem libsDirMenu;
    private MenuItem compOutputDirMenu;
    private MenuItem testFilesDirMenu;
    private MenuItem loadTestDef;
    private MenuItem runTests;
    private MenuItem compileSolutionMenu;

    private MenuItem testMenuItem;

    private List<FileJavaClass> solutionClassFiles;
    private List<FileJavaSource> solutionSourceFiles;
    private List<FileJavaClass> annotationFiles;
    private List<FileJavaSource> testSourceFiles;
    private List<StudentClass> studentArray;

    private String noDirText = "No DIR selected";
    private Compiler compiler = new Compiler();
    private ClassLoader classLoader = ClassLoader.getInstance();
    private Tools tools = new Tools();
    private Tester tester = new Tester();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initComponents(primaryStage);
        initActions(primaryStage);
        initOther();
    }

    private void initComponents(Stage primaryStage) {
        preSolutionDirLabel = new Label("Solution DIR: ");
        preLibsDirLabel = new Label("Libs DIR: ");
        preCompOutLabel = new Label("Compilation output DIR: ");
        preTestFilesLabel = new Label("Test files DIR: ");

        mainPane = new BorderPane();

        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("File");
        Menu menuRun = new Menu("Run");

        solutionDirMenu = new MenuItem("Set solution DIR");
        libsDirMenu = new MenuItem("Set libs DIR");
        compOutputDirMenu = new MenuItem("Set compilation output DIR");
        testFilesDirMenu = new MenuItem("Set test files DIR");
        compileSolutionMenu = new MenuItem("Compile solution");
        loadTestDef = new MenuItem("Load test definition");
        runTests = new MenuItem("Run tests");

        testMenuItem = new MenuItem("Test stuff");

        menuFile.getItems().addAll(solutionDirMenu, libsDirMenu, compOutputDirMenu, testFilesDirMenu);
        menuRun.getItems().addAll(compileSolutionMenu, loadTestDef, runTests);
        menuRun.getItems().add(testMenuItem);

        menuBar.getMenus().addAll(menuFile, menuRun);

        GridPane centerGrid = new GridPane();

        solutionDirLabel = new Label(noDirText);
        libsDirLabel = new Label(noDirText);
        compOutLabel = new Label(noDirText);
        testFilesLabel = new Label(noDirText);

        logArea = new TextArea();
        logArea.setEditable(false);
        listView = new ListView<>();

        centerGrid.setHgap(5);
        centerGrid.setVgap(5);
        centerGrid.setPadding(new Insets(10,10,10,10));
        centerGrid.add(preSolutionDirLabel, 0, 0);
        centerGrid.add(solutionDirLabel,1,0);
        centerGrid.add(preLibsDirLabel, 0, 2);
        centerGrid.add(libsDirLabel, 1, 2);
        centerGrid.add(preCompOutLabel, 0, 4);
        centerGrid.add(compOutLabel, 1 , 4);
        centerGrid.add(preTestFilesLabel, 0, 6);
        centerGrid.add(testFilesLabel, 1, 6);
        centerGrid.setGridLinesVisible(true);
        mainPane.setCenter(centerGrid);
        mainPane.setTop(menuBar);

        solutionDirLabel.setText(noDirText);
        libsDirLabel.setText(noDirText);
        compOutLabel.setText(noDirText);
        testFilesLabel.setText(noDirText);

        mainPane.setBottom(logArea);
        mainPane.setLeft(listView);

        Scene mainScene = new Scene(mainPane, 800, 600);

        primaryStage.setMaximized(true);
        primaryStage.setTitle("Grader");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void initActions(Stage primaryStage) {
        tools.addDirChooser(solutionDirMenu, solutionDirLabel, primaryStage, noDirText);
        tools.addDirChooser(libsDirMenu, libsDirLabel, primaryStage, noDirText);
        tools.addDirChooser(compOutputDirMenu, compOutLabel, primaryStage, noDirText);
        tools.addDirChooser(testFilesDirMenu, testFilesLabel, primaryStage, noDirText);

        compileSolutionMenu.setOnAction(event -> {
            if(solutionDirLabel.getText() == noDirText || libsDirLabel.getText() == noDirText || compOutLabel.getText() == noDirText){
                logArea.insertText(0, "Solution, Libs and Compilation Output DIRs all have to be selected." + '\n');
                return;
            }
            tools.getFilesFromDir(solutionDirLabel.getText(), solutionSourceFiles, "java", logArea);

            createAnnotationJAR();

            logArea.insertText(0, "Attempting solution compilation." + '\n');
            boolean result = compiler.compile(solutionSourceFiles, libsDirLabel.getText(), compOutLabel.getText());
            if(result){
                logArea.insertText(0, "Solution compilation completed" + '\n');
                tools.getFilesFromDir(compOutLabel.getText(), solutionClassFiles, "class",logArea);
                classLoader.loadClasses(solutionClassFiles);
                classLoader.loadAnnotations(solutionClassFiles);
                logArea.insertText(0, "Solution classes loaded." + '\n');

                File[] files = new File[solutionClassFiles.size()];
                for (int i = 0; i < solutionClassFiles.size(); i++){
                    files[i] = solutionClassFiles.get(i).getFile();
                }
                JarHandler.getInstance().createJarArchive(new File(libsDirLabel.getText() + "/solution.jar"), files);
                logArea.insertText(0, "JAR for test file compilation created." + '\n');
            }else{
                logArea.insertText(0, "Solution compilation failed." + '\n');
            }
        });

        loadTestDef.setOnAction(event -> {
            if(solutionClassFiles.isEmpty()){
                logArea.insertText(0, "Compiled solution not found." + '\n');
            }else{
                logArea.insertText(0, "Loading test definitions." + '\n');
                solutionClassFiles.forEach(fileJavaClass -> {
                        if(fileJavaClass.isDuClass()){
                            List<String> annotationNames = new ArrayList<String>();
                            fileJavaClass.getAnnotations().forEach(annotation -> {
                                    annotationNames.add(annotation.toString());
                                }
                            );
                            ObservableList<String> items = FXCollections.observableArrayList (annotationNames);
                            listView.setItems(items);
                        }
                    }
                );
            }
        });

        runTests.setOnAction(event -> {
            if (testFilesLabel.getText() == noDirText || libsDirLabel.getText() == noDirText || compOutLabel.getText() == noDirText){
                logArea.insertText(0, "Test file, Libs and Compilation Output DIRs all have to be selected." + '\n');
                return;
            }else{
                tester.compilationTest(testFilesLabel, libsDirLabel, compOutLabel, testSourceFiles, logArea, studentArray, compiler);
                solutionClassFiles.forEach(fileJavaClass -> {
                    if(fileJavaClass.isDuClass()){
                        tester.annotationTests(fileJavaClass);
                    }
                });

                tester.endTesting(studentArray, testFilesLabel, logArea);
            }
        });

        testMenuItem.setOnAction(event -> {

        });
    }

    private void createAnnotationJAR() {
        String userPath = System.getProperty("user.dir");
        //modify in order to work after compilation of the whole task
        userPath = userPath + "\\out\\production\\Grader";
        System.out.println(userPath);
        tools.getFilesFromDir(userPath, annotationFiles, "class", logArea);

        //create JAR with annotation classes
        if(!annotationFiles.isEmpty()){
            File[] files = new File[annotationFiles.size()];
            for (int i = 0; i < annotationFiles.size(); i++){
                files[i] = annotationFiles.get(i).getFile();
            }
            JarHandler.getInstance().createJarArchive(new File(libsDirLabel.getText() + "/annotations.jar"), files);
        }
    }

    private void initOther() {
        solutionClassFiles = new ArrayList<>();
        solutionSourceFiles = new ArrayList<>();
        annotationFiles = new ArrayList<>();
        testSourceFiles = new ArrayList<>();
        studentArray = new ArrayList<>();
    }
}
