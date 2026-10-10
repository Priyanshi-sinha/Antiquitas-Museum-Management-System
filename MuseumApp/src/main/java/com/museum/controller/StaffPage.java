package com.museum.controller;

import com.museum.context.AppState;
import com.museum.model.User;
import com.museum.util.Theme;
import com.museum.util.UI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class StaffPage extends VBox {
    private final AppState S = AppState.get();
    private String query = "";
    private VBox listBox;

    public StaffPage() {
        setStyle("-fx-background-color:"+Theme.BG_DARK+";");
        VBox.setVgrow(this,Priority.ALWAYS);
        if (!S.isCurator()) {
            setAlignment(Pos.CENTER);
            getChildren().addAll(UI.label("Access Denied",Theme.RED,18,true), UI.label("Curators and Admins only.",Theme.TEXT_MUTED,12));
            return;
        }
        build();
    }

    private void build() {
        HBox hdr=new HBox(10); hdr.setPadding(new Insets(10,16,10,16)); hdr.setAlignment(Pos.CENTER_LEFT);
        hdr.setStyle("-fx-background-color:"+Theme.BG_HEADER+";-fx-border-color:transparent transparent "+Theme.BORDER+" transparent;");
        long sc=S.getUsers().stream().filter(u->!u.isGuest()).count();
        VBox info=new VBox(1,UI.label("Staff Management",Theme.TEXT_PRIMARY,13,true),UI.label(sc+" staff members registered",Theme.TEXT_DIMMER,10));
        TextField sf=UI.textField("Search staff…"); sf.setMinWidth(200);
        sf.textProperty().addListener((o,a,v)->{query=v;refresh();});
        hdr.getChildren().addAll(info,UI.hSpacer(),sf);
        if(S.isAdmin()){Button r=UI.goldBtn("+ Register Staff");r.setOnAction(e->NavBar.getInstance().nav("register"));hdr.getChildren().add(r);}

        listBox=new VBox(8); listBox.setPadding(new Insets(16));
        ScrollPane sp=UI.scrollPane(listBox); VBox.setVgrow(sp,Priority.ALWAYS);
        getChildren().addAll(hdr,sp);
        refresh();
    }

    private void refresh() {
        listBox.getChildren().clear();
        String q=query.toLowerCase();
        S.getUsers().stream()
            .filter(u->!u.isGuest())
            .filter(u->q.isEmpty()||u.name.toLowerCase().contains(q)||u.username.toLowerCase().contains(q)||u.email.toLowerCase().contains(q)||u.role.toLowerCase().contains(q))
            .forEach(u->listBox.getChildren().add(buildCard(u)));
        if(listBox.getChildren().isEmpty()){
            VBox empty=new VBox(6);empty.setAlignment(Pos.CENTER);empty.setPadding(new Insets(40));
            empty.getChildren().addAll(UI.label("No staff found",Theme.TEXT_MUTED,14,true));listBox.getChildren().add(empty);}
    }

    private VBox buildCard(User u) {
        String rc=u.isAdmin()?Theme.PURPLE:Theme.GREEN;
        VBox card=new VBox(0);
        card.setStyle("-fx-background-color:"+Theme.BG_PANEL+";-fx-border-color:"+Theme.BORDER+";-fx-border-radius:6;-fx-background-radius:6;");

        HBox row=new HBox(12); row.setPadding(new Insets(12)); row.setAlignment(Pos.CENTER_LEFT); row.setCursor(Cursor.HAND);

        Label av=new Label(String.valueOf(u.name.charAt(0)));
        av.setStyle("-fx-background-color:"+rc+"33;-fx-text-fill:"+rc+";-fx-min-width:38px;-fx-min-height:38px;-fx-max-width:38px;-fx-max-height:38px;-fx-alignment:center;-fx-background-radius:20;-fx-font-size:16px;-fx-font-weight:bold;");

        VBox nb=new VBox(3); HBox.setHgrow(nb,Priority.ALWAYS);
        HBox nr=new HBox(8,UI.label(u.name,Theme.TEXT_PRIMARY,13,true),UI.badge(u.role,rc)); nr.setAlignment(Pos.CENTER_LEFT);
        nb.getChildren().addAll(nr,UI.label("@"+u.username,Theme.TEXT_DIMMER,10));

        VBox det=new VBox(2); det.setMinWidth(200);
        if(u.email!=null&&!u.email.isEmpty()) det.getChildren().add(UI.label("✉ "+u.email,Theme.TEXT_MUTED,10));
        if(u.phone!=null&&!u.phone.isEmpty()) det.getChildren().add(UI.label("☎ "+u.phone,Theme.TEXT_MUTED,10));

        Label exp=UI.label("▼",Theme.TEXT_DIMMER,10);
        HBox acts=new HBox(6); acts.setAlignment(Pos.CENTER_RIGHT);
        if(S.isAdmin()){
            Button del=UI.dangerBtn("Remove");
            del.setOnAction(e->{
                if(u.id.equals(S.getCurrentUser().id)){
                    Alert w=new Alert(Alert.AlertType.WARNING,"Cannot delete your own account.",ButtonType.OK); w.setHeaderText("Notice"); w.showAndWait(); return;
                }
                Alert c=new Alert(Alert.AlertType.CONFIRMATION,"Remove \""+u.name+"\"?",ButtonType.YES,ButtonType.NO); c.setHeaderText("Confirm");
                c.showAndWait().ifPresent(b->{ if(b==ButtonType.YES){ S.deleteUser(u.id); refresh(); } });
            });
            acts.getChildren().add(del);
        }
        acts.getChildren().add(exp);
        row.getChildren().addAll(av,nb,det,UI.hSpacer(),acts);

        // Expanded section
        VBox xp=new VBox(8); xp.setPadding(new Insets(0,12,12,12)); xp.setVisible(false); xp.setManaged(false);
        xp.setStyle("-fx-border-color:"+Theme.BORDER+" transparent transparent transparent;");
        GridPane dg=new GridPane(); dg.setHgap(16); dg.setVgap(5);
        String[][] fields={{"Address",u.address},{"Date of Birth",u.dob},{"Gender",u.gender},{"Added By",u.addedBy},{"Member Since",UI.fmt(u.createdAt)}};
        int ri=0;
        for(String[] f:fields){ if(f[1]!=null&&!f[1].isEmpty()){dg.add(UI.label(f[0]+":",Theme.TEXT_DIMMER,10),0,ri);dg.add(UI.label(f[1],Theme.TEXT_SECONDARY,11),1,ri);ri++;} }
        xp.getChildren().add(dg);

        row.setOnMouseClicked(e->{ boolean s=xp.isVisible(); xp.setVisible(!s); xp.setManaged(!s); exp.setText(s?"▼":"▲"); });
        card.getChildren().addAll(row,xp);
        return card;
    }
}
