package com.museum.model;

import java.util.ArrayList;
import java.util.List;

public class Artifact {
    public String id, name, photo, description, mainCategory, subCategory;
    public String type = "Artifact";
    public boolean inMuseum, onDisplay;
    public String collectionId;
    public List<AttributeDoc> mainCategoryAttributes = new ArrayList<>();
    public List<AttributeDoc> subCategoryAttributes  = new ArrayList<>();
    public ArtifactOrigin origin = new ArtifactOrigin();
    public List<ConnectedPerson>       connectedPersons       = new ArrayList<>();
    public List<ConnectedOrganization> connectedOrganizations = new ArrayList<>();
    public List<ConnectedArtifact>     connectedArtifacts     = new ArrayList<>();
    public String createdAt, updatedAt;

    public static class AttributeDoc {
        public String label, value;
        public AttributeDoc() {}
        public AttributeDoc(String l, String v) { label = l; value = v; }
        @Override public String toString() { return label + ": " + value; }
    }

    public static class ArtifactOrigin {
        public String country, place, dateOrigin, age, periodId, century, civilizationId;
    }

    public static class ConnectedPerson {
        public String personId, relationType, description;
    }

    public static class ConnectedOrganization {
        public String orgId, relationType, description;
    }

    public static class ConnectedArtifact {
        public String artifactId, relationType, description;
    }
}
