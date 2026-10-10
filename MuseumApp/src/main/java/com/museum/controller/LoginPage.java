package com.museum.controller;

import com.museum.context.AppState;
import com.museum.util.Theme;
import com.museum.util.UI;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class LoginPage extends VBox {
    private final AppState S = AppState.get();

    public LoginPage() {
        setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
        setAlignment(Pos.CENTER);
        VBox.setVgrow(this, Priority.ALWAYS);
        getChildren().add(buildPanel());
    }

    private VBox buildPanel() {
        VBox outer = new VBox(12); outer.setMaxWidth(340);

        // Main panel
        VBox panel = new VBox();
        panel.setStyle("-fx-background-color:" + Theme.BG_PANEL + ";-fx-border-color:" + Theme.BORDER + ";-fx-border-radius:6;-fx-background-radius:6;");

        HBox hdr = new HBox(10);
        hdr.setPadding(new Insets(14,16,14,16)); hdr.setAlignment(Pos.CENTER_LEFT);
        hdr.setStyle("-fx-background-color:" + Theme.BG_HEADER + ";-fx-border-color:transparent transparent " + Theme.BORDER + " transparent;");
        Label icon = new Label("→");
        icon.setStyle("-fx-background-color:" + Theme.GOLD + "22;-fx-text-fill:" + Theme.GOLD + ";-fx-min-width:30px;-fx-min-height:30px;-fx-max-width:30px;-fx-max-height:30px;-fx-alignment:center;-fx-background-radius:4;-fx-font-size:14px;-fx-border-color:" + Theme.GOLD + "44;-fx-border-radius:4;");
        VBox hInfo = new VBox(2, UI.label("Staff Login", Theme.TEXT_PRIMARY, 14, true), UI.label("Museum Management System", Theme.TEXT_DIMMER, 10));
        hdr.getChildren().addAll(icon, hInfo);

        VBox form = new VBox(14); form.setPadding(new Insets(18));

        Label errLbl = new Label();
        errLbl.setStyle("-fx-background-color:" + Theme.RED + "15;-fx-text-fill:" + Theme.RED + ";-fx-border-color:" + Theme.RED + "30;-fx-border-radius:4;-fx-background-radius:4;-fx-font-size:11px;-fx-padding:5 8;");
        errLbl.setVisible(false); errLbl.setManaged(false); errLbl.setWrapText(true);

        TextField userF = UI.textField("Enter your username");
        PasswordField passF = UI.passField("Enter your password");

        Button loginBtn = UI.goldBtn("Sign In");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(loginBtn.getStyle() + "-fx-font-size:13px;-fx-padding:8 12;");

        Runnable doLogin = () -> {
            String u = userF.getText().trim(), p = passF.getText();
            if (u.isEmpty() || p.isEmpty()) { err(errLbl,"Please enter username and password."); return; }
            loginBtn.setText("Authenticating…"); loginBtn.setDisable(true);
            PauseTransition pt = new PauseTransition(Duration.millis(350));
            pt.setOnFinished(ev -> {
                if (S.login(u, p)) NavBar.getInstance().nav("home");
                else { err(errLbl,"Invalid username or password."); loginBtn.setText("Sign In"); loginBtn.setDisable(false); }
            });
            pt.play();
        };
        loginBtn.setOnAction(e -> doLogin.run());
        passF.setOnAction(e -> doLogin.run());
        userF.setOnAction(e -> passF.requestFocus());

        HBox links = new HBox(8);
        Button homeL = navLink("← Home");   homeL.setOnAction(e -> NavBar.getInstance().nav("home"));
        Button browL = navLink("Browse");    browL.setOnAction(e -> NavBar.getInstance().nav("browse"));
        links.getChildren().addAll(homeL, UI.label("·", Theme.TEXT_DIMMER,11), browL);

        form.getChildren().addAll(errLbl, UI.formField("Username", userF), UI.formField("Password", passF), loginBtn, links);
        panel.getChildren().addAll(hdr, form);

        // Demo credentials
        VBox demo = new VBox(8); demo.setPadding(new Insets(12));
        demo.setStyle("-fx-background-color:" + Theme.BG_PANEL + ";-fx-border-color:" + Theme.BORDER + ";-fx-border-radius:6;-fx-background-radius:6;");
        demo.getChildren().add(UI.label("DEMO CREDENTIALS", Theme.TEXT_DIMMER, 10));
        Object[][] creds = {{"Admin","admin","admin123",Theme.PURPLE},{"Curator","curator1","curator123",Theme.GREEN},{"Guest","guest","guest123",Theme.BLUE}};
        for (Object[] c : creds) {
            String role=(String)c[0], un=(String)c[1], pw=(String)c[2], col=(String)c[3];
            HBox row = new HBox(); row.setAlignment(Pos.CENTER_LEFT); row.setPadding(new Insets(5,8,5,8));
            row.setCursor(Cursor.HAND);
            row.setStyle("-fx-border-color:" + col + "33;-fx-border-radius:4;-fx-background-radius:4;");
            Label rb = UI.badge(role, col); Label unL = UI.label(un, Theme.TEXT_MUTED, 11); unL.setPadding(new Insets(0,0,0,8));
            Label hint = UI.label("Click to fill", Theme.TEXT_DIMMER, 10);
            row.getChildren().addAll(rb, unL, UI.hSpacer(), hint);
            row.setOnMouseClicked(ev -> { userF.setText(un); passF.setText(pw); });
            row.setOnMouseEntered(ev -> row.setStyle("-fx-background-color:"+Theme.BG_INPUT+";-fx-border-color:"+col+"33;-fx-border-radius:4;-fx-background-radius:4;"));
            row.setOnMouseExited(ev  -> row.setStyle("-fx-border-color:"+col+"33;-fx-border-radius:4;-fx-background-radius:4;"));
            demo.getChildren().add(row);
        }
        outer.getChildren().addAll(panel, demo);
        return outer;
    }

    private void err(Label l, String msg) { l.setText(msg); l.setVisible(true); l.setManaged(true); }
    private Button navLink(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:transparent;-fx-text-fill:" + Theme.GOLD + ";-fx-font-size:11px;-fx-padding:0;-fx-cursor:hand;");
        return b;
    }
}
