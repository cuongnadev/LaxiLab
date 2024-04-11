package org.example.laxilab.Controller;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import org.example.laxilab.Other.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;




public class LaxiLabController implements Initializable {
    public TextArea textarea_code;
    public TreeView<String> tree_view;
    public MenuBar menu_bar;
    public MenuItem new_file_item;
    public MenuItem open_item;
    public MenuItem exit_item;
    public MenuItem save_item;
    private File currentFile;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new_file_item.setOnAction(event -> {
            textarea_code.clear();
            currentFile = null;
        });
        open_item.setOnAction(event -> openFile());
        save_item.setOnAction(event -> saveFile());
        exit_item.setOnAction(event -> System.exit(0));
        // Gắn sự kiện cho các TreeItem
        tree_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Kiểm tra nếu TreeItem là một file
                if (Files.isRegularFile(getPath(newValue))) {
                    // Hiển thị nội dung của file trong textarea_code
                    try {
                        String content = Files.readString(getPath(newValue), StandardCharsets.UTF_8);
                        textarea_code.setText(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Xử lý lỗi khi không thể đọc file
                    }
                }
                for (TreeItem<String> item : tree_view.getSelectionModel().getSelectedItems()){
                    if (Files.isRegularFile(getPath(item))) {
                        try {
                            String content = Files.readString(getPath(item), StandardCharsets.UTF_8);
                            textarea_code.setText(content);
                            currentFile = getPath(item).toFile();
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Xử lý lỗi khi không thể đọc file
                        }
                    }else {
                        System.out.println(getPath(item) + " is not a file.");
                    }
                }
            }
        });

        // Keyboard shortcut for Save
        save_item.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        // Xử lý sự kiện khi nhấn tổ hợp phím Ctrl + S
        textarea_code.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                saveFile();
            }
        });
    }


    //Open
    private void openFile()  {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Directory");
        File selectedDirectory = fileChooser.showOpenDialog(null);
        if (selectedDirectory != null) {
            currentFile = selectedDirectory;;
            Path rootPath = selectedDirectory.toPath();

            // tạo lại tree
            rebuildTreeView(selectedDirectory);

            // Kiểm tra nếu TreeItem là một file
            if (Files.isRegularFile(rootPath)) {
                // Hiển thị nội dung của file trong textarea_code
                try {
                    String content = Files.readString(rootPath);
                    textarea_code.setText(content);

                } catch (IOException e) {
                    e.printStackTrace();
                    // Xử lý lỗi khi không thể đọc file
                }
            }

        }
    }


    private void rebuildTreeView(File currentFile) {
        try {
            Path filePath = currentFile.toPath(); // Thay đổi này để xây dựng từ tệp đó
            Path parentPath = filePath.getParent(); // Lấy thư mục chứa tệp
            String rootName = parentPath.toString(); // Lấy tên của thư mục chứa tệp

            TreeItem<String> rootItem = new TreeItem<>(rootName);
            buildTree(parentPath, rootItem);
            tree_view.setRoot(rootItem);
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi khi không thể xây dựng cây thư mục
        }
    }
    private void buildTree(Path currentPath, TreeItem<String> parentItem) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath)) {
            for (Path entry : stream) {
                TreeItem<String> newItem = new TreeItem<>(entry.getFileName().toString());
                parentItem.getChildren().add(newItem);
                if (Files.isDirectory(entry)) {
                    buildTree(entry, newItem);
                }
            }
        }
    }

    private void saveFile() {
        if (currentFile != null) {
            saveToFile(currentFile);
        } else {
            saveAs();
        }
    }


    private void saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            currentFile = file;
            saveToFile(file);
            rebuildTreeView(file);
        }
    }


    private void saveToFile(File file) {
        try {
            FileUtils.writeFile(textarea_code.getText(), file);
        } catch (IOException ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to create file");
            errorAlert.setContentText( "Error saving file.");
            errorAlert.showAndWait();
        }
    }


    private Path getPath(TreeItem<String> item) {
        StringBuilder pathBuilder = new StringBuilder();

        // Duyệt qua các cha của TreeItem để xây dựng đường dẫn
        TreeItem<String> parent = item;


        while ((parent != null)) {
            pathBuilder.insert(0, parent.getValue() + "/");
            parent = parent.getParent();
        }

        return Path.of(pathBuilder.toString());
    }
}

