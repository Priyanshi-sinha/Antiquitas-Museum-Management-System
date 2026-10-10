package com.museum.controller;

import com.museum.context.AppState;
import com.museum.model.User;
import com.museum.util.Navigator;
import com.museum.util.Theme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class NavBar extends HBox {

    private final AppState S = AppState.get();
    private String active = "home";

    public NavBar() {
        setMinHeight(40); setMaxHeight(40);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 8, 0, 8));
        setSpacing(10);
        setStyle("-fx-background-color:" + Theme.BG_NAVBAR + ";-fx-border-color:transparent transparent " + Theme.BORDER + " transparent;");
        S.currentUserProp().addListener((o, a, b) -> rebuild());
        rebuild();
    }

    public void setActive(String page) { this.active = page; rebuild(); }

    private void rebuild() {
        getChildren().clear();

        // Logo
        Label m = new Label("M");
        m.setStyle("-fx-background-color:" + Theme.GOLD + ";-fx-text-fill:#1a1d2e;-fx-font-weight:bold;-fx-font-size:12px;-fx-min-width:22px;-fx-min-height:22px;-fx-max-width:22px;-fx-max-height:22px;-fx-alignment:center;-fx-background-radius:3;");
        Label brand = new Label("NIT KKR MUSEUM");
        brand.setStyle("-fx-text-fill:" + Theme.GOLD + ";-fx-font-size:12px;-fx-font-weight:bold;");
        HBox logo = new HBox(6, m, brand);
        logo.setAlignment(Pos.CENTER_LEFT);
        logo.setPadding(new Insets(0, 6, 0, 0));
        logo.setCursor(Cursor.HAND);
        logo.setOnMouseClicked(e -> nav("home"));
        getChildren().add(logo);

        addDiv();
        addBtn("Home",   "home");
        addBtn("Browse", "browse");
        addBtn("ChatBot","chatbot");

        if (S.isCurator()) {
            addDiv();
            addBtn("+ Add Item", "additem");
            addBtn("Logs",       "logs");
        }
        if (S.isAdmin()) {
            addDiv();
            addBtn("Staff",    "staff");
            addBtn("Register", "register");
        }

        getChildren().add(hSpacer());

        User u = S.getCurrentUser();
        if (u != null) {
            String rc = u.isAdmin() ? Theme.PURPLE : Theme.GREEN;
            Label av = new Label(String.valueOf(u.name.charAt(0)));
            av.setStyle("-fx-background-color:" + Theme.GOLD + ";-fx-text-fill:#1a1d2e;-fx-font-weight:bold;-fx-font-size:10px;-fx-min-width:20px;-fx-min-height:20px;-fx-max-width:20px;-fx-max-height:20px;-fx-alignment:center;-fx-background-radius:10;");
            String shortName = u.name.split(" ").length > 1 ? u.name.split(" ")[0] + " " + u.name.split(" ")[1].charAt(0) + "." : u.name;
            Label nm  = new Label(shortName);
            nm.setStyle("-fx-text-fill:" + Theme.TEXT_SECONDARY + ";-fx-font-size:11px;");
            Label rl  = new Label(u.role);
            rl.setStyle("-fx-background-color:" + rc + "22;-fx-text-fill:" + rc + ";-fx-border-color:" + rc + "55;-fx-border-radius:3;-fx-background-radius:3;-fx-font-size:9px;-fx-padding:1 5;");
            HBox chip = new HBox(5, av, nm, rl);
            chip.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT-1);
            chip.setAlignment(Pos.CENTER_LEFT);
            chip.setPadding(new Insets(0, 8, 0, 8));
            chip.setStyle("-fx-background-color:" + Theme.BG_PANEL + ";-fx-border-color:" + Theme.BORDER_INPUT + ";-fx-border-radius:4;-fx-background-radius:4;");
            Button logout = new Button("Logout");
            logout.setStyle("-fx-background-color:transparent;-fx-text-fill:" + Theme.RED + ";-fx-font-size:11px;-fx-padding:4 8;-fx-cursor:hand;");
            logout.setOnAction(e -> { S.logout(); nav("home"); });
            getChildren().addAll(chip, logout);
        } else {
            addBtn("Login", "login");
        }
    }

    private void addBtn(String text, String page) {
        boolean act = active.equals(page);
        Button b = new Button(text);
        if (act) {
            b.setStyle("-fx-background-color:" + Theme.GOLD + ";-fx-text-fill:#1a1d2e;-fx-font-size:12px;-fx-padding:4 10;-fx-background-radius:4;-fx-cursor:hand;-fx-font-weight:600;");
        } else {
            b.setStyle("-fx-background-color:transparent;-fx-text-fill:" + Theme.TEXT_SECONDARY + ";-fx-font-size:12px;-fx-padding:4 10;-fx-background-radius:4;-fx-cursor:hand;");
            b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:" + Theme.BG_INPUT + ";-fx-text-fill:" + Theme.TEXT_PRIMARY + ";-fx-font-size:12px;-fx-padding:4 10;-fx-background-radius:4;-fx-cursor:hand;"));
            b.setOnMouseExited(e  -> b.setStyle("-fx-background-color:transparent;-fx-text-fill:" + Theme.TEXT_SECONDARY + ";-fx-font-size:12px;-fx-padding:4 10;-fx-background-radius:4;-fx-cursor:hand;"));
        }
        b.setOnAction(e -> nav(page));
        getChildren().add(b);
    }

    private void addDiv() {
        Region r = new Region();
        r.setMinSize(1,20); r.setMaxSize(1,20);
        r.setStyle("-fx-background-color:" + Theme.GOLD + ";"+"-fx-background-radius:50;-fx-opacity: 0.5");
        getChildren().add(r);
    }

    private Region hSpacer() { Region r = new Region(); HBox.setHgrow(r, Priority.ALWAYS); return r; }

    // Keep a reference so other pages can call nav()
    private static NavBar INSTANCE;
    public static NavBar getInstance() { return INSTANCE; }

    public void nav(String page) {
        setActive(page);
        Navigator.go(switch (page) {
            case "home"     -> new HomePage();
            case "browse"   -> new BrowsePage("All");
            case "chatbot"  -> new ChatBotPage();
            case "login"    -> new LoginPage();
            case "register" -> new RegisterPage();
            case "additem"  -> new ItemAddEditPage(null, "Artifact");
            case "logs"     -> new LogPage();
            case "staff"    -> new StaffPage();
            default         -> new HomePage();
        });
    }

    { INSTANCE = this; }
}
