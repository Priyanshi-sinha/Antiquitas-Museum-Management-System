package com.museum.controller;

import com.museum.context.AppState;
import com.museum.data.MockData;
import com.museum.model.*;
import com.museum.util.Navigator;
import com.museum.util.Theme;
import com.museum.util.UI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;

public class ItemAddEditPage extends VBox {

    private final AppState S = AppState.get();
    private final String editId;
    private final String editType;
    private final boolean isEdit;

    // Item-type picker (add mode only)
    private ComboBox<String> itemTypeCB;

    // ── Artifact fields ───────────────────────────────────────────────────────
    private TextField     artNameF, artPhotoF, artCountryF, artPlaceF, artDateF, artAgeF, artCenturyF;
    private TextArea      artDescA;
    private ComboBox<String> artMainCatCB, artSubCatCB, artCivCB, artPerCB, artColCB;
    private CheckBox      artInMuseumCB, artOnDisplayCB;

    // Predefined attribute TextFields: label -> field
    private final Map<String, TextField> mainAttrFields = new LinkedHashMap<>();
    private final Map<String, TextField> subAttrFields  = new LinkedHashMap<>();

    // Relation rows
    private VBox personRelBox, artifactRelBox, orgRelBox;
    private final List<String[]> personRelRows   = new ArrayList<>(); // [personId, relType, desc]
    private final List<String[]> artifactRelRows = new ArrayList<>(); // [artifactId, relType, desc]
    private final List<String[]> orgRelRows      = new ArrayList<>(); // [orgId, relType, desc]
    // UI row nodes (parallel to *RelRows)
    private final List<HBox> personRelNodes   = new ArrayList<>();
    private final List<HBox> artifactRelNodes = new ArrayList<>();
    private final List<HBox> orgRelNodes      = new ArrayList<>();

    // Simple-item fields
    private TextField     simNameF, simStartYearF, simEndYearF;
    private TextArea      simDescA;
    private ComboBox<String> simPeriodCB, simOrgCB;

    // Shell nodes (built once)
    private VBox   formArea;
    private Label  pageTitle;

