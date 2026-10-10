package com.museum.controller;

import com.museum.context.AppState;
import com.museum.model.*;
import com.museum.util.Navigator;
import com.museum.util.Theme;
import com.museum.util.UI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.util.*;

public class BrowsePage extends VBox {
    private final AppState S = AppState.get();
    private String cat;
    private String query = "";
    private String filterCivId="", filterPeriodId="", filterColId="";
    private boolean filterDisplay=false, filterMuseum=false;
    private Label countLbl;
    private VBox resultBox;

    public BrowsePage(String initialCat) {
        this.cat = initialCat;
        setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
        VBox.setVgrow(this, Priority.ALWAYS);
        build();
    }

    private void build() {
        getChildren().clear();
        getChildren().add(buildHeader());
        getChildren().add(buildTabs());
        HBox body = new HBox(12);
        body.setPadding(new Insets(12,16,16,16));
        VBox.setVgrow(body, Priority.ALWAYS);
        body.getChildren().addAll(buildFilters(), buildResults());
        getChildren().add(body);
        refresh();
    }

    private HBox buildHeader() {
        HBox h = new HBox(10); h.setPadding(new Insets(10,16,10,16)); h.setAlignment(Pos.CENTER_LEFT);
        h.setStyle("-fx-background-color:"+Theme.BG_HEADER+";-fx-border-color:transparent transparent "+Theme.BORDER+" transparent;");
        VBox info = new VBox(1, UI.label("Browse Collection",Theme.TEXT_PRIMARY,13,true), UI.label("Search and filter museum items",Theme.TEXT_DIMMER,10));
        TextField sf = UI.textField("Search items…"); sf.setMinWidth(230);
        sf.textProperty().addListener((o,a,v)->{ query=v; refresh(); });
        h.getChildren().addAll(info, UI.hSpacer(), sf);
        return h;
    }

    private HBox buildTabs() {
        HBox tabs = new HBox(4); tabs.setPadding(new Insets(8,16,8,16)); tabs.setAlignment(Pos.CENTER_LEFT);
        tabs.setStyle("-fx-background-color:"+Theme.BG_DARKER+";-fx-border-color:transparent transparent "+Theme.BORDER+" transparent;");
        for (String c : new String[]{"All","Artifact","Period","Civilization","Person","Organization","Collection"}) {
            boolean act = c.equals(cat);
            Button b = new Button(c);
            if (act) b.setStyle("-fx-background-color:"+Theme.GOLD+";-fx-text-fill:#1a1d2e;-fx-font-size:11px;-fx-padding:4 10;-fx-background-radius:4;-fx-cursor:hand;-fx-font-weight:600;");
            else {
                b.setStyle("-fx-background-color:transparent;-fx-text-fill:"+Theme.TEXT_SECONDARY+";-fx-font-size:11px;-fx-padding:4 10;-fx-background-radius:4;-fx-cursor:hand;");
                b.setOnMouseEntered(e->b.setStyle("-fx-background-color:"+Theme.BG_INPUT+";-fx-text-fill:"+Theme.TEXT_PRIMARY+";-fx-font-size:11px;-fx-padding:4 10;-fx-background-radius:4;-fx-cursor:hand;"));
                b.setOnMouseExited(e ->b.setStyle("-fx-background-color:transparent;-fx-text-fill:"+Theme.TEXT_SECONDARY+";-fx-font-size:11px;-fx-padding:4 10;-fx-background-radius:4;-fx-cursor:hand;"));
            }
            b.setOnAction(e->{ cat=c; build(); });
            tabs.getChildren().add(b);
        }
        return tabs;
    }

