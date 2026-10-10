package com.museum.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public final class UI {

    public static Label label(String text, String color, double size) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill:" + color + ";-fx-font-size:" + size + "px;");
        return l;
    }
    public static Label label(String text, String color, double size, boolean bold) {
        Label l = label(text, color, size);
        if (bold) l.setStyle(l.getStyle() + "-fx-font-weight:bold;");
        return l;
    }

    public static Button goldBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + Theme.GOLD + ";-fx-text-fill:#1a1d2e;-fx-font-size:12px;-fx-padding:5 14;-fx-background-radius:4;-fx-cursor:hand;-fx-font-weight:600;");
        b.setOnMouseEntered(e -> b.setOpacity(0.85));
        b.setOnMouseExited(e  -> b.setOpacity(1.0));
        return b;
    }
    public static Button ghostBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:transparent;-fx-text-fill:" + Theme.TEXT_SECONDARY + ";-fx-font-size:12px;-fx-padding:5 12;-fx-background-radius:4;-fx-cursor:hand;-fx-border-color:" + Theme.BORDER + ";-fx-border-radius:4;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:" + Theme.BG_INPUT + ";-fx-text-fill:" + Theme.TEXT_PRIMARY + ";-fx-font-size:12px;-fx-padding:5 12;-fx-background-radius:4;-fx-cursor:hand;-fx-border-color:" + Theme.BORDER + ";-fx-border-radius:4;"));
        b.setOnMouseExited(e  -> b.setStyle("-fx-background-color:transparent;-fx-text-fill:" + Theme.TEXT_SECONDARY + ";-fx-font-size:12px;-fx-padding:5 12;-fx-background-radius:4;-fx-cursor:hand;-fx-border-color:" + Theme.BORDER + ";-fx-border-radius:4;"));
        return b;
    }
    public static Button dangerBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + Theme.RED + "22;-fx-text-fill:" + Theme.RED + ";-fx-font-size:11px;-fx-padding:4 10;-fx-background-radius:4;-fx-cursor:hand;-fx-border-color:" + Theme.RED + "44;-fx-border-radius:4;");
        return b;
    }

    public static TextField textField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color:" + Theme.BG_INPUT + ";-fx-text-fill:" + Theme.TEXT_PRIMARY + ";-fx-prompt-text-fill:" + Theme.TEXT_DIMMER + ";-fx-border-color:" + Theme.BORDER_INPUT + ";-fx-border-radius:4;-fx-background-radius:4;-fx-font-size:12px;-fx-padding:6 8;");
        return tf;
    }
    public static PasswordField passField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setStyle("-fx-background-color:" + Theme.BG_INPUT + ";-fx-text-fill:" + Theme.TEXT_PRIMARY + ";-fx-prompt-text-fill:" + Theme.TEXT_DIMMER + ";-fx-border-color:" + Theme.BORDER_INPUT + ";-fx-border-radius:4;-fx-background-radius:4;-fx-font-size:12px;-fx-padding:6 8;");
        return pf;
    }
    public static TextArea textArea(String prompt) {
        TextArea ta = new TextArea();
        ta.setPromptText(prompt);
        ta.setStyle("-fx-control-inner-background:" + Theme.BG_INPUT + ";-fx-background-color:" + Theme.BG_INPUT + ";-fx-text-fill:" + Theme.TEXT_PRIMARY + ";-fx-prompt-text-fill:" + Theme.TEXT_DIMMER + ";-fx-border-color:" + Theme.BORDER_INPUT + ";-fx-border-radius:4;-fx-background-radius:4;-fx-font-size:12px;");
        ta.setWrapText(true);
        return ta;
    }
    public static <T> ComboBox<T> combo() {
        ComboBox<T> cb = new ComboBox<>();
        cb.setStyle("-fx-background-color:" + Theme.BG_INPUT + ";-fx-text-fill:" + Theme.TEXT_PRIMARY + ";-fx-border-color:" + Theme.BORDER_INPUT + ";-fx-border-radius:4;-fx-background-radius:4;");
        return cb;
    }
    public static CheckBox checkBox(String text) {
        CheckBox cb = new CheckBox(text);
        cb.setStyle("-fx-text-fill:" + Theme.TEXT_SECONDARY + ";-fx-font-size:12px;");
        return cb;
    }

    public static Label badge(String text, String color) {
        Label l = new Label(text);
        l.setStyle("-fx-background-color:" + color + "22;-fx-text-fill:" + color + ";-fx-border-color:" + color + "44;-fx-border-radius:3;-fx-background-radius:3;-fx-font-size:10px;-fx-padding:1 6;");
        return l;
    }

    public static ScrollPane scrollPane(Node content) {
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:transparent;-fx-background:" + Theme.BG_DARK + ";");
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return sp;
    }

    public static VBox formField(String labelText, Node input) {
        Label l = new Label(labelText.toUpperCase());
        l.setStyle("-fx-text-fill:" + Theme.TEXT_DIMMER + ";-fx-font-size:10px;-fx-padding:0 0 3 0;");
        return new VBox(4, l, input);
    }

    public static Region hSpacer() { Region r = new Region(); HBox.setHgrow(r, Priority.ALWAYS); return r; }
    public static Region vSpacer() { Region r = new Region(); VBox.setVgrow(r, Priority.ALWAYS); return r; }

    public static Separator separator() {
        Separator s = new Separator();
        s.setStyle("-fx-background-color:" + Theme.BORDER + ";");
        return s;
    }

    public static HBox hbox(int spacing, Node... children) {
        HBox h = new HBox(spacing, children);
        h.setAlignment(Pos.CENTER_LEFT);
        return h;
    }
    public static VBox vbox(int spacing, Node... children) {
        return new VBox(spacing, children);
    }

    public static VBox card(Node... children) {
        VBox v = new VBox(10, children);
        v.setPadding(new Insets(14));
        v.setStyle("-fx-background-color:" + Theme.BG_PANEL + ";-fx-border-color:" + Theme.BORDER + ";-fx-border-radius:6;-fx-background-radius:6;");
        return v;
    }

    public static String fmt(String iso) {
        if (iso == null || iso.isEmpty()) return "—";
        try { return iso.substring(0, 10); } catch (Exception e) { return iso; }
    }
}
