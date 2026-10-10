package com.museum.controller;

import com.museum.context.AppState;
import com.museum.model.Log;
import com.museum.util.Theme;
import com.museum.util.UI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LogPage extends VBox {

    private final AppState S = AppState.get();
    private String query = "", filterType = "All", filterChange = "All";

    // Built once, never recreated
    private HBox filterBar;
    private VBox listBox;
    private Label countLbl;

    public LogPage() {
        setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
        VBox.setVgrow(this, Priority.ALWAYS);
        if (!S.isCurator()) {
            setAlignment(Pos.CENTER);
            getChildren().addAll(
                UI.label("Access Denied", Theme.RED, 18, true),
                UI.label("Curators and Admins only.", Theme.TEXT_MUTED, 12));
            return;
        }
        buildChrome();
        refresh();
    }

    /** Build header, filter bar, scroll pane ONCE. Never call again. */
    private void buildChrome() {
        // Header
        HBox hdr = new HBox(10);
        hdr.setPadding(new Insets(10, 16, 10, 16));
        hdr.setAlignment(Pos.CENTER_LEFT);
        hdr.setStyle("-fx-background-color:" + Theme.BG_HEADER
            + ";-fx-border-color:transparent transparent " + Theme.BORDER + " transparent;");

        countLbl = UI.label("", Theme.TEXT_DIMMER, 10);
        VBox info = new VBox(1,
            UI.label("Activity Logs", Theme.TEXT_PRIMARY, 13, true),
            countLbl);

        TextField sf = UI.textField("Search logs\u2026");
        sf.setMinWidth(200);
        sf.textProperty().addListener((o, a, v) -> { query = v; refresh(); });
        hdr.getChildren().addAll(info, UI.hSpacer(), sf);

        // Filter bar container — its children are replaced by rebuildFilterBar()
        filterBar = new HBox(6);
        filterBar.setPadding(new Insets(8, 16, 8, 16));
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setStyle("-fx-background-color:" + Theme.BG_DARKER
            + ";-fx-border-color:transparent transparent " + Theme.BORDER + " transparent;");
        rebuildFilterBar();

        // Results list inside a scroll pane
        listBox = new VBox(6);
        listBox.setPadding(new Insets(12, 16, 16, 16));
        ScrollPane sp = UI.scrollPane(listBox);
        VBox.setVgrow(sp, Priority.ALWAYS);

        getChildren().addAll(hdr, filterBar, sp);
    }

    /** Replace only the buttons inside the existing filterBar node. */
    private void rebuildFilterBar() {
        filterBar.getChildren().clear();

        filterBar.getChildren().add(UI.label("Type:", Theme.TEXT_DIMMER, 11));
        for (String t : new String[]{"All", "Artifact", "User", "Collection", "Civilization", "Period", "Organization"}) {
            final String v = t;
            Button b = fBtn(v, v.equals(filterType));
            b.setOnAction(e -> { filterType = v; rebuildFilterBar(); refresh(); });
            filterBar.getChildren().add(b);
        }

        Region div = new Region();
        div.setMinSize(1, 18); div.setMaxSize(1, 18);
        div.setStyle("-fx-background-color:" + Theme.BORDER + ";");
        filterBar.getChildren().add(div);

        filterBar.getChildren().add(UI.label("Change:", Theme.TEXT_DIMMER, 11));
        for (String c : new String[]{"All", "Created", "Updated", "Deleted"}) {
            final String v = c;
            Button b = fBtn(v, v.equals(filterChange));
            b.setOnAction(e -> { filterChange = v; rebuildFilterBar(); refresh(); });
            filterBar.getChildren().add(b);
        }
    }

    /** Clear and repopulate only the listBox. */
    private void refresh() {
        listBox.getChildren().clear();
        String q = query.toLowerCase();

        var filtered = S.getLogs().stream()
            .filter(l -> filterType.equals("All")   || l.itemType.equals(filterType))
            .filter(l -> filterChange.equals("All") || l.changeType.equals(filterChange))
            .filter(l -> q.isEmpty()
                || l.itemName.toLowerCase().contains(q)
                || l.changedBy.toLowerCase().contains(q)
                || l.itemType.toLowerCase().contains(q))
            .toList();

        countLbl.setText(filtered.size() + " of " + S.getLogs().size() + " entries");

        if (filtered.isEmpty()) {
            VBox empty = new VBox(6);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(40));
            empty.getChildren().add(UI.label("No logs found", Theme.TEXT_MUTED, 14, true));
            listBox.getChildren().add(empty);
        } else {
            filtered.forEach(l -> listBox.getChildren().add(buildRow(l)));
        }
    }

    private Button fBtn(String text, boolean active) {
        Button b = new Button(text);
        if (active) {
            b.setStyle("-fx-background-color:" + Theme.GOLD
                + ";-fx-text-fill:#1a1d2e;-fx-font-size:10px;-fx-padding:3 8;"
                + "-fx-background-radius:3;-fx-cursor:hand;-fx-font-weight:600;");
        } else {
            String base = "-fx-background-color:transparent;-fx-text-fill:" + Theme.TEXT_SECONDARY
                + ";-fx-font-size:10px;-fx-padding:3 8;-fx-background-radius:3;-fx-cursor:hand;";
            b.setStyle(base);
            b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:" + Theme.BG_INPUT
                + ";-fx-text-fill:" + Theme.TEXT_PRIMARY
                + ";-fx-font-size:10px;-fx-padding:3 8;-fx-background-radius:3;-fx-cursor:hand;"));
            b.setOnMouseExited(e -> b.setStyle(base));
        }
        return b;
    }

    private VBox buildRow(Log log) {
        String cc = Theme.logColor(log.changeType);
        String tc = Theme.typeColor(log.itemType);

        VBox card = new VBox(0);
        card.setStyle("-fx-background-color:" + Theme.BG_PANEL
            + ";-fx-border-color:" + Theme.BORDER
            + ";-fx-border-radius:6;-fx-background-radius:6;");

        HBox row = new HBox(10);
        row.setPadding(new Insets(10, 12, 10, 12));
        row.setAlignment(Pos.CENTER_LEFT);

        Label ind = new Label(log.changeType.substring(0, 1));
        ind.setStyle("-fx-background-color:" + cc + "22;-fx-text-fill:" + cc
            + ";-fx-min-width:30px;-fx-min-height:30px;-fx-max-width:30px;-fx-max-height:30px;"
            + "-fx-alignment:center;-fx-background-radius:4;-fx-font-size:12px;"
            + "-fx-font-weight:bold;-fx-border-color:" + cc + "44;-fx-border-radius:4;");

        VBox info = new VBox(4);
        HBox.setHgrow(info, Priority.ALWAYS);

        HBox topRow = new HBox(6,
            UI.badge(log.changeType, cc),
            UI.badge(log.itemType, tc),
            UI.label(log.itemName, Theme.TEXT_PRIMARY, 12, true));
        topRow.setAlignment(Pos.CENTER_LEFT);

        HBox botRow = new HBox(10,
            UI.label("By: " + log.changedBy, Theme.TEXT_DIMMER, 10),
            UI.label("\u2022", Theme.TEXT_DIMMER, 10),
            UI.label(fmtDate(log.changedAt), Theme.TEXT_DIMMER, 10));
        botRow.setAlignment(Pos.CENTER_LEFT);

        info.getChildren().addAll(topRow, botRow);
        row.getChildren().addAll(ind, info);
        card.getChildren().add(row);

        if (!log.changes.isEmpty()) {
            VBox changesBox = new VBox(4);
            changesBox.setPadding(new Insets(0, 12, 10, 52));
            changesBox.setStyle("-fx-border-color:" + Theme.BORDER
                + " transparent transparent transparent;");
            for (Log.LogChange ch : log.changes) {
                HBox cr = new HBox(6,
                    UI.badge("field: " + ch.field, Theme.BLUE),
                    UI.label(ch.oldValue, Theme.RED, 10),
                    UI.label("\u2192", Theme.TEXT_DIMMER, 10),
                    UI.label(ch.newValue, Theme.GREEN, 10));
                cr.setAlignment(Pos.CENTER_LEFT);
                changesBox.getChildren().add(cr);
            }
            card.getChildren().add(changesBox);
        }
        return card;
    }

    private String fmtDate(String iso) {
        if (iso == null || iso.isEmpty()) return "\u2014";
        try {
            String d = iso.substring(0, 10);
            String t = iso.length() >= 16 ? iso.substring(11, 16) : "";
            return d + (t.isEmpty() ? "" : " " + t);
        } catch (Exception e) { return iso; }
    }
}