    private VBox buildFilters() {
        VBox p = new VBox(14); p.setMinWidth(180); p.setMaxWidth(180); p.setPadding(new Insets(12));
        p.setStyle("-fx-background-color:"+Theme.BG_PANEL+";-fx-border-color:"+Theme.BORDER+";-fx-border-radius:6;-fx-background-radius:6;");
        p.getChildren().add(UI.label("FILTERS",Theme.TEXT_DIMMER,10));

        if (cat.equals("Artifact")||cat.equals("All")) {
            ComboBox<String> civCB = UI.combo(); civCB.getItems().add("Any Civilization"); S.getCivilizations().forEach(c->civCB.getItems().add(c.name+"|"+c.id)); civCB.setValue("Any Civilization"); civCB.setMaxWidth(Double.MAX_VALUE);
            civCB.setOnAction(e->{ String v=civCB.getValue(); filterCivId=(v!=null&&v.contains("|"))?v.split("\\|")[1]:""; refresh(); });
            p.getChildren().add(UI.formField("Civilization",civCB));

            ComboBox<String> perCB = UI.combo(); perCB.getItems().add("Any Period"); S.getPeriods().forEach(pp->perCB.getItems().add(pp.name+"|"+pp.id)); perCB.setValue("Any Period"); perCB.setMaxWidth(Double.MAX_VALUE);
            perCB.setOnAction(e->{ String v=perCB.getValue(); filterPeriodId=(v!=null&&v.contains("|"))?v.split("\\|")[1]:""; refresh(); });
            p.getChildren().add(UI.formField("Period",perCB));

            ComboBox<String> colCB = UI.combo(); colCB.getItems().add("Any Collection"); S.getCollections().forEach(c->colCB.getItems().add(c.name+"|"+c.id)); colCB.setValue("Any Collection"); colCB.setMaxWidth(Double.MAX_VALUE);
            colCB.setOnAction(e->{ String v=colCB.getValue(); filterColId=(v!=null&&v.contains("|"))?v.split("\\|")[1]:""; refresh(); });
            p.getChildren().add(UI.formField("Collection",colCB));

            CheckBox disp = UI.checkBox("On Display Only"); disp.setOnAction(e->{ filterDisplay=disp.isSelected(); refresh(); });
            CheckBox mus  = UI.checkBox("In Museum Only");  mus.setOnAction(e->{ filterMuseum=mus.isSelected();  refresh(); });
            p.getChildren().addAll(disp, mus);
        }
        p.getChildren().add(UI.vSpacer());
        Button clr = UI.ghostBtn("Clear Filters"); clr.setMaxWidth(Double.MAX_VALUE);
        clr.setOnAction(e->{ filterCivId=""; filterPeriodId=""; filterColId=""; filterDisplay=false; filterMuseum=false; build(); });
        p.getChildren().add(clr);
        return p;
    }

    private VBox buildResults() {
        VBox area = new VBox(10); VBox.setVgrow(area,Priority.ALWAYS); HBox.setHgrow(area,Priority.ALWAYS);
        countLbl = UI.label("", Theme.TEXT_DIMMER, 11);
        resultBox = new VBox(8);
        ScrollPane sp = UI.scrollPane(resultBox); VBox.setVgrow(sp,Priority.ALWAYS);
        area.getChildren().addAll(countLbl, sp);
        return area;
    }

    private void refresh() {
        List<Object[]> items = getItems();
        countLbl.setText(items.size() + " result" + (items.size()!=1?"s":""));
        resultBox.getChildren().clear();
        if (items.isEmpty()) {
            VBox empty = new VBox(6); empty.setAlignment(Pos.CENTER); empty.setPadding(new Insets(40));
            empty.getChildren().addAll(UI.label("No items found",Theme.TEXT_MUTED,14,true), UI.label("Try adjusting search or filters.",Theme.TEXT_DIMMER,11));
            resultBox.getChildren().add(empty); return;
        }
        for (Object[] it : items) resultBox.getChildren().add(buildRow(it));
    }

    private List<Object[]> getItems() {
        List<Object[]> all = new ArrayList<>();
        String q = query.toLowerCase();
        boolean art=(cat.equals("All")||cat.equals("Artifact")), per=(cat.equals("All")||cat.equals("Period")),
                civ=(cat.equals("All")||cat.equals("Civilization")), pers=(cat.equals("All")||cat.equals("Person")),
                org=(cat.equals("All")||cat.equals("Organization")), col=(cat.equals("All")||cat.equals("Collection"));

        if (art) for (Artifact a:S.getArtifacts()) {
            if (!q.isEmpty()&&!a.name.toLowerCase().contains(q)&&!a.mainCategory.toLowerCase().contains(q)&&!a.origin.country.toLowerCase().contains(q)) continue;
            if (!filterCivId.isEmpty()&&!filterCivId.equals(a.origin.civilizationId)) continue;
            if (!filterPeriodId.isEmpty()&&!filterPeriodId.equals(a.origin.periodId)) continue;
            if (!filterColId.isEmpty()&&!filterColId.equals(a.collectionId)) continue;
            if (filterDisplay&&!a.onDisplay) continue;
            if (filterMuseum&&!a.inMuseum) continue;
            all.add(new Object[]{a.id,a.name,"Artifact",a.mainCategory,a.origin.country,a.onDisplay,a.photo});
        }
        if (per) for (Period p:S.getPeriods()) {
            if (!q.isEmpty()&&!p.name.toLowerCase().contains(q)) continue;
            all.add(new Object[]{p.id,p.name,"Period",p.getYearRange(),"",false,""});
        }
        if (civ) for (Civilization c:S.getCivilizations()) {
            if (!q.isEmpty()&&!c.name.toLowerCase().contains(q)&&!c.description.toLowerCase().contains(q)) continue;
            all.add(new Object[]{c.id,c.name,"Civilization",c.description.length()>70?c.description.substring(0,70)+"…":c.description,"",false,""});
        }
        if (pers) for (Person p:S.getPersons()) {
            if (!q.isEmpty()&&!p.name.toLowerCase().contains(q)) continue;
            all.add(new Object[]{p.id,p.name,"Person",p.description.length()>70?p.description.substring(0,70)+"…":p.description,"",false,""});
        }
        if (org) for (Organization o:S.getOrganizations()) {
            if (!q.isEmpty()&&!o.name.toLowerCase().contains(q)) continue;
            all.add(new Object[]{o.id,o.name,"Organization",o.description.length()>70?o.description.substring(0,70)+"…":o.description,"",false,""});
        }
        if (col) for (MuseumCollection c:S.getCollections()) {
            if (!q.isEmpty()&&!c.name.toLowerCase().contains(q)) continue;
            all.add(new Object[]{c.id,c.name,"Collection",c.description.length()>70?c.description.substring(0,70)+"…":c.description,"",false,""});
        }
        return all;
    }

