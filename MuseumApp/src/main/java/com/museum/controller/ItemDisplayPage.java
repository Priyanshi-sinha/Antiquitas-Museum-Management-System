package com.museum.controller;

import com.museum.context.AppState;
import com.museum.model.*;
import com.museum.util.Navigator;
import com.museum.util.Theme;
import com.museum.util.UI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class ItemDisplayPage extends VBox {
    private final AppState S = AppState.get();

    public ItemDisplayPage(String id) {
        setStyle("-fx-background-color:"+Theme.BG_DARK+";");
        VBox.setVgrow(this,Priority.ALWAYS);
        Artifact a = S.findArtifact(id);
        if (a==null) { setAlignment(Pos.CENTER); getChildren().add(UI.label("Artifact not found.",Theme.RED,14)); return; }
        ScrollPane sp = UI.scrollPane(buildContent(a));
        VBox.setVgrow(sp,Priority.ALWAYS);
        getChildren().add(sp);
    }

    private VBox buildContent(Artifact a) {
        VBox root = new VBox(0); root.setStyle("-fx-background-color:"+Theme.BG_DARK+";");

        // Header bar
        HBox hdr = new HBox(10); hdr.setPadding(new Insets(10,16,10,16)); hdr.setAlignment(Pos.CENTER_LEFT);
        hdr.setStyle("-fx-background-color:"+Theme.BG_HEADER+";-fx-border-color:transparent transparent "+Theme.BORDER+" transparent;");
        Button back = UI.ghostBtn("← Browse"); back.setOnAction(e->Navigator.go(new BrowsePage("Artifact")));
        hdr.getChildren().addAll(back, UI.hSpacer());
        if (S.isCurator()) {
            Button edit = UI.goldBtn("Edit Artifact"); edit.setOnAction(e->Navigator.go(new ItemAddEditPage(a.id, "Artifact")));
            Button del  = UI.dangerBtn("Delete");
            del.setOnAction(e->{
                Alert c=new Alert(Alert.AlertType.CONFIRMATION,"Delete \""+a.name+"\"?",ButtonType.YES,ButtonType.NO); c.setHeaderText("Confirm Delete");
                c.showAndWait().ifPresent(b->{ if(b==ButtonType.YES){ S.deleteArtifact(a.id); Navigator.go(new BrowsePage("Artifact")); } });
            });
            hdr.getChildren().addAll(edit,del);
        }
        root.getChildren().add(hdr);

        VBox main = new VBox(18); main.setPadding(new Insets(20,32,32,32)); main.setMaxWidth(1000);

        // Image + info row
        HBox top = new HBox(20); top.setAlignment(Pos.TOP_LEFT);

        StackPane imgPane = new StackPane(); imgPane.setMinSize(260,240); imgPane.setMaxSize(260,240);
        imgPane.setStyle("-fx-background-color:#0d0f1a;-fx-background-radius:8;-fx-border-color:"+Theme.BORDER+";-fx-border-radius:8;");
        ImageView iv = new ImageView(); iv.setFitWidth(260); iv.setFitHeight(240); iv.setPreserveRatio(false);
        Rectangle clip=new Rectangle(260,240); clip.setArcWidth(14); clip.setArcHeight(14); iv.setClip(clip);
        try { if(a.photo!=null&&!a.photo.isEmpty()) iv.setImage(new Image(a.photo,true)); } catch(Exception ignored){}
        imgPane.getChildren().add(iv);

        VBox info = new VBox(10); HBox.setHgrow(info,Priority.ALWAYS);
        HBox badges = new HBox(6, UI.badge(a.mainCategory,Theme.GOLD), UI.badge(a.subCategory,Theme.BLUE));
        if (a.onDisplay)  badges.getChildren().add(UI.badge("On Display",Theme.GREEN));
        if (!a.inMuseum)  badges.getChildren().add(UI.badge("Off-Site",Theme.RED));
        Label nm = UI.label(a.name,Theme.TEXT_PRIMARY,22,true); nm.setWrapText(true);
        Label desc=UI.label(a.description,Theme.TEXT_MUTED,12); desc.setWrapText(true); desc.setLineSpacing(3);

        GridPane og = new GridPane(); og.setHgap(12); og.setVgap(5);
        String[][] orows={{"Country",a.origin.country},{"Place",a.origin.place},{"Date",a.origin.dateOrigin},{"Age",a.origin.age},{"Century",a.origin.century}};
        for(int i=0;i<orows.length;i++){
            og.add(UI.label(orows[i][0],Theme.TEXT_DIMMER,10),0,i);
            og.add(UI.label(orows[i][1]!=null?orows[i][1]:"—",Theme.TEXT_SECONDARY,11),1,i);
        }
        HBox links=new HBox(6); links.setAlignment(Pos.CENTER_LEFT);
        Civilization civ=a.origin.civilizationId!=null?S.findCiv(a.origin.civilizationId):null;
        Period per=a.origin.periodId!=null?S.findPeriod(a.origin.periodId):null;
        MuseumCollection col=a.collectionId!=null?S.findCollection(a.collectionId):null;
        if(civ!=null)links.getChildren().add(UI.badge("Civ: "+civ.name,Theme.GREEN));
        if(per!=null)links.getChildren().add(UI.badge("Period: "+per.name,Theme.BLUE));
        if(col!=null)links.getChildren().add(UI.badge("Collection: "+col.name,Theme.ORANGE));
        info.getChildren().addAll(badges,nm,desc,og,links);
        top.getChildren().addAll(imgPane,info);
        main.getChildren().addAll(top,UI.separator());

        // Attributes
        if(!a.mainCategoryAttributes.isEmpty()||!a.subCategoryAttributes.isEmpty()){
            HBox attrs=new HBox(16);
            if(!a.mainCategoryAttributes.isEmpty()) attrs.getChildren().add(buildAttrSection(a.mainCategory+" Attributes",a.mainCategoryAttributes));
            if(!a.subCategoryAttributes.isEmpty())  attrs.getChildren().add(buildAttrSection(a.subCategory+" Attributes",a.subCategoryAttributes));
            main.getChildren().addAll(attrs,UI.separator());
        }

        // Connections
        VBox conn=new VBox(14);
        if(!a.connectedPersons.isEmpty()) conn.getChildren().add(buildConnSection("Connected Persons",
            a.connectedPersons.stream().map(cp->{Person p=S.findPerson(cp.personId);return new String[]{p!=null?p.name:"Unknown",cp.relationType,cp.description};}).toList(),Theme.ORANGE));
        if(!a.connectedOrganizations.isEmpty()) conn.getChildren().add(buildConnSection("Connected Organizations",
            a.connectedOrganizations.stream().map(co->{Organization o=S.findOrg(co.orgId);return new String[]{o!=null?o.name:"Unknown",co.relationType,co.description};}).toList(),Theme.PURPLE));
        if(!a.connectedArtifacts.isEmpty()) conn.getChildren().add(buildConnSection("Related Artifacts",
            a.connectedArtifacts.stream().map(ca->{Artifact r=S.findArtifact(ca.artifactId);return new String[]{r!=null?r.name:"Unknown",ca.relationType,ca.description};}).toList(),Theme.GOLD));
        if(!conn.getChildren().isEmpty()) main.getChildren().addAll(conn,UI.separator());

        // Meta
        HBox meta=new HBox(20);
        meta.getChildren().addAll(metaPair("Record ID",a.id),metaPair("Created",UI.fmt(a.createdAt)),metaPair("Last Updated",UI.fmt(a.updatedAt)));
        main.getChildren().add(meta);

        HBox centered=new HBox(main); centered.setAlignment(Pos.TOP_CENTER); centered.setStyle("-fx-background-color:"+Theme.BG_DARK+";");
        root.getChildren().add(centered);
        return root;
    }

    private VBox buildAttrSection(String title, List<Artifact.AttributeDoc> attrs) {
        VBox box=new VBox(8); HBox.setHgrow(box,Priority.ALWAYS);
        box.setPadding(new Insets(12)); box.setStyle("-fx-background-color:"+Theme.BG_PANEL+";-fx-border-color:"+Theme.BORDER+";-fx-border-radius:6;-fx-background-radius:6;");
        box.getChildren().add(UI.label(title,Theme.TEXT_PRIMARY,12,true));
        for(var at:attrs){
            HBox row=new HBox(8);
            Label k=UI.label(at.label+":",Theme.TEXT_DIMMER,11); k.setMinWidth(100);
            row.getChildren().addAll(k,UI.label(at.value,Theme.TEXT_SECONDARY,11));
            box.getChildren().add(row);
        }
        return box;
    }

    private VBox buildConnSection(String title, List<String[]> items, String color) {
        VBox box=new VBox(8); box.getChildren().add(UI.label(title,Theme.TEXT_PRIMARY,13,true));
        for(String[] it:items){
            HBox row=new HBox(10); row.setPadding(new Insets(8,10,8,10)); row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-background-color:"+Theme.BG_PANEL+";-fx-border-color:"+color+"33;-fx-border-radius:5;-fx-background-radius:5;");
            VBox inf=new VBox(3);
            HBox tr=new HBox(8,UI.label(it[0],Theme.TEXT_PRIMARY,12,true),UI.badge(it[1],color)); tr.setAlignment(Pos.CENTER_LEFT);
            Label d=UI.label(it[2],Theme.TEXT_DIMMER,10); d.setWrapText(true);
            inf.getChildren().addAll(tr,d);
            row.getChildren().add(inf);
            box.getChildren().add(row);
        }
        return box;
    }

    private HBox metaPair(String k, String v) {
        return new HBox(new VBox(2,UI.label(k,Theme.TEXT_DIMMER,9),UI.label(v,Theme.TEXT_SECONDARY,11)));
    }
}