    // ─────────────────────────────────────────────────────────────────────────
    public ItemAddEditPage(String editId, String editType) {
        this.editId   = editId;
        this.editType = editType != null ? editType : "Artifact";
        this.isEdit   = editId != null;

        if (!S.isCurator()) {
            setAlignment(Pos.CENTER);
            setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
            getChildren().addAll(
                UI.label("Access Denied", Theme.RED, 18, true),
                UI.label("Curators and Admins only.", Theme.TEXT_MUTED, 12));
            return;
        }
        setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
        VBox.setVgrow(this, Priority.ALWAYS);
        ScrollPane sp = UI.scrollPane(buildShell());
        VBox.setVgrow(sp, Priority.ALWAYS);
        getChildren().add(sp);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SHELL (built once)
    // ═════════════════════════════════════════════════════════════════════════
    private VBox buildShell() {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color:" + Theme.BG_DARK + ";");

        // Header
        HBox hdr = new HBox(10);
        hdr.setPadding(new Insets(10, 16, 10, 16));
        hdr.setAlignment(Pos.CENTER_LEFT);
        hdr.setStyle("-fx-background-color:" + Theme.BG_HEADER
            + ";-fx-border-color:transparent transparent " + Theme.BORDER + " transparent;");
        Button back = UI.ghostBtn("\u2190 Cancel");
        back.setOnAction(e -> Navigator.go(new BrowsePage("All")));
        pageTitle = UI.label(isEdit ? "Edit Item" : "Add New Item", Theme.TEXT_PRIMARY, 14, true);
        hdr.getChildren().addAll(back, pageTitle);
        root.getChildren().add(hdr);

        // Type picker (add mode only)
        if (!isEdit) {
            HBox typeRow = new HBox(12);
            typeRow.setPadding(new Insets(14, 32, 4, 32));
            typeRow.setAlignment(Pos.CENTER_LEFT);
            typeRow.setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
            itemTypeCB = UI.combo();
            itemTypeCB.getItems().addAll("Artifact","Period","Civilization","Person","Organization","Collection");
            itemTypeCB.setValue("Artifact");
            itemTypeCB.setMinWidth(220);
            itemTypeCB.setOnAction(e -> switchForm(itemTypeCB.getValue()));
            typeRow.getChildren().addAll(UI.label("Item Type:", Theme.TEXT_SECONDARY, 12, true), itemTypeCB);
            root.getChildren().add(typeRow);
        }

        formArea = new VBox(18);
        formArea.setPadding(new Insets(12, 32, 32, 32));
        formArea.setMaxWidth(960);
        HBox centered = new HBox(formArea);
        centered.setAlignment(Pos.TOP_CENTER);
        centered.setStyle("-fx-background-color:" + Theme.BG_DARK + ";");
        root.getChildren().add(centered);

        switchForm(isEdit ? this.editType : "Artifact");
        return root;
    }

    private void switchForm(String type) {
        formArea.getChildren().clear();
        mainAttrFields.clear(); subAttrFields.clear();
        personRelRows.clear(); artifactRelRows.clear(); orgRelRows.clear();
        personRelNodes.clear(); artifactRelNodes.clear(); orgRelNodes.clear();
        pageTitle.setText(isEdit ? "Edit " + type : "Add " + type);
        switch (type) {
            case "Artifact"     -> buildArtifactForm();
            case "Period"       -> buildPeriodForm();
            case "Civilization" -> buildCivForm();
            case "Person"       -> buildPersonForm();
            case "Organization" -> buildOrgForm();
            case "Collection"   -> buildCollectionForm();
            default             -> buildArtifactForm();
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // ARTIFACT FORM
    // ═════════════════════════════════════════════════════════════════════════
    private void buildArtifactForm() {
        // ── Basic info ────────────────────────────────────────────────────
        artNameF  = UI.textField("Artifact name");
        artDescA  = UI.textArea("Detailed description\u2026");
        artDescA.setMinHeight(80); artDescA.setMaxHeight(110);
        artPhotoF = UI.textField("https://\u2026");

        artMainCatCB = UI.combo();
        artMainCatCB.getItems().addAll(MockData.MAIN_CATEGORIES);
        artMainCatCB.setMaxWidth(Double.MAX_VALUE);

        artSubCatCB = UI.combo();
        artSubCatCB.setMaxWidth(Double.MAX_VALUE);

        artMainCatCB.setOnAction(e -> {
            String m = artMainCatCB.getValue();
            artSubCatCB.getItems().clear();
            String[] subs = MockData.SUB_CATEGORIES.get(m);
            if (subs != null) artSubCatCB.getItems().addAll(subs);
            rebuildAttrPanels();
        });
        artSubCatCB.setOnAction(e -> rebuildAttrPanels());

        GridPane basic = grid2();
        basic.add(UI.formField("Name *",          artNameF),    0, 0, 2, 1);
        basic.add(UI.formField("Description",     artDescA),    0, 1, 2, 1);
        basic.add(UI.formField("Photo URL",       artPhotoF),   0, 2, 2, 1);
        basic.add(UI.formField("Main Category *", artMainCatCB),0, 3);
        basic.add(UI.formField("Sub Category",    artSubCatCB), 1, 3);
        formArea.getChildren().add(section("Basic Information", basic));

        // ── Origin ────────────────────────────────────────────────────────
        artCountryF = UI.textField("e.g. Egypt");
        artPlaceF   = UI.textField("e.g. Valley of the Kings");
        artDateF    = UI.textField("e.g. 1350 BCE");
        artAgeF     = UI.textField("e.g. ~3374 years");
        artCenturyF = UI.textField("e.g. 14th Century BCE");

        artCivCB = UI.combo(); artCivCB.getItems().add("— None —");
        S.getCivilizations().forEach(c -> artCivCB.getItems().add(c.name + "|" + c.id));
        artCivCB.setValue("— None —"); artCivCB.setMaxWidth(Double.MAX_VALUE);

        artPerCB = UI.combo(); artPerCB.getItems().add("— None —");
        S.getPeriods().forEach(p -> artPerCB.getItems().add(p.name + "|" + p.id));
        artPerCB.setValue("— None —"); artPerCB.setMaxWidth(Double.MAX_VALUE);

        artColCB = UI.combo(); artColCB.getItems().add("— No Collection —");
        S.getCollections().forEach(c -> artColCB.getItems().add(c.name + "|" + c.id));
        artColCB.setValue("— No Collection —"); artColCB.setMaxWidth(Double.MAX_VALUE);

        artInMuseumCB  = UI.checkBox("In Museum");  artInMuseumCB.setSelected(true);
        artOnDisplayCB = UI.checkBox("On Public Display");

        GridPane origin = grid3();
        origin.add(UI.formField("Country",           artCountryF), 0, 0);
        origin.add(UI.formField("Place",             artPlaceF),   1, 0);
        origin.add(UI.formField("Date of Origin",    artDateF),    2, 0);
        origin.add(UI.formField("Approximate Age",   artAgeF),     0, 1);
        origin.add(UI.formField("Century",           artCenturyF), 1, 1);
        origin.add(UI.formField("Civilization",      artCivCB),    0, 2, 2, 1);
        origin.add(UI.formField("Historical Period", artPerCB),    2, 2);
        formArea.getChildren().add(section("Origin", origin));

        // ── Status ────────────────────────────────────────────────────────
        HBox statusRow = new HBox(20, artInMuseumCB, artOnDisplayCB,
            UI.hSpacer(), UI.formField("Collection", artColCB));
        statusRow.setAlignment(Pos.CENTER_LEFT);
        formArea.getChildren().add(section("Status & Collection", statusRow));

        // ── Category attribute panels (populated after category chosen) ──
        // Placeholders — rebuildAttrPanels() will fill them
        formArea.getChildren().add(new VBox()); // main attrs placeholder index
        formArea.getChildren().add(new VBox()); // sub  attrs placeholder index

        // ── Relations ─────────────────────────────────────────────────────
        formArea.getChildren().add(buildPersonRelSection());
        formArea.getChildren().add(buildArtifactRelSection());
        formArea.getChildren().add(buildOrgRelSection());

        // ── Save button ───────────────────────────────────────────────────
        formArea.getChildren().add(makeBottomSaveBtn("Artifact"));

        // Pre-select first category to populate attributes
        if (artMainCatCB.getItems().size() > 0 && !isEdit) {
            artMainCatCB.setValue(MockData.MAIN_CATEGORIES[0]);
            String[] subs = MockData.SUB_CATEGORIES.get(MockData.MAIN_CATEGORIES[0]);
            if (subs != null) { artSubCatCB.getItems().addAll(subs); artSubCatCB.setValue(subs[0]); }
        }
        rebuildAttrPanels();

        if (isEdit) prefillArtifact();
    }

    // ── Attribute panels rebuild ──────────────────────────────────────────────
    private static final int MAIN_ATTR_IDX = 3; // index in formArea of main attrs section
    private static final int SUB_ATTR_IDX  = 4; // index in formArea of sub attrs section

    private void rebuildAttrPanels() {
        mainAttrFields.clear();
        subAttrFields.clear();

        String mc = artMainCatCB != null ? artMainCatCB.getValue() : null;
        String sc = artSubCatCB  != null ? artSubCatCB.getValue()  : null;

        // Main common attrs
        String[] mainLabels = mc != null ? MockData.MAIN_COMMON_ATTRS.get(mc) : null;
        VBox mainPanel;
        if (mainLabels != null && mainLabels.length > 0) {
            GridPane g = gridN(2);
            for (int i = 0; i < mainLabels.length; i++) {
                TextField f = UI.textField("Enter " + mainLabels[i]);
                mainAttrFields.put(mainLabels[i], f);
                g.add(UI.formField(mainLabels[i], f), i % 2, i / 2);
            }
            mainPanel = asSection("Category Attributes (" + (mc != null ? mc : "") + ")", g);
        } else {
            mainPanel = new VBox(); // hidden if no attrs
        }

        // Sub-specific attrs
        String[] subLabels = sc != null ? MockData.SUB_ATTRS.get(sc) : null;
        VBox subPanel;
        if (subLabels != null && subLabels.length > 0) {
            GridPane g = gridN(2);
            for (int i = 0; i < subLabels.length; i++) {
                TextField f = UI.textField("Enter " + subLabels[i]);
                subAttrFields.put(subLabels[i], f);
                g.add(UI.formField(subLabels[i], f), i % 2, i / 2);
            }
            subPanel = asSection("Sub-Category Attributes (" + (sc != null ? sc : "") + ")", g);
        } else {
            subPanel = new VBox();
        }

        // Replace placeholders in-place
        if (formArea.getChildren().size() > MAIN_ATTR_IDX)
            formArea.getChildren().set(MAIN_ATTR_IDX, mainPanel);
        if (formArea.getChildren().size() > SUB_ATTR_IDX)
            formArea.getChildren().set(SUB_ATTR_IDX, subPanel);
    }

    // ── Relation sections ─────────────────────────────────────────────────────
    private VBox buildPersonRelSection() {
        personRelBox = new VBox(6);
        Button addBtn = UI.ghostBtn("+ Add Person Relation");
        addBtn.setOnAction(e -> addPersonRelRow(null, null, null));
        return section("Relations with Persons", new VBox(8, personRelBox, addBtn));
    }

    private VBox buildArtifactRelSection() {
        artifactRelBox = new VBox(6);
        Button addBtn = UI.ghostBtn("+ Add Artifact Relation");
        addBtn.setOnAction(e -> addArtifactRelRow(null, null, null));
        return section("Relations with Artifacts", new VBox(8, artifactRelBox, addBtn));
    }

    private VBox buildOrgRelSection() {
        orgRelBox = new VBox(6);
        Button addBtn = UI.ghostBtn("+ Add Organization Relation");
        addBtn.setOnAction(e -> addOrgRelRow(null, null, null));
        return section("Relations with Organizations", new VBox(8, orgRelBox, addBtn));
    }

    private void addPersonRelRow(String prePersonId, String preRelType, String preDesc) {
        ComboBox<String> personCB = UI.combo();
        personCB.getItems().add("— Select Person —");
        S.getPersons().forEach(p -> personCB.getItems().add(p.name + "|" + p.id));
        personCB.setValue(prePersonId != null
            ? S.getPersons().stream().filter(p->p.id.equals(prePersonId))
                .map(p->p.name+"|"+p.id).findFirst().orElse("— Select Person —")
            : "— Select Person —");
        personCB.setMinWidth(180);

        TextField relF  = UI.textField("Relation type (e.g. Owner)");
        if (preRelType != null) relF.setText(preRelType);
        TextField descF = UI.textField("Description (optional)");
        if (preDesc != null) descF.setText(preDesc);
        HBox.setHgrow(relF,  Priority.ALWAYS);
        HBox.setHgrow(descF, Priority.ALWAYS);

        Button rm = UI.dangerBtn("\u00d7");
        HBox row = new HBox(8, personCB, relF, descF, rm);
        row.setAlignment(Pos.CENTER_LEFT);

        String[] data = {prePersonId != null ? prePersonId : "", preRelType != null ? preRelType : "", preDesc != null ? preDesc : ""};
        personRelRows.add(data);
        personRelNodes.add(row);

        // Keep data in sync on changes
        personCB.setOnAction(e -> { String v=personCB.getValue(); data[0]=(v!=null&&v.contains("|"))?v.split("\\|")[1]:""; });
        relF.textProperty().addListener((o,a,v)->data[1]=v);
        descF.textProperty().addListener((o,a,v)->data[2]=v);

        rm.setOnAction(e -> {
            int idx = personRelNodes.indexOf(row);
            if (idx >= 0) { personRelRows.remove(idx); personRelNodes.remove(idx); }
            personRelBox.getChildren().remove(row);
        });
        personRelBox.getChildren().add(row);
    }

    private void addArtifactRelRow(String preArtId, String preRelType, String preDesc) {
        ComboBox<String> artCB = UI.combo();
        artCB.getItems().add("— Select Artifact —");
        S.getArtifacts().stream()
            .filter(a -> !a.id.equals(editId))  // exclude self when editing
            .forEach(a -> artCB.getItems().add(a.name + "|" + a.id));
        artCB.setValue(preArtId != null
            ? S.getArtifacts().stream().filter(a->a.id.equals(preArtId))
                .map(a->a.name+"|"+a.id).findFirst().orElse("— Select Artifact —")
            : "— Select Artifact —");
        artCB.setMinWidth(200);

        TextField relF  = UI.textField("Relation type (e.g. Same Collection)");
        if (preRelType != null) relF.setText(preRelType);
        TextField descF = UI.textField("Description (optional)");
        if (preDesc != null) descF.setText(preDesc);
        HBox.setHgrow(relF, Priority.ALWAYS);
        HBox.setHgrow(descF, Priority.ALWAYS);

        Button rm = UI.dangerBtn("\u00d7");
        HBox row = new HBox(8, artCB, relF, descF, rm);
        row.setAlignment(Pos.CENTER_LEFT);

        String[] data = {preArtId != null ? preArtId : "", preRelType != null ? preRelType : "", preDesc != null ? preDesc : ""};
        artifactRelRows.add(data);
        artifactRelNodes.add(row);

        artCB.setOnAction(e -> { String v=artCB.getValue(); data[0]=(v!=null&&v.contains("|"))?v.split("\\|")[1]:""; });
        relF.textProperty().addListener((o,a,v)->data[1]=v);
        descF.textProperty().addListener((o,a,v)->data[2]=v);

        rm.setOnAction(e -> {
            int idx = artifactRelNodes.indexOf(row);
            if (idx >= 0) { artifactRelRows.remove(idx); artifactRelNodes.remove(idx); }
            artifactRelBox.getChildren().remove(row);
        });
        artifactRelBox.getChildren().add(row);
    }

    private void addOrgRelRow(String preOrgId, String preRelType, String preDesc) {
        ComboBox<String> orgCB = UI.combo();
        orgCB.getItems().add("— Select Organization —");
        S.getOrganizations().forEach(o -> orgCB.getItems().add(o.name + "|" + o.id));
        orgCB.setValue(preOrgId != null
            ? S.getOrganizations().stream().filter(o->o.id.equals(preOrgId))
                .map(o->o.name+"|"+o.id).findFirst().orElse("— Select Organization —")
            : "— Select Organization —");
        orgCB.setMinWidth(200);

        TextField relF  = UI.textField("Relation type (e.g. Issuing Authority)");
        if (preRelType != null) relF.setText(preRelType);
        TextField descF = UI.textField("Description (optional)");
        if (preDesc != null) descF.setText(preDesc);
        HBox.setHgrow(relF, Priority.ALWAYS);
        HBox.setHgrow(descF, Priority.ALWAYS);

        Button rm = UI.dangerBtn("\u00d7");
        HBox row = new HBox(8, orgCB, relF, descF, rm);
        row.setAlignment(Pos.CENTER_LEFT);

        String[] data = {preOrgId != null ? preOrgId : "", preRelType != null ? preRelType : "", preDesc != null ? preDesc : ""};
        orgRelRows.add(data);
        orgRelNodes.add(row);

        orgCB.setOnAction(e -> { String v=orgCB.getValue(); data[0]=(v!=null&&v.contains("|"))?v.split("\\|")[1]:""; });
        relF.textProperty().addListener((o,a,v)->data[1]=v);
        descF.textProperty().addListener((o,a,v)->data[2]=v);

        rm.setOnAction(e -> {
            int idx = orgRelNodes.indexOf(row);
            if (idx >= 0) { orgRelRows.remove(idx); orgRelNodes.remove(idx); }
            orgRelBox.getChildren().remove(row);
        });
        orgRelBox.getChildren().add(row);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // PREFILL (edit mode)
    // ═════════════════════════════════════════════════════════════════════════
    private void prefillArtifact() {
        Artifact a = S.findArtifact(editId);
        if (a == null) return;

        artNameF.setText(safe(a.name));
        artDescA.setText(safe(a.description));
        artPhotoF.setText(safe(a.photo));

        if (a.mainCategory != null) {
            artMainCatCB.setValue(a.mainCategory);
            String[] subs = MockData.SUB_CATEGORIES.get(a.mainCategory);
            if (subs != null) { artSubCatCB.getItems().setAll(subs); }
        }
        if (a.subCategory != null) artSubCatCB.setValue(a.subCategory);

        // Rebuild attribute panels now that category is set, then fill values
        rebuildAttrPanels();
        for (var attr : a.mainCategoryAttributes) {
            TextField f = mainAttrFields.get(attr.label);
            if (f != null) f.setText(attr.value);
        }
        for (var attr : a.subCategoryAttributes) {
            TextField f = subAttrFields.get(attr.label);
            if (f != null) f.setText(attr.value);
        }

        artCountryF.setText(safe(a.origin.country));
        artPlaceF.setText(safe(a.origin.place));
        artDateF.setText(safe(a.origin.dateOrigin));
        artAgeF.setText(safe(a.origin.age));
        artCenturyF.setText(safe(a.origin.century));
        if (a.origin.civilizationId != null)
            artCivCB.getItems().stream().filter(s->s.endsWith("|"+a.origin.civilizationId)).findFirst().ifPresent(artCivCB::setValue);
        if (a.origin.periodId != null)
            artPerCB.getItems().stream().filter(s->s.endsWith("|"+a.origin.periodId)).findFirst().ifPresent(artPerCB::setValue);
        if (a.collectionId != null)
            artColCB.getItems().stream().filter(s->s.endsWith("|"+a.collectionId)).findFirst().ifPresent(artColCB::setValue);

        artInMuseumCB.setSelected(a.inMuseum);
        artOnDisplayCB.setSelected(a.onDisplay);

        // Prefill relations
        for (var cp : a.connectedPersons)       addPersonRelRow(cp.personId, cp.relationType, cp.description);
        for (var ca : a.connectedArtifacts)     addArtifactRelRow(ca.artifactId, ca.relationType, ca.description);
        for (var co : a.connectedOrganizations) addOrgRelRow(co.orgId, co.relationType, co.description);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SIMPLE ITEM FORMS (Period / Civ / Person / Org / Collection)
    // ═════════════════════════════════════════════════════════════════════════
    private void buildPeriodForm() {
        simNameF      = UI.textField("e.g. Bronze Age");
        simStartYearF = UI.textField("e.g. -3300  (negative = BCE)");
        simEndYearF   = UI.textField("e.g. -1200");
        GridPane g = grid2();
        g.add(UI.formField("Period Name *", simNameF),      0, 0, 2, 1);
        g.add(UI.formField("Start Year *",  simStartYearF), 0, 1);
        g.add(UI.formField("End Year *",    simEndYearF),   1, 1);
        formArea.getChildren().add(section("Period Details", g));
        formArea.getChildren().add(makeBottomSaveBtn("Period"));
        if (isEdit) {
            Period p = S.findPeriod(editId);
            if (p != null) { simNameF.setText(p.name); simStartYearF.setText(String.valueOf(p.startYear)); simEndYearF.setText(String.valueOf(p.endYear)); }
        }
    }

    private void buildCivForm() {
        simNameF    = UI.textField("e.g. Ancient Egyptian");
        simDescA    = UI.textArea("Description\u2026"); simDescA.setMinHeight(80);
        simPeriodCB = UI.combo(); simPeriodCB.getItems().add("— None —");
        S.getPeriods().forEach(p -> simPeriodCB.getItems().add(p.name + "|" + p.id));
        simPeriodCB.setValue("— None —"); simPeriodCB.setMaxWidth(Double.MAX_VALUE);
        GridPane g = grid2();
        g.add(UI.formField("Name *",            simNameF),    0, 0, 2, 1);
        g.add(UI.formField("Description",       simDescA),    0, 1, 2, 1);
        g.add(UI.formField("Associated Period", simPeriodCB), 0, 2);
        formArea.getChildren().add(section("Civilization Details", g));
        formArea.getChildren().add(makeBottomSaveBtn("Civilization"));
        if (isEdit) {
            Civilization c = S.findCiv(editId);
            if (c != null) {
                simNameF.setText(c.name); simDescA.setText(safe(c.description));
                if (c.periodId != null) simPeriodCB.getItems().stream().filter(s->s.endsWith("|"+c.periodId)).findFirst().ifPresent(simPeriodCB::setValue);
            }
        }
    }

    private void buildPersonForm() {
        simNameF = UI.textField("e.g. Cleopatra VII");
        simDescA = UI.textArea("Description\u2026"); simDescA.setMinHeight(80);
        simOrgCB = UI.combo(); simOrgCB.getItems().add("— None —");
        S.getOrganizations().forEach(o -> simOrgCB.getItems().add(o.name + "|" + o.id));
        simOrgCB.setValue("— None —"); simOrgCB.setMaxWidth(Double.MAX_VALUE);
        GridPane g = grid2();
        g.add(UI.formField("Name *",       simNameF), 0, 0, 2, 1);
        g.add(UI.formField("Description",  simDescA), 0, 1, 2, 1);
        g.add(UI.formField("Organization", simOrgCB), 0, 2);
        formArea.getChildren().add(section("Person Details", g));
        formArea.getChildren().add(makeBottomSaveBtn("Person"));
        if (isEdit) {
            Person p = S.findPerson(editId);
            if (p != null) {
                simNameF.setText(p.name); simDescA.setText(safe(p.description));
                if (p.organizationId != null) simOrgCB.getItems().stream().filter(s->s.endsWith("|"+p.organizationId)).findFirst().ifPresent(simOrgCB::setValue);
            }
        }
    }

    private void buildOrgForm() {
        simNameF = UI.textField("e.g. Roman Senate");
        simDescA = UI.textArea("Description\u2026"); simDescA.setMinHeight(80);
        GridPane g = grid2();
        g.add(UI.formField("Name *",      simNameF), 0, 0, 2, 1);
        g.add(UI.formField("Description", simDescA), 0, 1, 2, 1);
        formArea.getChildren().add(section("Organization Details", g));
        formArea.getChildren().add(makeBottomSaveBtn("Organization"));
        if (isEdit) {
            Organization o = S.findOrg(editId);
            if (o != null) { simNameF.setText(o.name); simDescA.setText(safe(o.description)); }
        }
    }

    private void buildCollectionForm() {
        simNameF = UI.textField("e.g. Mediterranean Antiquities");
        simDescA = UI.textArea("Description\u2026"); simDescA.setMinHeight(80);
        GridPane g = grid2();
        g.add(UI.formField("Name *",      simNameF), 0, 0, 2, 1);
        g.add(UI.formField("Description", simDescA), 0, 1, 2, 1);
        formArea.getChildren().add(section("Collection Details", g));
        formArea.getChildren().add(makeBottomSaveBtn("Collection"));
        if (isEdit) {
            MuseumCollection c = S.findCollection(editId);
            if (c != null) { simNameF.setText(c.name); simDescA.setText(safe(c.description)); }
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SAVE
    // ═════════════════════════════════════════════════════════════════════════
    private void saveItem() {
        String type = isEdit ? editType : (itemTypeCB != null ? itemTypeCB.getValue() : "Artifact");
        switch (type) {
            case "Artifact"     -> saveArtifact();
            case "Period"       -> savePeriod();
            case "Civilization" -> saveCiv();
            case "Person"       -> savePerson();
            case "Organization" -> saveOrg();
            case "Collection"   -> saveCollection();
        }
    }

    private void saveArtifact() {
        String n  = artNameF.getText().trim();
        String mc = artMainCatCB.getValue();
        if (n.isEmpty())                 { alert("Name is required."); return; }
        if (mc == null || mc.isEmpty())  { alert("Main category is required."); return; }

        Artifact a = isEdit ? cloneArtifact(S.findArtifact(editId)) : new Artifact();
        a.name = n; a.description = artDescA.getText().trim(); a.photo = artPhotoF.getText().trim();
        a.mainCategory = mc;
        a.subCategory  = artSubCatCB.getValue() != null ? artSubCatCB.getValue() : "";
        a.origin.country    = artCountryF.getText().trim();
        a.origin.place      = artPlaceF.getText().trim();
        a.origin.dateOrigin = artDateF.getText().trim();
        a.origin.age        = artAgeF.getText().trim();
        a.origin.century    = artCenturyF.getText().trim();
        a.inMuseum  = artInMuseumCB.isSelected();
        a.onDisplay = artOnDisplayCB.isSelected();

        String cv  = artCivCB.getValue(); a.origin.civilizationId = (cv  != null && cv.contains("|"))  ? cv.split("\\|")[1]  : null;
        String pv  = artPerCB.getValue(); a.origin.periodId       = (pv  != null && pv.contains("|"))  ? pv.split("\\|")[1]  : null;
        String clv = artColCB.getValue(); a.collectionId          = (clv != null && clv.contains("|")) ? clv.split("\\|")[1] : null;

        // Predefined main attributes
        a.mainCategoryAttributes.clear();
        for (var entry : mainAttrFields.entrySet()) {
            String val = entry.getValue().getText().trim();
            if (!val.isEmpty()) a.mainCategoryAttributes.add(new Artifact.AttributeDoc(entry.getKey(), val));
        }

        // Predefined sub attributes
        a.subCategoryAttributes.clear();
        for (var entry : subAttrFields.entrySet()) {
            String val = entry.getValue().getText().trim();
            if (!val.isEmpty()) a.subCategoryAttributes.add(new Artifact.AttributeDoc(entry.getKey(), val));
        }

        // Relations
        a.connectedPersons.clear();
        for (String[] row : personRelRows) {
            if (!row[0].isEmpty()) {
                Artifact.ConnectedPerson cp = new Artifact.ConnectedPerson();
                cp.personId = row[0]; cp.relationType = row[1]; cp.description = row[2];
                a.connectedPersons.add(cp);
            }
        }
        a.connectedArtifacts.clear();
        for (String[] row : artifactRelRows) {
            if (!row[0].isEmpty()) {
                Artifact.ConnectedArtifact ca = new Artifact.ConnectedArtifact();
                ca.artifactId = row[0]; ca.relationType = row[1]; ca.description = row[2];
                a.connectedArtifacts.add(ca);
            }
        }
        a.connectedOrganizations.clear();
        for (String[] row : orgRelRows) {
            if (!row[0].isEmpty()) {
                Artifact.ConnectedOrganization co = new Artifact.ConnectedOrganization();
                co.orgId = row[0]; co.relationType = row[1]; co.description = row[2];
                a.connectedOrganizations.add(co);
            }
        }

        if (isEdit) { S.updateArtifact(a); Navigator.go(new ItemDisplayPage(a.id)); }
        else        { S.addArtifact(a);    Navigator.go(new BrowsePage("Artifact")); }
    }

    private void savePeriod() {
        String n = simNameF.getText().trim();
        if (n.isEmpty()) { alert("Name is required."); return; }
        int start, end;
        try { start = Integer.parseInt(simStartYearF.getText().trim()); end = Integer.parseInt(simEndYearF.getText().trim()); }
        catch (NumberFormatException ex) { alert("Start and End Year must be integers (e.g. -3300)."); return; }
        Period p = new Period();
        if (isEdit) { Period ex = S.findPeriod(editId); if (ex != null) p.id = ex.id; }
        p.name = n; p.startYear = start; p.endYear = end;
        if (isEdit) S.updatePeriod(p); else S.addPeriod(p);
        Navigator.go(new BrowsePage("Period"));
    }

    private void saveCiv() {
        String n = simNameF.getText().trim();
        if (n.isEmpty()) { alert("Name is required."); return; }
        Civilization c = new Civilization();
        if (isEdit) { Civilization ex = S.findCiv(editId); if (ex != null) c.id = ex.id; }
        c.name = n; c.description = simDescA != null ? simDescA.getText().trim() : "";
        String pv = simPeriodCB != null ? simPeriodCB.getValue() : null;
        c.periodId = (pv != null && pv.contains("|")) ? pv.split("\\|")[1] : null;
        if (isEdit) S.updateCivilization(c); else S.addCivilization(c);
        Navigator.go(new BrowsePage("Civilization"));
    }

    private void savePerson() {
        String n = simNameF.getText().trim();
        if (n.isEmpty()) { alert("Name is required."); return; }
        Person p = new Person();
        if (isEdit) { Person ex = S.findPerson(editId); if (ex != null) p.id = ex.id; }
        p.name = n; p.description = simDescA != null ? simDescA.getText().trim() : "";
        String ov = simOrgCB != null ? simOrgCB.getValue() : null;
        p.organizationId = (ov != null && ov.contains("|")) ? ov.split("\\|")[1] : null;
        if (isEdit) S.updatePerson(p); else S.addPerson(p);
        Navigator.go(new BrowsePage("Person"));
    }

    private void saveOrg() {
        String n = simNameF.getText().trim();
        if (n.isEmpty()) { alert("Name is required."); return; }
        Organization o = new Organization();
        if (isEdit) { Organization ex = S.findOrg(editId); if (ex != null) o.id = ex.id; }
        o.name = n; o.description = simDescA != null ? simDescA.getText().trim() : "";
        if (isEdit) S.updateOrganization(o); else S.addOrganization(o);
        Navigator.go(new BrowsePage("Organization"));
    }

    private void saveCollection() {
        String n = simNameF.getText().trim();
        if (n.isEmpty()) { alert("Name is required."); return; }
        MuseumCollection c = new MuseumCollection();
        if (isEdit) { MuseumCollection ex = S.findCollection(editId); if (ex != null) c.id = ex.id; }
        c.name = n; c.description = simDescA != null ? simDescA.getText().trim() : "";
        if (isEdit) S.updateCollection(c); else S.addCollection(c);
        Navigator.go(new BrowsePage("Collection"));
    }

    // ═════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════════════════
    private VBox section(String title, javafx.scene.Node inner) {
        Label t = UI.label(title, Theme.TEXT_PRIMARY, 13, true);
        t.setStyle(t.getStyle() + "-fx-padding:0 0 6 0;"
            + "-fx-border-color:transparent transparent " + Theme.BORDER + " transparent;");
        VBox card = new VBox(12); card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color:" + Theme.BG_PANEL
            + ";-fx-border-color:" + Theme.BORDER + ";-fx-border-radius:6;-fx-background-radius:6;");
        card.getChildren().add(inner);
        return new VBox(8, t, card);
    }

    /** section() but returns VBox directly (used for inline replacement) */
    private VBox asSection(String title, javafx.scene.Node inner) { return section(title, inner); }

    private Button makeBottomSaveBtn(String type) {
        Button b = UI.goldBtn(isEdit ? "Save Changes" : "Add " + type);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPadding(new Insets(10));
        b.setOnAction(e -> saveItem());
        return b;
    }

    private GridPane grid2() {
        GridPane g = new GridPane(); g.setHgap(12); g.setVgap(10);
        for (int i = 0; i < 2; i++) { ColumnConstraints c = new ColumnConstraints(); c.setPercentWidth(50); g.getColumnConstraints().add(c); }
        return g;
    }

    private GridPane grid3() {
        GridPane g = new GridPane(); g.setHgap(12); g.setVgap(10);
        for (int i = 0; i < 3; i++) { ColumnConstraints c = new ColumnConstraints(); c.setPercentWidth(33.33); g.getColumnConstraints().add(c); }
        return g;
    }

    private GridPane gridN(int cols) {
        GridPane g = new GridPane(); g.setHgap(12); g.setVgap(10);
        for (int i = 0; i < cols; i++) { ColumnConstraints c = new ColumnConstraints(); c.setPercentWidth(100.0/cols); g.getColumnConstraints().add(c); }
        return g;
    }

    private Artifact cloneArtifact(Artifact src) {
        if (src == null) return new Artifact();
        Artifact a = new Artifact();
        a.id=src.id; a.name=src.name; a.photo=src.photo; a.description=src.description;
        a.mainCategory=src.mainCategory; a.subCategory=src.subCategory;
        a.inMuseum=src.inMuseum; a.onDisplay=src.onDisplay; a.collectionId=src.collectionId;
        a.origin.country=src.origin.country; a.origin.place=src.origin.place;
        a.origin.dateOrigin=src.origin.dateOrigin; a.origin.age=src.origin.age;
        a.origin.century=src.origin.century;
        a.origin.civilizationId=src.origin.civilizationId; a.origin.periodId=src.origin.periodId;
        a.mainCategoryAttributes.addAll(src.mainCategoryAttributes);
        a.subCategoryAttributes.addAll(src.subCategoryAttributes);
        a.connectedPersons.addAll(src.connectedPersons);
        a.connectedOrganizations.addAll(src.connectedOrganizations);
        a.connectedArtifacts.addAll(src.connectedArtifacts);
        a.createdAt=src.createdAt; a.updatedAt=src.updatedAt;
        return a;
    }

    private String safe(String s) { return s != null ? s : ""; }
    private void alert(String m) {
        Alert a = new Alert(Alert.AlertType.WARNING, m, ButtonType.OK);
        a.setHeaderText("Validation Error"); a.showAndWait();
    }
}
