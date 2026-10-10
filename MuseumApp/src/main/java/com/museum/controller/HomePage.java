package com.museum.controller;

import com.museum.context.AppState;
import com.museum.model.Artifact;
import com.museum.util.Navigator;
import com.museum.util.Theme;
import com.museum.util.UI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class HomePage extends VBox {
    private final AppState S = AppState.get();

    public HomePage() {
        setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
        VBox.setVgrow(this, Priority.ALWAYS);
        ScrollPane sp = UI.scrollPane(buildContent());
        VBox.setVgrow(sp, Priority.ALWAYS);
        getChildren().add(sp);
    }

    private VBox buildContent() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
        root.getChildren().add(buildHero());
        VBox main = new VBox(28);
        main.setPadding(new Insets(28, 48, 40, 48));
        main.setMaxWidth(920);
        main.getChildren().addAll(buildStats(), buildFeatured(), buildAbout(), buildCategoryNav());
        HBox center = new HBox(main);
        center.setAlignment(Pos.TOP_CENTER);
        center.setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
        root.getChildren().add(center);
        return root;
    }

    private StackPane buildHero() {
        StackPane hero = new StackPane();
        hero.setMinHeight(260); hero.setMaxHeight(260);
        hero.setStyle("-fx-background-color:#0d0f1a;");

        ImageView bg = new ImageView();
        bg.setFitWidth(1400); bg.setFitHeight(260);
        bg.setPreserveRatio(false);
        bg.setStyle("-fx-opacity:0.35;");
        try { bg.setImage(new Image("https://images.unsplash.com/photo-1763734546247-83a8792bf0eb?w=1400&q=70", true)); } catch (Exception ignored) {}

        VBox overlay = new VBox(10);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(20));

        HBox logoRow = new HBox(8);
        logoRow.setAlignment(Pos.CENTER);
        Label lIcon = new Label("M");
        lIcon.setStyle("-fx-background-color:" + Theme.GOLD + ";-fx-text-fill:#1a1d2e;-fx-font-weight:bold;-fx-font-size:16px;-fx-min-width:30px;-fx-min-height:30px;-fx-max-width:30px;-fx-max-height:30px;-fx-alignment:center;-fx-background-radius:4;");
        Label brand = new Label("ANTIQUITAS MUSEUM");
        brand.setStyle("-fx-text-fill:" + Theme.GOLD + ";-fx-font-size:11px;-fx-letter-spacing:2px;");
        logoRow.getChildren().addAll(lIcon, brand);

        Label title = UI.label("Discover the Ancient World", Theme.TEXT_PRIMARY, 28, true);
        title.setWrapText(true);

        Label sub = UI.label("Explore thousands of artifacts, civilizations, and historical records\nspanning five millennia of human history.", Theme.TEXT_MUTED, 12);
        sub.setAlignment(Pos.CENTER);

        HBox btns = new HBox(10);
        btns.setAlignment(Pos.CENTER);
        Button b1 = UI.goldBtn("Browse Collection");
        b1.setOnAction(e -> Navigator.go(new BrowsePage("All")));
        Button b2 = UI.ghostBtn("Ask the AI Guide");
        b2.setOnAction(e -> Navigator.go(new ChatBotPage()));
        btns.getChildren().addAll(b1, b2);

        overlay.getChildren().addAll(logoRow, title, sub, btns);
        hero.getChildren().addAll(bg, overlay);
        return hero;
    }

    private GridPane buildStats() {
        GridPane g = new GridPane();
        g.setHgap(12);
        for (int i = 0; i < 4; i++) { ColumnConstraints c = new ColumnConstraints(); c.setPercentWidth(25); g.getColumnConstraints().add(c); }

        Object[][] stats = {
            {"Artifacts",       S.getArtifacts().size(),     Theme.GOLD,   "A"},
            {"Collections",     S.getCollections().size(),   Theme.BLUE,   "C"},
            {"Civilizations",   S.getCivilizations().size(), Theme.GREEN,  "Z"},
            {"Periods",         S.getPeriods().size(),       Theme.RED,    "P"},
        };
        for (int i = 0; i < 4; i++) {
            String lbl = (String) stats[i][0]; int val = (int) stats[i][1]; String col = (String) stats[i][2]; String ico = (String) stats[i][3];
            HBox card = new HBox(10);
            card.setPadding(new Insets(14)); card.setAlignment(Pos.CENTER_LEFT);
            card.setStyle("-fx-background-color:" + Theme.BG_PANEL + ";-fx-border-color:" + Theme.BORDER + ";-fx-border-radius:6;-fx-background-radius:6;");
            Label icon = new Label(ico);
            icon.setStyle("-fx-background-color:" + col + "22;-fx-text-fill:" + col + ";-fx-min-width:36px;-fx-min-height:36px;-fx-max-width:36px;-fx-max-height:36px;-fx-alignment:center;-fx-background-radius:6;-fx-font-size:14px;-fx-font-weight:bold;");
            VBox info = new VBox(2, UI.label(String.valueOf(val), Theme.TEXT_PRIMARY, 20, true), UI.label(lbl, Theme.TEXT_DIMMER, 11));
            card.getChildren().addAll(icon, info);
            g.add(card, i, 0);
        }
        return g;
    }

    private VBox buildFeatured() {
        VBox sec = new VBox(12);
        HBox hdr = new HBox();
        hdr.setAlignment(Pos.CENTER_LEFT);
        Button viewAll = new Button("View All →");
        viewAll.setStyle("-fx-background-color:transparent;-fx-text-fill:" + Theme.GOLD + ";-fx-font-size:11px;-fx-padding:0;-fx-cursor:hand;");
        viewAll.setOnAction(e -> Navigator.go(new BrowsePage("Artifact")));
        hdr.getChildren().addAll(UI.label("Featured Artifacts", Theme.TEXT_PRIMARY, 15, true), UI.hSpacer(), viewAll);

        GridPane g = new GridPane(); g.setHgap(12);
        for (int i = 0; i < 3; i++) { ColumnConstraints c = new ColumnConstraints(); c.setPercentWidth(33.33); g.getColumnConstraints().add(c); }
        List<Artifact> feats = S.getArtifacts().subList(0, Math.min(3, S.getArtifacts().size()));
        for (int i = 0; i < feats.size(); i++) g.add(artifactCard(feats.get(i)), i, 0);

        sec.getChildren().addAll(hdr, g);
        return sec;
    }

    private VBox artifactCard(Artifact a) {
        VBox card = new VBox();
        card.setCursor(Cursor.HAND);
        card.setStyle("-fx-background-color:" + Theme.BG_PANEL + ";-fx-border-color:" + Theme.BORDER + ";-fx-border-radius:6;-fx-background-radius:6;");

        StackPane imgPane = new StackPane();
        imgPane.setMinHeight(130); imgPane.setMaxHeight(130);
        imgPane.setStyle("-fx-background-color:#0d0f1a;");
        ImageView iv = new ImageView();
        iv.setFitWidth(340); iv.setFitHeight(130); iv.setPreserveRatio(false);
        Rectangle clip = new Rectangle(340, 130); clip.setArcWidth(12); clip.setArcHeight(12); iv.setClip(clip);
        try { if (a.photo != null) iv.setImage(new Image(a.photo, true)); } catch (Exception ignored) {}
        imgPane.getChildren().add(iv);

        VBox info = new VBox(5); info.setPadding(new Insets(10));
        HBox badges = new HBox(5, UI.badge(a.mainCategory, Theme.GOLD));
        if (a.onDisplay) badges.getChildren().add(UI.badge("On Display", Theme.GREEN));
        Label nm = UI.label(a.name, "#dde3f0", 12, true); nm.setWrapText(true);
        info.getChildren().addAll(badges, nm, UI.label(a.origin.country + " · " + a.origin.century, Theme.TEXT_DIMMER, 10));

        card.getChildren().addAll(imgPane, info);
        card.setOnMouseClicked(e -> Navigator.go(new ItemDisplayPage(a.id)));
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color:" + Theme.BG_PANEL + ";-fx-border-color:" + Theme.GOLD + "44;-fx-border-radius:6;-fx-background-radius:6;"));
        card.setOnMouseExited(e  -> card.setStyle("-fx-background-color:" + Theme.BG_PANEL + ";-fx-border-color:" + Theme.BORDER + ";-fx-border-radius:6;-fx-background-radius:6;"));
        return card;
    }

    private VBox buildAbout() {
        VBox sec = new VBox(12);
        HBox content = new HBox(24);
        content.setPadding(new Insets(16));
        content.setStyle("-fx-background-color:" + Theme.BG_PANEL + ";-fx-border-color:" + Theme.BORDER + ";-fx-border-radius:6;-fx-background-radius:6;");

        VBox left = new VBox(8); HBox.setHgrow(left, Priority.ALWAYS);
        Label p1 = UI.label("The Antiquitas Museum is dedicated to the preservation, study, and public display of archaeological and historical artifacts from ancient civilizations spanning from the Mesopotamian Bronze Age to the Byzantine Empire.", Theme.TEXT_MUTED, 12);
        p1.setWrapText(true); p1.setLineSpacing(2);
        Label p2 = UI.label("Founded in 1923, we hold over 12,000 registered objects, with more than 2,500 currently on public display.", Theme.TEXT_MUTED, 12);
        p2.setWrapText(true); p2.setLineSpacing(2);
        left.getChildren().addAll(p1, p2);

        VBox right = new VBox(8); right.setMinWidth(260);
        String[][] rows = {{"Opening Hours","Tue–Sun: 9:00 AM – 5:00 PM"},{"Location","1 Museum Drive, Athens, GA 30602"},{"Admission","Adults $18 · Students $9 · Under 12 Free"},{"Contact","info@antiquitas.org · +1 555-0100"}};
        for (String[] r : rows) {
            HBox row = new HBox(6);
            Label k = UI.label(r[0] + ":", Theme.GOLD, 11); k.setMinWidth(110);
            Label v = UI.label(r[1], Theme.TEXT_MUTED, 11); v.setWrapText(true);
            row.getChildren().addAll(k, v);
            right.getChildren().add(row);
        }
        content.getChildren().addAll(left, right);
        sec.getChildren().addAll(UI.label("About the Museum", Theme.TEXT_PRIMARY, 15, true), content);
        return sec;
    }

    private VBox buildCategoryNav() {
        VBox sec = new VBox(12);
        GridPane g = new GridPane(); g.setHgap(10); g.setVgap(10);
        for (int i = 0; i < 3; i++) { ColumnConstraints c = new ColumnConstraints(); c.setPercentWidth(33.33); g.getColumnConstraints().add(c); }

        Object[][] cats = {
            {"Artifacts",          S.getArtifacts().size()     + " items",          "A","Artifact"},
            {"Civilizations",      S.getCivilizations().size() + " civilizations",  "C","Civilization"},
            {"Historical Periods", S.getPeriods().size()       + " periods",        "P","Period"},
            {"Notable Persons",    "Historical figures",                            "H","Person"},
            {"Organizations",      "Ancient institutions",                          "O","Organization"},
            {"Collections",        S.getCollections().size()   + " curated sets",   "E","Collection"},
        };
        for (int i = 0; i < cats.length; i++) {
            String lbl=(String)cats[i][0], desc=(String)cats[i][1], ico=(String)cats[i][2], type=(String)cats[i][3];
            HBox card = new HBox(10); card.setPadding(new Insets(12)); card.setAlignment(Pos.CENTER_LEFT); card.setCursor(Cursor.HAND);
            card.setStyle("-fx-background-color:" + Theme.BG_PANEL + ";-fx-border-color:" + Theme.BORDER + ";-fx-border-radius:6;-fx-background-radius:6;");
            Label icLbl = new Label(ico);
            icLbl.setStyle("-fx-background-color:" + Theme.GOLD + "22;-fx-text-fill:" + Theme.GOLD + ";-fx-min-width:36px;-fx-min-height:36px;-fx-max-width:36px;-fx-max-height:36px;-fx-alignment:center;-fx-background-radius:6;-fx-font-size:14px;-fx-font-weight:bold;");
            VBox inf = new VBox(2, UI.label(lbl,"#dde3f0",12,true), UI.label(desc,Theme.TEXT_DIMMER,10)); HBox.setHgrow(inf,Priority.ALWAYS);
            Label arrow = UI.label("›", Theme.TEXT_DIMMER, 16);
            card.getChildren().addAll(icLbl, inf, arrow);
            card.setOnMouseClicked(e -> Navigator.go(new BrowsePage(type)));
            card.setOnMouseEntered(e -> { card.setStyle("-fx-background-color:#242840;-fx-border-color:"+Theme.GOLD+"44;-fx-border-radius:6;-fx-background-radius:6;"); arrow.setStyle("-fx-text-fill:"+Theme.GOLD+";-fx-font-size:16px;"); });
            card.setOnMouseExited(e  -> { card.setStyle("-fx-background-color:"+Theme.BG_PANEL+";-fx-border-color:"+Theme.BORDER+";-fx-border-radius:6;-fx-background-radius:6;"); arrow.setStyle("-fx-text-fill:"+Theme.TEXT_DIMMER+";-fx-font-size:16px;"); });
            g.add(card, i%3, i/3);
        }
        sec.getChildren().addAll(UI.label("Browse by Category", Theme.TEXT_PRIMARY, 15, true), g);
        return sec;
    }
}