    private HBox buildRow(Object[] it) {
        String id=(String)it[0],name=(String)it[1],type=(String)it[2],sub=(String)it[3],country=(String)it[4];
        boolean disp=(boolean)it[5]; String photo=(String)it[6];
        String tc = Theme.typeColor(type);

        HBox row = new HBox(12); row.setPadding(new Insets(10,12,10,12)); row.setAlignment(Pos.CENTER_LEFT); row.setCursor(Cursor.HAND);
        row.setStyle("-fx-background-color:"+Theme.BG_PANEL+";-fx-border-color:"+Theme.BORDER+";-fx-border-radius:6;-fx-background-radius:6;");

        // Thumbnail
        if (type.equals("Artifact")&&photo!=null&&!photo.isEmpty()) {
            StackPane th = new StackPane(); th.setMinSize(48,48); th.setMaxSize(48,48);
            th.setStyle("-fx-background-color:#0d0f1a;-fx-background-radius:4;");
            ImageView iv = new ImageView(); iv.setFitWidth(48); iv.setFitHeight(48); iv.setPreserveRatio(false);
            try { iv.setImage(new Image(photo,true)); } catch (Exception ignored) {}
            th.getChildren().add(iv); row.getChildren().add(th);
        } else {
            Label ico = new Label(type.substring(0,1));
            ico.setStyle("-fx-background-color:"+tc+"22;-fx-text-fill:"+tc+";-fx-min-width:46px;-fx-min-height:46px;-fx-max-width:46px;-fx-max-height:46px;-fx-alignment:center;-fx-background-radius:4;-fx-font-size:18px;-fx-font-weight:bold;");
            row.getChildren().add(ico);
        }

        VBox info = new VBox(4); HBox.setHgrow(info,Priority.ALWAYS);
        HBox badges = new HBox(5); badges.setAlignment(Pos.CENTER_LEFT);
        badges.getChildren().add(UI.badge(type,tc));
        if (disp) badges.getChildren().add(UI.badge("On Display",Theme.GREEN));
        if (!country.isEmpty()) badges.getChildren().add(UI.badge("📍 "+country,Theme.TEXT_DIMMER));
        Label nm=UI.label(name,"#dde3f0",12,true);
        Label sb=UI.label(sub,Theme.TEXT_DIMMER,10); sb.setWrapText(true);
        info.getChildren().addAll(badges,nm,sb);

        Label arrow = UI.label("›",Theme.TEXT_DIMMER,16);
        row.getChildren().addAll(info, arrow);

        if (type.equals("Artifact")) row.setOnMouseClicked(e->Navigator.go(new ItemDisplayPage(id)));
        row.setOnMouseEntered(e->{ row.setStyle("-fx-background-color:#242840;-fx-border-color:"+tc+"44;-fx-border-radius:6;-fx-background-radius:6;"); arrow.setStyle("-fx-text-fill:"+Theme.GOLD+";-fx-font-size:16px;"); });
        row.setOnMouseExited(e ->{ row.setStyle("-fx-background-color:"+Theme.BG_PANEL+";-fx-border-color:"+Theme.BORDER+";-fx-border-radius:6;-fx-background-radius:6;"); arrow.setStyle("-fx-text-fill:"+Theme.TEXT_DIMMER+";-fx-font-size:16px;"); });
        return row;
    }
}
