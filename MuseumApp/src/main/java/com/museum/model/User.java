package com.museum.model;

public class User {
    public String id, username, password, name, email, phone, address, dob, gender, role, addedBy, createdAt;
    public boolean isAdmin()   { return "Admin".equals(role); }
    public boolean isCurator() { return "Curator".equals(role) || "Admin".equals(role); }
    public boolean isGuest()   { return "Guest".equals(role); }
}
