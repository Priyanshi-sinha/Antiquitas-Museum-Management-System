package com.museum.controller;

import com.museum.context.AppState;
import com.museum.model.Artifact;
import com.museum.util.Theme;
import com.museum.util.UI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ChatBotPage extends VBox {
    private final AppState S = AppState.get();
    private VBox chatBox;
    private TextField inputF;

    public ChatBotPage() {
        setStyle("-fx-background-color:"+Theme.BG_DARK+";");
        VBox.setVgrow(this,Priority.ALWAYS);
        build();
    }

    private void build() {
        HBox hdr=new HBox(10);hdr.setPadding(new Insets(10,16,10,16));hdr.setAlignment(Pos.CENTER_LEFT);
        hdr.setStyle("-fx-background-color:"+Theme.BG_HEADER+";-fx-border-color:transparent transparent "+Theme.BORDER+" transparent;");
        Label ico=new Label("🏛");ico.setStyle("-fx-text-fill:"+Theme.BLUE+";-fx-font-size:18px;-fx-background-color:"+Theme.BLUE+"22;-fx-min-width:30px;-fx-min-height:30px;-fx-max-width:30px;-fx-max-height:30px;-fx-alignment:center;-fx-background-radius:4;");
        VBox info=new VBox(1,UI.label("Museum AI Guide",Theme.TEXT_PRIMARY,13,true),UI.label("Ask me anything about our collection",Theme.TEXT_DIMMER,10));
        hdr.getChildren().addAll(ico,info);

        chatBox=new VBox(10);chatBox.setPadding(new Insets(16));chatBox.setFillWidth(true);
        ScrollPane sp=UI.scrollPane(chatBox);VBox.setVgrow(sp,Priority.ALWAYS);
        sp.vvalueProperty().bind(chatBox.heightProperty());

        HBox inputBar=new HBox(8);inputBar.setPadding(new Insets(10,16,12,16));inputBar.setAlignment(Pos.CENTER);
        inputBar.setStyle("-fx-background-color:"+Theme.BG_HEADER+";-fx-border-color:"+Theme.BORDER+" transparent transparent transparent;");
        inputF=UI.textField("Ask about our collection…");HBox.setHgrow(inputF,Priority.ALWAYS);
        Button send=UI.goldBtn("Send");send.setMinWidth(70);
        send.setOnAction(e->sendMsg());inputF.setOnAction(e->sendMsg());
        inputBar.getChildren().addAll(inputF,send);

        getChildren().addAll(hdr,sp,inputBar);

        // Quick prompts
        HBox prompts=new HBox(6);prompts.setAlignment(Pos.CENTER_LEFT);prompts.setPadding(new Insets(0,0,4,0));
        for(String p:new String[]{"Artifacts from Egypt","Weapons in collection","On display items","Ancient Greek items"}){
            Button chip=new Button(p);
            chip.setStyle("-fx-background-color:"+Theme.BG_INPUT+";-fx-text-fill:"+Theme.TEXT_SECONDARY+";-fx-font-size:10px;-fx-padding:3 8;-fx-background-radius:12;-fx-cursor:hand;-fx-border-color:"+Theme.BORDER_INPUT+";-fx-border-radius:12;");
            chip.setOnAction(e->{inputF.setText(p);sendMsg();});
            prompts.getChildren().add(chip);}
        chatBox.getChildren().add(0,prompts);

        botMsg("Welcome to the Antiquitas Museum AI Guide! 👋\n\nI can help you explore our collection. Try asking:\n• \"What artifacts are from Egypt?\"\n• \"Show me weapons\"\n• \"How many artifacts are on display?\"\n• \"What civilizations are represented?\"\n• \"Tell me about coins\"");
    }

    private void sendMsg() {
        String txt=inputF.getText().trim(); if(txt.isEmpty()) return;
        inputF.clear(); userMsg(txt);
        HBox thinking=botBubble("…"); chatBox.getChildren().add(thinking);
        new Thread(()->{
            try{Thread.sleep(450);}catch(Exception ignored){}
            String resp=respond(txt);
            Platform.runLater(()->{chatBox.getChildren().remove(thinking);botMsg(resp);});
        }).start();
    }

    private String respond(String q) {
        String ql=q.toLowerCase();

        if(ql.contains("egypt")||ql.contains("egyptian")){
            var arts=S.getArtifacts().stream().filter(a->a.origin.country.equalsIgnoreCase("Egypt")).toList();
            if(arts.isEmpty()) return "No artifacts from Egypt currently in the collection.";
            StringBuilder sb=new StringBuilder("We have "+arts.size()+" artifact(s) from Egypt:\n\n");
            arts.forEach(a->sb.append("• ").append(a.name).append(" (").append(a.origin.century).append(")\n"));
            return sb.toString();
        }
        if(ql.contains("greek")||ql.contains("greece")){
            var arts=S.getArtifacts().stream().filter(a->"Greece".equalsIgnoreCase(a.origin.country)).toList();
            StringBuilder sb=new StringBuilder("Greek artifacts ("+arts.size()+"):\n\n");
            arts.forEach(a->sb.append("• ").append(a.name).append(" — ").append(a.mainCategory).append("\n"));
            return sb.toString();
        }
        if(ql.contains("roman")||ql.contains("rome")||ql.contains("italy")){
            var arts=S.getArtifacts().stream().filter(a->"Italy".equalsIgnoreCase(a.origin.country)||"c3".equals(a.origin.civilizationId)).toList();
            StringBuilder sb=new StringBuilder("Roman artifacts ("+arts.size()+"):\n\n");
            arts.forEach(a->sb.append("• ").append(a.name).append(" — ").append(a.mainCategory).append("\n"));
            return sb.toString();
        }
        if(ql.contains("weapon")||ql.contains("sword")){
            var arts=S.getArtifacts().stream().filter(a->"Weapons".equalsIgnoreCase(a.mainCategory)).toList();
            StringBuilder sb=new StringBuilder("Weapons in collection ("+arts.size()+"):\n\n");
            arts.forEach(a->sb.append("• ").append(a.name).append(" (").append(a.origin.country).append(")\n"));
            return sb.toString();
        }
        if(ql.contains("coin")){
            var arts=S.getArtifacts().stream().filter(a->"Coins".equalsIgnoreCase(a.mainCategory)).toList();
            StringBuilder sb=new StringBuilder("Coins in collection ("+arts.size()+"):\n\n");
            arts.forEach(a->sb.append("• ").append(a.name).append(" — ").append(a.origin.place).append("\n"));
            return sb.toString();
        }
        if(ql.contains("jewelry")||ql.contains("jewel")){
            var arts=S.getArtifacts().stream().filter(a->"Jewelry".equalsIgnoreCase(a.mainCategory)).toList();
            StringBuilder sb=new StringBuilder("Jewelry pieces ("+arts.size()+"):\n\n");
            arts.forEach(a->sb.append("• ").append(a.name).append(" (").append(a.origin.country).append(")\n"));
            return sb.toString();
        }
        if(ql.contains("display")){
            long on=S.getArtifacts().stream().filter(a->a.onDisplay).count();
            return on+" artifact(s) currently on public display.\n"+(S.getArtifacts().size()-on)+" in storage or on loan.";
        }
        if(ql.contains("how many")||ql.contains("total")||ql.contains("stats")||ql.contains("statistic")){
            return "📊 Collection Statistics:\n\n• Artifacts: "+S.getArtifacts().size()+"\n• Collections: "+S.getCollections().size()+"\n• Civilizations: "+S.getCivilizations().size()+"\n• Historical Periods: "+S.getPeriods().size()+"\n• On Display: "+S.getArtifacts().stream().filter(a->a.onDisplay).count()+"\n• Notable Persons: "+S.getPersons().size();
        }
        if(ql.contains("civilization")||ql.contains("culture")){
            StringBuilder sb=new StringBuilder("Civilizations in our collection:\n\n");
            S.getCivilizations().forEach(c->sb.append("• ").append(c.name).append("\n"));
            return sb.toString();
        }
        if(ql.contains("collection")){
            StringBuilder sb=new StringBuilder("Our curated collections:\n\n");
            S.getCollections().forEach(c->sb.append("• ").append(c.name).append("\n  ").append(c.description.length()>80?c.description.substring(0,80)+"…":c.description).append("\n"));
            return sb.toString();
        }
        if(ql.contains("period")||ql.contains("era")||ql.contains("age")){
            StringBuilder sb=new StringBuilder("Historical periods:\n\n");
            S.getPeriods().forEach(p->sb.append("• ").append(p.name).append(" (").append(p.getYearRange()).append(")\n"));
            return sb.toString();
        }
        // Keyword search in artifact names
        for(String w:ql.split("\\s+")){
            if(w.length()>3){
                var m=S.getArtifacts().stream().filter(a->a.name.toLowerCase().contains(w)||a.description.toLowerCase().contains(w)).toList();
                if(!m.isEmpty()){
                    StringBuilder sb=new StringBuilder("Found "+m.size()+" item(s) matching \""+w+"\":\n\n");
                    m.subList(0,Math.min(5,m.size())).forEach(a->sb.append("• ").append(a.name).append(" (").append(a.mainCategory).append(", ").append(a.origin.country).append(")\n"));
                    return sb.toString();
                }
            }
        }
        return "I'm not sure about that. Try asking:\n\n• \"Artifacts from Egypt\"\n• \"Show me weapons\"\n• \"What civilizations are here?\"\n• \"How many artifacts on display?\"\n• \"Tell me about collections\"";
    }

    private void userMsg(String text) {
        HBox bubble=new HBox();bubble.setAlignment(Pos.CENTER_RIGHT);
        Label lbl=new Label(text);
        lbl.setStyle("-fx-background-color:"+Theme.GOLD+";-fx-text-fill:#1a1d2e;-fx-padding:8 12;-fx-background-radius:12 12 2 12;-fx-font-size:12px;");
        lbl.setWrapText(true);lbl.setMaxWidth(480);
        bubble.getChildren().add(lbl);chatBox.getChildren().add(bubble);
    }

    private void botMsg(String text) { chatBox.getChildren().add(botBubble(text)); }

    private HBox botBubble(String text) {
        HBox bubble=new HBox(8);bubble.setAlignment(Pos.TOP_LEFT);
        Label av=new Label("🏛");av.setStyle("-fx-font-size:14px;-fx-min-width:28px;-fx-min-height:28px;-fx-max-width:28px;-fx-max-height:28px;-fx-alignment:center;-fx-background-color:"+Theme.BG_INPUT+";-fx-background-radius:14;");
        Label lbl=new Label(text);
        lbl.setStyle("-fx-background-color:"+Theme.BG_PANEL+";-fx-text-fill:"+Theme.TEXT_SECONDARY+";-fx-padding:8 12;-fx-background-radius:12 12 12 2;-fx-font-size:12px;-fx-border-color:"+Theme.BORDER+";-fx-border-radius:12 12 12 2;");
        lbl.setWrapText(true);lbl.setMaxWidth(500);
        bubble.getChildren().addAll(av,lbl);return bubble;
    }
}