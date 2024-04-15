package org.example.laxilab.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.laxilab.Modal.FileContent;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class LaxiLabController implements Initializable {
    public TextArea textarea_code;
    public TreeView<String> tree_view;
    public MenuBar menu_bar;
    public MenuItem new_file_item;
    public MenuItem open_file_item;
    public MenuItem exit_item;
    public MenuItem save_item;
    public MenuItem open_folder_item;
    public TabPane tabPane;
    public MenuItem save_as_item;
    public Button close_btn;
    private Stage stage;
    private File file;
    private TextArea textarea;
    private FileContent filecon=new FileContent();


    public void NewFile(ActionEvent e){
        //tạo tab cho file mới
        Tab newTab= new Tab("Untitled");
        newTab.getStyleClass().add("name_tab");
        Button newButtonClose=new Button("x");
        newButtonClose.getStyleClass().add("button");
        newTab.setGraphic(newButtonClose);
        newButtonClose.setOnAction(event->closeTab(newTab));
        MenuBar menuBar=new MenuBar();
        menuBar.getStyleClass().add("menu_bar");
        Menu newMenu=new Menu("File");
        Menu newMenu1=new Menu("Edit");
        Menu newMenu2=new Menu("Help");
        MenuItem newItem1=new MenuItem("New File");
        MenuItem newItem2=new MenuItem("Open File");
        MenuItem newItem3=new MenuItem("Open Folder");
        MenuItem newItem4=new MenuItem("Save");
        MenuItem newItem5=new MenuItem("Save As");
        MenuItem newItem6=new MenuItem("Close");
        newMenu.getItems().addAll(newItem1,newItem2,newItem3,newItem4,newItem5,newItem6);
        menuBar.getMenus().addAll(newMenu, newMenu1, newMenu2);
        TextArea newTA=new TextArea();
        newTA.getStyleClass().add("text_area");
        VBox newVBox=new VBox();
        newVBox.getChildren().addAll(menuBar,newTA);
        VBox.setVgrow(newTA, Priority.ALWAYS);//cho Text Area chiếm hết chỗ trống
        newTab.setContent(newVBox);
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
        //sử dụng lambda expression xử lí sự kiện
        newItem1.setOnAction(event->{
            NewFile(e);
        });
        newItem2.setOnAction(event->{
            OpenFile(e);
        });
        newItem3.setOnAction(event->{
            OpenFolder(e);
        });
        newItem4.setOnAction(event -> {
            Save(e);
        });
        newItem5.setOnAction(event->{
            SaveAs(e);
        });
        newItem6.setOnAction(event->{
            Close(e);
        });
        this.file=null;
    }

    public void closeTab(Tab tab){
        tabPane.getTabs().remove(tab);
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if(tabPane.getSelectionModel().getSelectedItem()==null){
            Close(new ActionEvent());
        }
        if(tabPane.getSelectionModel().getSelectedItem().getText().equals("Untitled")){
            this.file=null;
        }
        else{
            String fileName=tabPane.getSelectionModel().getSelectedItem().getText()+".txt";
            for (int i=0;i<filecon.getNameFile().size();i++){
                if(filecon.getNameFile().get(i).equals(fileName)){
                    this.file= new File(filecon.getAbsolutePath().get(i));
                }
            }
        }
    }
    public void OpenFile(ActionEvent e) {
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Open");
        FileChooser.ExtensionFilter stylefile1= new FileChooser.ExtensionFilter("Text documents(*.txt)","*.txt");
        FileChooser.ExtensionFilter stylefile2= new FileChooser.ExtensionFilter("All files", "*.*");
        fileChooser.getExtensionFilters().addAll(stylefile1,stylefile2);
        File fileopen= fileChooser.showOpenDialog(null);
        if(fileopen!=null){
            //tạo tab cho file đã mở
            Tab newTab= new Tab(fileopen.getName().replace(".txt",""));
            newTab.getStyleClass().add("name_tab");
            Button newButtonClose=new Button("x");
            newButtonClose.getStyleClass().add("button");
            newTab.setGraphic(newButtonClose);
            newButtonClose.setOnAction(event-> closeTab(newTab));
            MenuBar menuBar=new MenuBar();
            menuBar.getStyleClass().add("menu_bar");
            Menu newMenu=new Menu("File");
            Menu newMenu1=new Menu("Edit");
            Menu newMenu2=new Menu("Help");
            MenuItem newItem1=new MenuItem("New File");
            MenuItem newItem2=new MenuItem("Open File");
            MenuItem newItem3=new MenuItem("Open Folder");
            MenuItem newItem4=new MenuItem("Save");
            MenuItem newItem5=new MenuItem("Save As");
            MenuItem newItem6=new MenuItem("Close");
            newMenu.getItems().addAll(newItem1,newItem2,newItem3,newItem4,newItem5,newItem6);
            menuBar.getMenus().addAll(newMenu, newMenu1, newMenu2);
            TextArea newTA=new TextArea();
            newTA.getStyleClass().add("text_area");
            VBox newVBox=new VBox();
            newVBox.getChildren().addAll(menuBar,newTA);
            VBox.setVgrow(newTA, Priority.ALWAYS);
            newTab.setContent(newVBox);
            tabPane.getTabs().add(newTab);
            tabPane.getSelectionModel().select(newTab);
            //sử dụng lambda expression xử lí sự kiện
            newItem1.setOnAction(event->{
                NewFile(e);
            });
            newItem2.setOnAction(event->{
                OpenFile(e);
            });
            newItem3.setOnAction(event->{
                OpenFolder(e);
            });
            newItem4.setOnAction(event -> {
                Save(e);
            });
            newItem5.setOnAction(event->{
                SaveAs(e);
            });
            newItem6.setOnAction(event->{
                Close(e);
            });
            //đọc file
            try {
                BufferedReader read= new BufferedReader(new FileReader(fileopen));
                StringBuilder content= new StringBuilder();
                String line;
                while ((line =read.readLine())!=null){
                    content.append(line).append("\n");
                }
                newTA.setText(content.toString());
                stage = (Stage) tabPane.getScene().getWindow();
                stage.setTitle("Text Editor");
                this.file=fileopen;
                filecon.addAbsolutePath(file);
                filecon.addNameFile(file);
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void OpenFolder(ActionEvent e){
        DirectoryChooser folderChooser=new DirectoryChooser();
        folderChooser.setTitle("Open Folder");
        File selectedFolder = folderChooser.showDialog(null);
        if (selectedFolder != null) {
            Tab newTab = new Tab("Folder: "+selectedFolder.getName());
            newTab.getStyleClass().add("name_tab");
            Button newButtonClose=new Button("x");
            newButtonClose.getStyleClass().add("button");
            newTab.setGraphic(newButtonClose);
            newButtonClose.setOnAction(event-> closeTab(newTab));
            VBox vBox=new VBox();
            SplitPane splitPane=new SplitPane();
            MenuBar menuBar=new MenuBar();
            menuBar.getStyleClass().add("menu_bar");
            Menu newMenu=new Menu("File");
            Menu newMenu1=new Menu("Edit");
            Menu newMenu2=new Menu("Help");
            MenuItem newItem1=new MenuItem("New File");
            MenuItem newItem2=new MenuItem("Open File");
            MenuItem newItem3=new MenuItem("Open Folder");
            MenuItem newItem4=new MenuItem("Save");
            MenuItem newItem5=new MenuItem("Save As");
            MenuItem newItem6=new MenuItem("Close");
            newMenu.getItems().addAll(newItem1,newItem2,newItem3,newItem4,newItem5,newItem6);
            menuBar.getMenus().addAll(newMenu, newMenu1, newMenu2);
            // TreeView
            TreeView<String> folderTreeView = new TreeView<>(new TreeItem<>(selectedFolder.getName()));
            folderTreeView.setPrefHeight(635);
            folderTreeView.setMinWidth(200);
            folderTreeView.setMaxWidth(200);
            TreeView(selectedFolder, folderTreeView.getRoot());
            splitPane.getItems().addAll(folderTreeView);
            vBox.getChildren().addAll(menuBar,splitPane);
            newTab.setContent(vBox);
            tabPane.getTabs().add(newTab);
            tabPane.getSelectionModel().select(newTab);

            // Bắt sự kiện khi chọn một tập tin trong TreeView
            folderTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && newValue.isLeaf()) {
                    String selectedFileName = newValue.getValue();
                    File selectedFile = new File(selectedFolder.getAbsolutePath() + File.separator + selectedFileName);
                    if (selectedFile.isFile()) {
                        // Gọi phương thức OpenFileInFolderOpened với tập tin và thư mục hiện tại
                        OpenFileInFolderOpened(e, selectedFile, selectedFolder);
                    }
                }
            });
            newItem1.setOnAction(event->{
                NewFile(e);
            });
            newItem2.setOnAction(event->{
                OpenFile(e);
            });
            newItem3.setOnAction(event->{
                OpenFolder(e);
            });
            newItem4.setOnAction(event -> {
                Save(e);
            });
            newItem5.setOnAction(event->{
                SaveAs(e);
            });
            newItem6.setOnAction(event->{
                Close(e);
            });
        }
    }

    // dùng đệ quy duyệt thư mục
    public void TreeView(File selectedFolder, TreeItem<String> item) {
        File[] directory= selectedFolder.listFiles();
        if (directory != null) {
            for (File file : directory) {
                TreeItem<String> newItem = new TreeItem<>(file.getName());
                item.getChildren().add(newItem);
                if (file.isDirectory()) {
                    TreeView(file, newItem);
                }
            }
        }
    }

    void OpenFileInFolderOpened(ActionEvent e,File fileopen,File selectedFolder){
        //tạo tab cho file đã mở
        Tab newTab= new Tab(fileopen.getName().replace(".txt",""));
        newTab.getStyleClass().add("name_tab");
        Button newButtonClose=new Button("x");
        newButtonClose.getStyleClass().add("button");
        newTab.setGraphic(newButtonClose);
        newButtonClose.setOnAction(event-> closeTab(newTab));
        VBox vBox=new VBox();
        SplitPane splitPane=new SplitPane();
        MenuBar menuBar=new MenuBar();
        menuBar.getStyleClass().add("menu_bar");
        Menu newMenu=new Menu("File");
        Menu newMenu1=new Menu("Edit");
        Menu newMenu2=new Menu("Help");
        MenuItem newItem1=new MenuItem("New File");
        MenuItem newItem2=new MenuItem("Open File");
        MenuItem newItem3=new MenuItem("Open Folder");
        MenuItem newItem4=new MenuItem("Save");
        MenuItem newItem5=new MenuItem("Save As");
        MenuItem newItem6=new MenuItem("Close");
        newMenu.getItems().addAll(newItem1,newItem2,newItem3,newItem4,newItem5,newItem6);
        menuBar.getMenus().addAll(newMenu, newMenu1, newMenu2);
        // TreeView
        TreeView<String> folderTreeView = new TreeView<>(new TreeItem<>(selectedFolder.getName()));
        folderTreeView.setPrefHeight(635);
        folderTreeView.setMinWidth(200);
        folderTreeView.setMaxWidth(200);
        TreeView(selectedFolder, folderTreeView.getRoot());
        TextArea newTA=new TextArea();
        newTA.getStyleClass().add("text_area");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileopen));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            newTA.setText(content.toString());
            textarea = newTA;
            reader.close();
            this.file=fileopen;
            filecon.addAbsolutePath(file);
            filecon.addNameFile(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        splitPane.getItems().addAll(folderTreeView,newTA);
        vBox.getChildren().addAll(menuBar,splitPane);
        newTab.setContent(vBox);
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
        //Bắt sự kiện khi chọn file trong tree view
        folderTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isLeaf()) {
                String selectedNewFileName = newValue.getValue();
                File newSelectedFile = new File(selectedFolder.getAbsolutePath() + File.separator + selectedNewFileName);
                if (newSelectedFile.isFile()) {
                    OpenFileInFolderOpened(e, newSelectedFile, selectedFolder);
                }
            }
        });
        newItem1.setOnAction(event->{
            NewFile(e);
        });
        newItem2.setOnAction(event->{
            OpenFile(e);
        });
        newItem3.setOnAction(event->{
            OpenFolder(e);
        });
        newItem4.setOnAction(event -> {
            Save(e);
        });
        newItem5.setOnAction(event->{
            SaveAs(e);
        });
        newItem6.setOnAction(event->{
            Close(e);
        });
    }

    public void Save(ActionEvent e) {
        //kiểm tra xem file này là file đã tồn tại hay chưa nếu tồn tại thì chỉ cần lưu lại nội dung edit còn chưa tồn tại thì tiến hành chọn nơi và lưu file
        if(file!=null){
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(textarea.getText());
                fileWriter.close();
                Tab selectTab=tabPane.getSelectionModel().getSelectedItem();
                selectTab.setText(file.getName().replace(".txt",""));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save");
            FileChooser.ExtensionFilter stylefile1= new FileChooser.ExtensionFilter("Text documents(*.txt)","*.txt");
            FileChooser.ExtensionFilter stylefile2=new FileChooser.ExtensionFilter("All files","*.*");
            fileChooser.getExtensionFilters().addAll(stylefile1,stylefile2);
            fileChooser.setInitialFileName("Untitled");
            stage = (Stage) tabPane.getScene().getWindow(); // Lấy stage hiện tại
            File filesave = fileChooser.showSaveDialog(stage); // Truyền stage cho hộp thoại lưu

            if(filesave!=null){
                try {
                    FileWriter fileWriter= new FileWriter(filesave);
                    fileWriter.write(String.valueOf(textarea.getText()));
                    fileWriter.close();
                    Tab selectTab=tabPane.getSelectionModel().getSelectedItem();
                    selectTab.setText(filesave.getName().replace(".txt",""));
                    this.file=filesave;
                    filecon.addAbsolutePath(file);
                    filecon.addNameFile(file);
                    List<String> lines= new ArrayList<>();
                    BufferedReader bufferedReader= new BufferedReader(new FileReader(file));
                    String line;
                    while ((line=bufferedReader.readLine())!=null){
                        lines.add(line);
                    }
                    // Sử dụng Stream() cho Collection (ArrayList, List …)
                    List<String> content= new ArrayList<>();
                    content=lines.stream().flatMap(l-> Arrays.stream(l.split("\\s+"))).collect(Collectors.toList());
                    filecon.addFileContent(content);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
    //Lưu file đã tồn tại thành file khác
    public void SaveAs(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        FileChooser.ExtensionFilter stylefile1= new FileChooser.ExtensionFilter("Text documents(*.txt)","*.txt");
        FileChooser.ExtensionFilter stylefile2=new FileChooser.ExtensionFilter("All files","*.*");
        fileChooser.getExtensionFilters().addAll(stylefile1,stylefile2);
        fileChooser.setInitialDirectory(this.file.getParentFile());
        fileChooser.setInitialFileName(this.file.getName());
        File filesaveas=fileChooser.showSaveDialog(null);
        if(filesaveas!=null){
            try {
                FileWriter fileWriter= new FileWriter(filesaveas);
                fileWriter.write(String.valueOf(textarea.getText()));
                fileWriter.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    //thoát chương trình
    public void Close(ActionEvent e) {
        Platform.exit();
        System.exit(0);
    }
    //Đưa sự kiện vào các menuitem sử dụng lambda expression
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Tab initialTab= tabPane.getTabs().get(0);
        Tab checkTab=tabPane.getSelectionModel().getSelectedItem();
        //Kiểm tra nếu tên mặc định thì đặt file ban đầu là null
        if(checkTab.getText().equals("Untitled")){
            this.file=null;
        }

        new_file_item.setOnAction(e->NewFile(e));
        close_btn.setOnAction(e->{
            if(e.getSource()==initialTab.getGraphic()){
                closeTab(initialTab);
            }
        });
        open_file_item.setOnAction(e-> {
            OpenFile(e);
        });
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if(newTab==initialTab){
                textarea = textarea_code;
            }
            else if (newTab != null ) {
                // Lấy TextArea từ Tab mới được chọn
                VBox vBox=(VBox) newTab.getContent();
                if(vBox.getChildren().get(1) instanceof TextArea){
                    textarea = (TextArea) vBox.getChildren().get(1); // index 1 vì menuBar ở index 0, TextArea ở index 1
                }
                else{
                    SplitPane splitPane=(SplitPane) vBox.getChildren().get(1);
                    if(splitPane.getItems().size()>1){
                        if(splitPane.getItems().get(1) instanceof TextArea){
                            textarea = (TextArea) splitPane.getItems().get(1);
                        }
                    }
                    else {
                        textarea=null;
                    }

                }
            }
        });
        open_folder_item.setOnAction(e->OpenFolder(e));
        save_item.setOnAction(e->Save(e));
        save_as_item.setOnAction(e->SaveAs(e));
        exit_item.setOnAction(e->Close(e));
    }
}

