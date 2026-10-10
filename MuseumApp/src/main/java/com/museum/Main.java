package com.museum;

import com.museum.controller.HomePage;
import com.museum.controller.NavBar;
import com.museum.util.Navigator;
import com.museum.util.Theme;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        NavBar navBar = new NavBar();

        StackPane content = new StackPane(new HomePage());
        content.setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
        VBox.setVgrow(content, Priority.ALWAYS);

        Navigator.init(content, () -> {});

        VBox root = new VBox(navBar, content);
        root.setStyle("-fx-background-color:" + Theme.BG_DARK + ";");

        Scene scene = new Scene(root, 1240, 800);
        scene.getStylesheets().add(
            getClass().getResource("/com/museum/style.css").toExternalForm()
        );

        stage.setTitle("NIT KKR Museum");
        stage.setScene(scene);
        stage.setMinWidth(960);
        stage.setMinHeight(640);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
