package com.museum.controller;

import com.museum.context.AppState;
import com.museum.model.User;
import com.museum.util.Theme;
import com.museum.util.UI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class RegisterPage extends VBox {

    private final AppState S = AppState.get();
    private TextField nameF, usernameF, emailF, phoneF, addressF, dobF;
    private PasswordField passF, confirmF;
    private ComboBox<String> genderCB, roleCB;

    public RegisterPage() {
        setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
        VBox.setVgrow(this, Priority.ALWAYS);
        if (!S.isAdmin()) {
            setAlignment(Pos.CENTER);
            getChildren().addAll(
                UI.label("Access Denied", Theme.RED, 18, true),
                UI.label("Admins only.", Theme.TEXT_MUTED, 12));
            return;
        }
        ScrollPane sp = UI.scrollPane(build());
        VBox.setVgrow(sp, Priority.ALWAYS);
        getChildren().add(sp);
    }

    private VBox build() {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color:" + Theme.BG_DARK + ";");

        // Header — back button only, no save button here
        HBox hdr = new HBox(10);
        hdr.setPadding(new Insets(10, 16, 10, 16));
        hdr.setAlignment(Pos.CENTER_LEFT);
        hdr.setStyle("-fx-background-color:" + Theme.BG_HEADER
            + ";-fx-border-color:transparent transparent " + Theme.BORDER + " transparent;");
        Button back = UI.ghostBtn("\u2190 Staff");
        back.setOnAction(e -> NavBar.getInstance().nav("staff"));
        Label title = UI.label("Register Staff Member", Theme.TEXT_PRIMARY, 14, true);
        hdr.getChildren().addAll(back, title);
        root.getChildren().add(hdr);

        // Fields
        nameF     = UI.textField("Full name");
        usernameF = UI.textField("Username");
        passF     = UI.passField("Password");
        confirmF  = UI.passField("Confirm password");
        emailF    = UI.textField("email@example.com");
        phoneF    = UI.textField("+1 555-0000");
        addressF  = UI.textField("Street, City");
        dobF      = UI.textField("YYYY-MM-DD");

        genderCB = UI.combo();
        genderCB.getItems().addAll("Male", "Female", "Other", "Prefer not to say");
        genderCB.setValue("Male");
        genderCB.setMaxWidth(Double.MAX_VALUE);

        roleCB = UI.combo();
        roleCB.getItems().addAll("Curator", "Admin");
        roleCB.setValue("Curator");
        roleCB.setMaxWidth(Double.MAX_VALUE);

        // Grid layout
        GridPane g = new GridPane();
        g.setHgap(12); g.setVgap(14);
        for (int i = 0; i < 2; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(50);
            g.getColumnConstraints().add(cc);
        }
        g.add(UI.formField("Full Name *",       nameF),     0, 0);
        g.add(UI.formField("Username *",         usernameF), 1, 0);
        g.add(UI.formField("Password *",         passF),     0, 1);
        g.add(UI.formField("Confirm Password *", confirmF),  1, 1);
        g.add(UI.formField("Email",              emailF),    0, 2);
        g.add(UI.formField("Phone",              phoneF),    1, 2);
        g.add(UI.formField("Address",            addressF),  0, 3, 2, 1);
        g.add(UI.formField("Date of Birth",      dobF),      0, 4);
        g.add(UI.formField("Gender",             genderCB),  1, 4);
        g.add(UI.formField("Role *",             roleCB),    0, 5);

        // Submit button — full width, below all form fields
        Button saveBtn = UI.goldBtn("Register Staff Member");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setPadding(new Insets(10));
        saveBtn.setOnAction(e -> register());

        // Assemble: title → card with grid → submit button
        VBox formCard = UI.card(g);

        VBox content = new VBox(16,
            UI.label("Staff Account Details", Theme.TEXT_PRIMARY, 14, true),
            formCard,
            saveBtn);
        content.setPadding(new Insets(24, 32, 32, 32));
        content.setMaxWidth(720);

        HBox centered = new HBox(content);
        centered.setAlignment(Pos.TOP_CENTER);
        centered.setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
        root.getChildren().add(centered);
        return root;
    }

    private void register() {
        String n   = nameF.getText().trim();
        String un  = usernameF.getText().trim();
        String pw  = passF.getText();
        String cpw = confirmF.getText();

        if (n.isEmpty() || un.isEmpty() || pw.isEmpty()) {
            alert("Name, username and password are required."); return;
        }
        if (!pw.equals(cpw)) {
            alert("Passwords do not match."); return;
        }
        if (S.getUsers().stream().anyMatch(u -> u.username.equals(un))) {
            alert("Username \"" + un + "\" is already taken."); return;
        }

        User u = new User();
        u.name     = n;
        u.username = un;
        u.password = pw;
        u.email    = emailF.getText().trim();
        u.phone    = phoneF.getText().trim();
        u.address  = addressF.getText().trim();
        u.dob      = dobF.getText().trim();
        u.gender   = genderCB.getValue();
        u.role     = roleCB.getValue();
        S.addUser(u);

        Alert ok = new Alert(Alert.AlertType.INFORMATION,
            "Staff member \"" + n + "\" registered successfully!", ButtonType.OK);
        ok.setHeaderText("Registration Successful");
        ok.showAndWait();
        NavBar.getInstance().nav("staff");
    }

    private void alert(String m) {
        Alert a = new Alert(Alert.AlertType.WARNING, m, ButtonType.OK);
        a.setHeaderText("Validation Error");
        a.showAndWait();
    }
}
