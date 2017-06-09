package bpm.util;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by vivek on 09/06/2017.
 */
public class MainUIController implements Initializable {

    @FXML
    private MenuBar menuBar;

    @FXML
    private TableView tableView;
    private ObservableList<EnvVarModel> envVarData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        tableView.setItems(envVarData);
    }

    @FXML
    private void onImportTWXFileClick(final ActionEvent event) {
        System.out.print("you clicked this");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if (file != null) {
            System.out.print("you have selected " + file.getName());
            new Thread(new LoadEnvVarFromTWXTask(file, new LoadEnvVarFromTWXTask.Callback() {
                @Override
                public void onComplete(List<EnvVarModel> envs) {

                    System.out.print("retrived " + envs.size());

                    //envVarData.removeAll();
                    //envVarData.addAll(envs);
                    tableView.getItems().clear();
                    tableView.getItems().addAll(envs);
                }
            })).start();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuBar.setFocusTraversable(true);
    }
}
