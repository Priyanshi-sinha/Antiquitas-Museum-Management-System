package com.museum.context;

import com.museum.data.MockData;
import com.museum.model.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class AppState {
    private static AppState instance;

    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>(null);
    private final ObservableList<User>         users         = FXCollections.observableArrayList(MockData.users());
    private final ObservableList<Artifact>     artifacts     = FXCollections.observableArrayList(MockData.artifacts());
    private final ObservableList<Period>        periods       = FXCollections.observableArrayList(MockData.periods());
    private final ObservableList<Civilization> civilizations = FXCollections.observableArrayList(MockData.civilizations());
    private final ObservableList<Person>        persons       = FXCollections.observableArrayList(MockData.persons());
    private final ObservableList<Organization>  organizations = FXCollections.observableArrayList(MockData.organizations());
    private final ObservableList<MuseumCollection>    collections   = FXCollections.observableArrayList(MockData.collections());
    private final ObservableList<Log>           logs          = FXCollections.observableArrayList(MockData.logs());
    private int logSeq = 20;

    private AppState() {}
    public static AppState get() { if (instance == null) instance = new AppState(); return instance; }

    // ── Auth ────────────────────────────────────────────────────────────────
    public boolean login(String username, String password) {
        return users.stream()
            .filter(u -> u.username.equals(username) && u.password.equals(password))
            .findFirst()
            .map(u -> { currentUser.set(u); return true; })
            .orElse(false);
    }
    public void logout() { currentUser.set(null); }
    public User          getCurrentUser()         { return currentUser.get(); }
    public ObjectProperty<User> currentUserProp() { return currentUser; }
    public boolean isLoggedIn() { return currentUser.get() != null; }
    public boolean isAdmin()    { return currentUser.get() != null && currentUser.get().isAdmin(); }
    public boolean isCurator()  { return currentUser.get() != null && currentUser.get().isCurator(); }

    // ── Getters ─────────────────────────────────────────────────────────────
    public ObservableList<User>         getUsers()         { return users; }
    public ObservableList<Artifact>     getArtifacts()     { return artifacts; }
    public ObservableList<Period>        getPeriods()       { return periods; }
    public ObservableList<Civilization> getCivilizations() { return civilizations; }
    public ObservableList<Person>        getPersons()       { return persons; }
    public ObservableList<Organization>  getOrganizations() { return organizations; }
    public ObservableList<MuseumCollection>    getCollections()   { return collections; }
    public ObservableList<Log>           getLogs()          { return logs; }

    // ── Lookup ──────────────────────────────────────────────────────────────
    public Artifact      findArtifact(String id)    { return artifacts.stream().filter(a->a.id.equals(id)).findFirst().orElse(null); }
    public Person        findPerson(String id)       { return persons.stream().filter(p->p.id.equals(id)).findFirst().orElse(null); }
    public Organization  findOrg(String id)          { return organizations.stream().filter(o->o.id.equals(id)).findFirst().orElse(null); }
    public Civilization  findCiv(String id)          { return civilizations.stream().filter(c->c.id.equals(id)).findFirst().orElse(null); }
    public Period        findPeriod(String id)       { return periods.stream().filter(p->p.id.equals(id)).findFirst().orElse(null); }
    public MuseumCollection findCollection(String id)   { return collections.stream().filter(c->c.id.equals(id)).findFirst().orElse(null); }

    // ── Artifact CRUD ────────────────────────────────────────────────────────
    public void addArtifact(Artifact a) {
        a.id = "a" + UUID.randomUUID().toString().substring(0,8);
        a.createdAt = a.updatedAt = now();
        artifacts.add(a);
        addLog("Artifact", a.id, a.name, whoAmI(), "Created", null);
    }
    public void updateArtifact(Artifact updated) {
        for (int i = 0; i < artifacts.size(); i++) {
            if (artifacts.get(i).id.equals(updated.id)) {
                updated.updatedAt = now();
                artifacts.set(i, updated);
                addLog("Artifact", updated.id, updated.name, whoAmI(), "Updated", null);
                return;
            }
        }
    }
    public void deleteArtifact(String id) {
        artifacts.removeIf(a -> {
            if (a.id.equals(id)) { addLog("Artifact", a.id, a.name, whoAmI(), "Deleted", null); return true; }
            return false;
        });
    }

    // ── Period CRUD ───────────────────────────────────────────────────────────
    public void addPeriod(Period p) {
        p.id = "p" + UUID.randomUUID().toString().substring(0,8);
        p.createdAt = p.updatedAt = now();
        periods.add(p);
        addLog("Period", p.id, p.name, whoAmI(), "Created", null);
    }
    public void updatePeriod(Period updated) {
        for (int i = 0; i < periods.size(); i++) {
            if (periods.get(i).id.equals(updated.id)) { updated.updatedAt=now(); periods.set(i,updated); addLog("Period",updated.id,updated.name,whoAmI(),"Updated",null); return; }
        }
    }
    public void deletePeriod(String id) { periods.removeIf(p->{ if(p.id.equals(id)){addLog("Period",p.id,p.name,whoAmI(),"Deleted",null);return true;}return false;}); }

    // ── Civilization CRUD ─────────────────────────────────────────────────────
    public void addCivilization(Civilization c) {
        c.id = "c" + UUID.randomUUID().toString().substring(0,8);
        c.createdAt = c.updatedAt = now();
        civilizations.add(c);
        addLog("Civilization", c.id, c.name, whoAmI(), "Created", null);
    }
    public void updateCivilization(Civilization updated) {
        for (int i = 0; i < civilizations.size(); i++) {
            if (civilizations.get(i).id.equals(updated.id)) { updated.updatedAt=now(); civilizations.set(i,updated); addLog("Civilization",updated.id,updated.name,whoAmI(),"Updated",null); return; }
        }
    }
    public void deleteCivilization(String id) { civilizations.removeIf(c->{ if(c.id.equals(id)){addLog("Civilization",c.id,c.name,whoAmI(),"Deleted",null);return true;}return false;}); }

    // ── Person CRUD ───────────────────────────────────────────────────────────
    public void addPerson(Person p) {
        p.id = "per" + UUID.randomUUID().toString().substring(0,8);
        p.createdAt = p.updatedAt = now();
        persons.add(p);
        addLog("Person", p.id, p.name, whoAmI(), "Created", null);
    }
    public void updatePerson(Person updated) {
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).id.equals(updated.id)) { updated.updatedAt=now(); persons.set(i,updated); addLog("Person",updated.id,updated.name,whoAmI(),"Updated",null); return; }
        }
    }
    public void deletePerson(String id) { persons.removeIf(p->{ if(p.id.equals(id)){addLog("Person",p.id,p.name,whoAmI(),"Deleted",null);return true;}return false;}); }

    // ── Organization CRUD ─────────────────────────────────────────────────────
    public void addOrganization(Organization o) {
        o.id = "o" + UUID.randomUUID().toString().substring(0,8);
        o.createdAt = o.updatedAt = now();
        organizations.add(o);
        addLog("Organization", o.id, o.name, whoAmI(), "Created", null);
    }
    public void updateOrganization(Organization updated) {
        for (int i = 0; i < organizations.size(); i++) {
            if (organizations.get(i).id.equals(updated.id)) { updated.updatedAt=now(); organizations.set(i,updated); addLog("Organization",updated.id,updated.name,whoAmI(),"Updated",null); return; }
        }
    }
    public void deleteOrganization(String id) { organizations.removeIf(o->{ if(o.id.equals(id)){addLog("Organization",o.id,o.name,whoAmI(),"Deleted",null);return true;}return false;}); }

    // ── MuseumCollection CRUD ─────────────────────────────────────────────────
    public void addCollection(MuseumCollection c) {
        c.id = "col" + UUID.randomUUID().toString().substring(0,8);
        c.createdAt = c.updatedAt = now();
        collections.add(c);
        addLog("Collection", c.id, c.name, whoAmI(), "Created", null);
    }
    public void updateCollection(MuseumCollection updated) {
        for (int i = 0; i < collections.size(); i++) {
            if (collections.get(i).id.equals(updated.id)) { updated.updatedAt=now(); collections.set(i,updated); addLog("Collection",updated.id,updated.name,whoAmI(),"Updated",null); return; }
        }
    }
    public void deleteCollection(String id) { collections.removeIf(c->{ if(c.id.equals(id)){addLog("Collection",c.id,c.name,whoAmI(),"Deleted",null);return true;}return false;}); }

    // ── User CRUD ─────────────────────────────────────────────────────────────
    public void addUser(User u) {
        u.id = "u" + UUID.randomUUID().toString().substring(0,8);
        u.createdAt = now();
        u.addedBy = whoAmI();
        users.add(u);
        addLog("User", u.id, u.name, whoAmI(), "Created", null);
    }
    public void deleteUser(String id) {
        users.removeIf(u -> {
            if (u.id.equals(id)) { addLog("User", u.id, u.name, whoAmI(), "Deleted", null); return true; }
            return false;
        });
    }

    // ── Logging ──────────────────────────────────────────────────────────────
    public void addLog(String itemType, String itemId, String itemName, String by, String changeType, List<Log.LogChange> changes) {
        Log log = new Log();
        log.id = "l" + (++logSeq);
        log.itemType = itemType; log.itemId = itemId; log.itemName = itemName;
        log.changedBy = by; log.changeType = changeType; log.changedAt = now();
        if (changes != null) log.changes.addAll(changes);
        logs.add(0, log);
    }

    private String now()    { return Instant.now().toString(); }
    private String whoAmI() { return currentUser.get() != null ? currentUser.get().username : "system"; }
}
