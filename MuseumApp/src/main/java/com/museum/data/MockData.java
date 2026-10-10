package com.museum.data;

import com.museum.model.*;
import java.util.*;

public class MockData {

    // ═════════════════════════════════════════════════════════════════════════
    // CATEGORY TAXONOMY
    // Each subcategory maps to its predefined attribute labels.
    // The form renders one TextField per label — no free-text label entry.
    // ═════════════════════════════════════════════════════════════════════════

    /** Ordered main categories */
    public static final String[] MAIN_CATEGORIES = {
        "Fine Arts",
        "Archaeological Objects",
        "Documents & Archives",
        "Textiles & Costumes",
        "Numismatics",
        "Natural History",
        "Science & Technology",
        "Cultural Objects",
        "Media",
        "Decorative Arts",
        "Military",
        "Transport",
        "Architecture",
        "Biographical",
        "Toys",
        "Digital"
    };

    /** Common attributes shared by all subcategories within a main category */
    public static final Map<String, String[]> MAIN_COMMON_ATTRS = new LinkedHashMap<>();
    static {
        MAIN_COMMON_ATTRS.put("Fine Arts",             new String[]{"Artistic Style","Subject","Art Movement","Signature"});
        MAIN_COMMON_ATTRS.put("Archaeological Objects",new String[]{"Excavation Site","Excavation Date","Layer / Stratum","Dating Method"});
        MAIN_COMMON_ATTRS.put("Documents & Archives",  new String[]{"Language","Script","Author","Publication Date"});
        MAIN_COMMON_ATTRS.put("Textiles & Costumes",   new String[]{"Fabric Type","Weaving Technique","Dye Type"});
        MAIN_COMMON_ATTRS.put("Numismatics",           new String[]{"Currency Type","Issuer","Year"});
        MAIN_COMMON_ATTRS.put("Natural History",       new String[]{"Scientific Name","Classification","Habitat"});
        MAIN_COMMON_ATTRS.put("Science & Technology",  new String[]{"Function","Manufacturer","Model"});
        MAIN_COMMON_ATTRS.put("Cultural Objects",      new String[]{"Cultural Group","Ritual Use"});
        MAIN_COMMON_ATTRS.put("Media",                 new String[]{"Format","Duration"});
        MAIN_COMMON_ATTRS.put("Decorative Arts",       new String[]{"Decorative Style","Craftsmanship Level"});
        MAIN_COMMON_ATTRS.put("Military",              new String[]{"Conflict Name","Side (Army)"});
        MAIN_COMMON_ATTRS.put("Transport",             new String[]{"Manufacturer","Year"});
        MAIN_COMMON_ATTRS.put("Architecture",          new String[]{"Structure Type","Style"});
        MAIN_COMMON_ATTRS.put("Biographical",          new String[]{"Person Name","Life Period"});
        MAIN_COMMON_ATTRS.put("Toys",                  new String[]{"Age Group","Material"});
        MAIN_COMMON_ATTRS.put("Digital",               new String[]{"File Type","Storage Location"});
    }

    /** Subcategories per main category */
    public static final Map<String, String[]> SUB_CATEGORIES = new LinkedHashMap<>();
    static {
        SUB_CATEGORIES.put("Fine Arts",             new String[]{"Painting","Drawing","Sculpture","Print","Installation","Mixed Media","Digital Art","Conceptual Art"});
        SUB_CATEGORIES.put("Archaeological Objects",new String[]{"Tool","Pottery & Ceramics","Weapon","Burial Item","Structure Fragment","Coin & Seal","Jewelry","Idol & Figurine"});
        SUB_CATEGORIES.put("Documents & Archives",  new String[]{"Manuscript","Letter","Government Record","Map","Diary","Book","Scroll","Certificate"});
        SUB_CATEGORIES.put("Textiles & Costumes",   new String[]{"Clothing","Uniform","Embroidery"});
        SUB_CATEGORIES.put("Numismatics",           new String[]{"Coin","Banknote","Medal"});
        SUB_CATEGORIES.put("Natural History",       new String[]{"Fossil","Mineral","Plant Specimen","Animal Specimen"});
        SUB_CATEGORIES.put("Science & Technology",  new String[]{"Scientific Instrument","Machine","Space Artifact"});
        SUB_CATEGORIES.put("Cultural Objects",      new String[]{"Musical Instrument","Mask","Ceremonial Object"});
        SUB_CATEGORIES.put("Media",                 new String[]{"Photograph","Film","Audio Recording"});
        SUB_CATEGORIES.put("Decorative Arts",       new String[]{"Furniture","Clock","Ceramic Ware","Glass Object"});
        SUB_CATEGORIES.put("Military",              new String[]{"Armor","Weapon","Uniform","Insignia"});
        SUB_CATEGORIES.put("Transport",             new String[]{"Land Vehicle","Watercraft","Aircraft"});
        SUB_CATEGORIES.put("Architecture",          new String[]{"Building Fragment","Architectural Model","Decorative Element"});
        SUB_CATEGORIES.put("Biographical",          new String[]{"Personal Memorabilia","Portrait","Document"});
        SUB_CATEGORIES.put("Toys",                  new String[]{"Game","Doll","Educational Toy","Mechanical Toy"});
        SUB_CATEGORIES.put("Digital",               new String[]{"Software","Dataset","Digital Image","3D Model"});
    }

    /** Attributes specific to each subcategory */
    public static final Map<String, String[]> SUB_ATTRS = new LinkedHashMap<>();
    static {
        // Fine Arts
        SUB_ATTRS.put("Painting",        new String[]{"Medium","Surface","Frame Type","Orientation","Varnish Status"});
        SUB_ATTRS.put("Drawing",         new String[]{"Drawing Medium","Paper Type","Line Technique","Shading Type"});
        SUB_ATTRS.put("Sculpture",       new String[]{"Material","Casting Method","Carving Technique","3D Dimensions","Weight (kg)"});
        SUB_ATTRS.put("Print",           new String[]{"Print Type","Edition Number","Plate Used","Ink Type"});
        SUB_ATTRS.put("Installation",    new String[]{"Components Count","Setup Instructions","Space Requirement","Interactive"});
        SUB_ATTRS.put("Mixed Media",     new String[]{"Materials Used","Composition Structure"});
        SUB_ATTRS.put("Digital Art",     new String[]{"File Format","Resolution","Software Used","Hardware Requirements"});
        SUB_ATTRS.put("Conceptual Art",  new String[]{"Concept Description","Documentation Type"});

        // Archaeological Objects
        SUB_ATTRS.put("Tool",            new String[]{"Tool Type","Material","Usage","Wear Marks"});
        SUB_ATTRS.put("Pottery & Ceramics", new String[]{"Clay Type","Firing Technique","Glaze Type","Decoration Pattern"});
        SUB_ATTRS.put("Weapon",          new String[]{"Weapon Type","Combat Use","Sharpness","Damage Marks"});
        SUB_ATTRS.put("Burial Item",     new String[]{"Burial Type","Associated Skeleton","Ritual Use"});
        SUB_ATTRS.put("Structure Fragment", new String[]{"Structure Type","Architecture Style","Original Location"});
        SUB_ATTRS.put("Coin & Seal",     new String[]{"Denomination","Inscription","Mint Location"});
        SUB_ATTRS.put("Jewelry",         new String[]{"Gemstones","Craft Technique","Material","Dimensions"});
        SUB_ATTRS.put("Idol & Figurine", new String[]{"Deity / Figure Name","Religious Context","Pose","Material"});

        // Documents & Archives
        SUB_ATTRS.put("Manuscript",      new String[]{"Writing Material","Ink Type","Number of Pages","Condition"});
        SUB_ATTRS.put("Letter",          new String[]{"Sender","Receiver","Date Sent","Condition"});
        SUB_ATTRS.put("Government Record", new String[]{"Department","Record Type","Classification Level"});
        SUB_ATTRS.put("Map",             new String[]{"Scale","Projection Type","Region Covered"});
        SUB_ATTRS.put("Diary",           new String[]{"Owner","Time Span","Number of Entries"});
        SUB_ATTRS.put("Book",            new String[]{"Publisher","Edition","Number of Pages","Genre"});
        SUB_ATTRS.put("Scroll",          new String[]{"Scroll Length","Rolling Direction","Material"});
        SUB_ATTRS.put("Certificate",     new String[]{"Issuing Authority","Validity","Awarded To"});

        // Textiles & Costumes
        SUB_ATTRS.put("Clothing",        new String[]{"Size","Gender Type","Occasion","Color"});
        SUB_ATTRS.put("Uniform",         new String[]{"Rank","Regiment","Branch","Condition"});
        SUB_ATTRS.put("Embroidery",      new String[]{"Stitch Type","Pattern","Thread Material"});

        // Numismatics
        SUB_ATTRS.put("Coin",            new String[]{"Metal","Diameter (mm)","Weight (g)","Grade / Condition"});
        SUB_ATTRS.put("Banknote",        new String[]{"Serial Number","Security Features","Denomination Value"});
        SUB_ATTRS.put("Medal",           new String[]{"Event","Award Level","Obverse Description","Reverse Description"});

        // Natural History
        SUB_ATTRS.put("Fossil",          new String[]{"Fossil Type","Geological Age","Formation Name","Completeness"});
        SUB_ATTRS.put("Mineral",         new String[]{"Hardness (Mohs)","Chemical Composition","Crystal System","Luster"});
        SUB_ATTRS.put("Plant Specimen",  new String[]{"Preservation Method","Collection Location","Collector Name"});
        SUB_ATTRS.put("Animal Specimen", new String[]{"Species","Preservation Type","Sex","Collection Date"});

        // Science & Technology
        SUB_ATTRS.put("Scientific Instrument", new String[]{"Accuracy","Measurement Type","Scale Range"});
        SUB_ATTRS.put("Machine",         new String[]{"Power Source","Capacity","Operating Speed"});
        SUB_ATTRS.put("Space Artifact",  new String[]{"Mission Name","Launch Date","Agency","Flight Duration"});

        // Cultural Objects
        SUB_ATTRS.put("Musical Instrument", new String[]{"Sound Type","Material","Playing Technique","Tuning"});
        SUB_ATTRS.put("Mask",            new String[]{"Occasion","Symbolism","Material","Wearable"});
        SUB_ATTRS.put("Ceremonial Object", new String[]{"Ceremony Type","Ritual Significance","Used By"});

        // Media
        SUB_ATTRS.put("Photograph",      new String[]{"Camera Type","Exposure","Film Type","Print Process"});
        SUB_ATTRS.put("Film",            new String[]{"Director","Runtime","Genre","Format"});
        SUB_ATTRS.put("Audio Recording", new String[]{"Bitrate","Recording Medium","Duration","Genre"});

        // Decorative Arts
        SUB_ATTRS.put("Furniture",       new String[]{"Wood Type","Usage","Style Period","Upholstery"});
        SUB_ATTRS.put("Clock",           new String[]{"Mechanism","Accuracy","Case Material","Movement Type"});
        SUB_ATTRS.put("Ceramic Ware",    new String[]{"Clay Body","Glaze","Firing Temperature","Decoration"});
        SUB_ATTRS.put("Glass Object",    new String[]{"Glass Type","Blowing Technique","Color","Transparency"});

        // Military
        SUB_ATTRS.put("Armor",           new String[]{"Protection Level","Material","Coverage Area","Weight (kg)"});
        // "Weapon" and "Uniform" already defined above

        // Transport
        SUB_ATTRS.put("Land Vehicle",    new String[]{"Engine Type","Fuel","Top Speed","Seats"});
        SUB_ATTRS.put("Watercraft",      new String[]{"Hull Material","Propulsion","Length (m)","Displacement"});
        SUB_ATTRS.put("Aircraft",        new String[]{"Range","Engine Type","Max Altitude","Wing Span"});

        // Architecture
        SUB_ATTRS.put("Building Fragment", new String[]{"Original Structure","Architectural Period","Material","Dimensions"});
        SUB_ATTRS.put("Architectural Model", new String[]{"Scale","Materials","Depicted Structure","Accuracy Level"});
        SUB_ATTRS.put("Decorative Element", new String[]{"Element Type","Original Position","Style"});

        // Biographical
        SUB_ATTRS.put("Personal Memorabilia", new String[]{"Associated Person","Item Type","Provenance"});
        SUB_ATTRS.put("Portrait",        new String[]{"Subject Name","Medium","Artist","Dimensions"});
        // "Document" already defined

        // Toys
        SUB_ATTRS.put("Game",            new String[]{"Game Type","Number of Players","Complete Set"});
        SUB_ATTRS.put("Doll",            new String[]{"Material","Movable Parts","Clothing Included"});
        SUB_ATTRS.put("Educational Toy", new String[]{"Subject Area","Learning Skill","Recommended Age"});
        SUB_ATTRS.put("Mechanical Toy",  new String[]{"Mechanism","Power Source","Moving Parts"});

        // Digital
        SUB_ATTRS.put("Software",        new String[]{"Version","Platform","License Type"});
        SUB_ATTRS.put("Dataset",         new String[]{"Data Format","Record Count","Collection Period"});
        SUB_ATTRS.put("Digital Image",   new String[]{"Resolution","Color Profile","File Size"});
        SUB_ATTRS.put("3D Model",        new String[]{"Polygon Count","File Format","Software Used"});
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SEED DATA
    // ═════════════════════════════════════════════════════════════════════════

    public static List<User> users() {
        List<User> l = new ArrayList<>();
        l.add(u("u1","admin","admin123","Dr. Alexandra Rhodes","admin@museum.org","+1 555-0100","1 Museum Drive, Athens, GA","1975-03-15","Female","Admin",null,"2023-01-01T08:00:00Z"));
        l.add(u("u2","curator1","curator123","Prof. James Hartley","jhartley@museum.org","+1 555-0201","42 Scholar Lane, Athens, GA","1980-07-22","Male","Curator","admin","2023-02-10T09:00:00Z"));
        l.add(u("u3","curator2","curator123","Dr. Lena Vasquez","lvasquez@museum.org","+1 555-0302","7 Heritage Blvd, Athens, GA","1985-11-05","Female","Curator","admin","2023-03-05T09:00:00Z"));
        l.add(u("u4","guest","guest123","Guest Visitor","guest@example.com","","","1990-01-01","Other","Guest",null,"2024-01-01T00:00:00Z"));
        return l;
    }
    private static User u(String id,String un,String pw,String name,String email,String phone,String addr,String dob,String gender,String role,String addedBy,String created) {
        User u=new User(); u.id=id; u.username=un; u.password=pw; u.name=name; u.email=email; u.phone=phone; u.address=addr; u.dob=dob; u.gender=gender; u.role=role; u.addedBy=addedBy; u.createdAt=created; return u;
    }

    public static List<Period> periods() {
        List<Period> l = new ArrayList<>();
        l.add(per("p1","Bronze Age",-3300,-1200));
        l.add(per("p2","Ancient Egyptian Kingdom",-3100,-30));
        l.add(per("p3","Classical Greece",-500,-323));
        l.add(per("p4","Roman Empire",-27,476));
        l.add(per("p5","Byzantine Empire",330,1453));
        l.add(per("p6","Iron Age",-1200,-550));
        return l;
    }
    private static Period per(String id,String name,int s,int e){Period p=new Period();p.id=id;p.name=name;p.startYear=s;p.endYear=e;p.createdAt="2023-01-05T10:00:00Z";p.updatedAt="2023-01-05T10:00:00Z";return p;}

    public static List<Civilization> civilizations() {
        List<Civilization> l = new ArrayList<>();
        l.add(civ("c1","Ancient Egyptian","p2","One of the world's earliest and longest-lasting civilizations, centered on the Nile River valley."));
        l.add(civ("c2","Ancient Greek","p3","A Mediterranean civilization known for its contributions to philosophy, art, democracy, and science."));
        l.add(civ("c3","Roman","p4","The civilization that grew from a small village on the Tiber River to dominate most of Europe."));
        l.add(civ("c4","Byzantine","p5","The Eastern Roman Empire that preserved Greco-Roman culture for a thousand years."));
        l.add(civ("c5","Mesopotamian","p1","The cradle of civilization between the Tigris and Euphrates rivers."));
        l.add(civ("c6","Persian","p3","The Achaemenid Empire, one of the largest empires of the ancient world."));
        l.add(civ("c7","Celtic","p6","Indo-European peoples of Iron Age and Medieval Europe known for their intricate art."));
        l.add(civ("c8","Minoan","p1","An Aegean Bronze Age civilization that arose on Crete circa 2700–1450 BCE."));
        return l;
    }
    private static Civilization civ(String id,String name,String pid,String desc){Civilization c=new Civilization();c.id=id;c.name=name;c.periodId=pid;c.description=desc;c.createdAt="2023-01-06T10:00:00Z";c.updatedAt="2023-01-06T10:00:00Z";return c;}

    public static List<Organization> organizations() {
        List<Organization> l = new ArrayList<>();
        l.add(org("o1","Egyptian Royal Court","The governing body of pharaonic Egypt, responsible for administration, religion, and military command."));
        l.add(org("o2","Roman Senate","The governing council of ancient Rome, comprising senators from noble families."));
        l.add(org("o3","Byzantine Imperial Court","The royal court of the Byzantine Empire, known for its elaborate ceremonial practices."));
        l.add(org("o4","Macedonian Army","The military force commanded by Alexander the Great during his conquests."));
        return l;
    }
    private static Organization org(String id,String name,String desc){Organization o=new Organization();o.id=id;o.name=name;o.description=desc;o.createdAt="2023-01-08T10:00:00Z";o.updatedAt="2023-01-08T10:00:00Z";return o;}

    public static List<Person> persons() {
        List<Person> l = new ArrayList<>();
        l.add(per2("per1","Tutankhamun","o1","Egyptian pharaoh of the 18th dynasty, known as the Boy King. Ruled from 1332–1323 BCE."));
        l.add(per2("per2","Cleopatra VII","o1","Last active ruler of the Ptolemaic Kingdom of Egypt."));
        l.add(per2("per3","Julius Caesar","o2","Roman general and statesman who played a critical role in the transformation of the Roman Republic."));
        l.add(per2("per4","Alexander the Great","o4","King of Macedon who created one of the largest empires in history."));
        l.add(per2("per5","Emperor Justinian I","o3","Byzantine emperor from 527–565 CE, known for codifying Roman law."));
        l.add(per2("per6","Darius the Great",null,"King of the Achaemenid Empire from 522–486 BCE."));
        return l;
    }
    private static Person per2(String id,String name,String orgId,String desc){Person p=new Person();p.id=id;p.name=name;p.organizationId=orgId;p.description=desc;p.createdAt="2023-01-09T10:00:00Z";p.updatedAt="2023-01-09T10:00:00Z";return p;}

    public static List<MuseumCollection> collections() {
        List<MuseumCollection> l = new ArrayList<>();
        l.add(col("col1","Mediterranean Antiquities","A curated selection of artifacts from ancient Mediterranean civilizations spanning three millennia."));
        l.add(col("col2","Ancient Egypt Treasures","Precious objects from pharaonic Egypt including funerary goods, jewelry, and religious artifacts."));
        l.add(col("col3","Greco-Roman Gallery","Sculpture, pottery, weapons, and daily objects from the Greek and Roman civilizations."));
        l.add(col("col4","Near East Collection","Objects from ancient Mesopotamia, Persia, and surrounding regions."));
        return l;
    }
    private static MuseumCollection col(String id,String name,String desc){MuseumCollection c=new MuseumCollection();c.id=id;c.name=name;c.description=desc;c.createdAt="2023-01-10T10:00:00Z";c.updatedAt="2023-01-10T10:00:00Z";return c;}

    public static List<Artifact> artifacts() {
        List<Artifact> l = new ArrayList<>();

        // 1. Golden Death Mask — Archaeological Objects / Jewelry
        Artifact a1 = base("a1","Golden Death Mask of Amenhotep",
            "https://images.unsplash.com/photo-1768914308444-7555a191b78b?w=600&q=80",
            "A magnificent golden funerary mask crafted for the high priest Amenhotep, dated to the New Kingdom period. Features inlaid lapis lazuli, carnelian, and faience eyes of obsidian and quartz.",
            "Archaeological Objects","Jewelry",true,true,"col2",
            "Egypt","Valley of the Kings, Luxor","1350 BCE","~3374 years","p2","14th Century BCE","c1",
            "2023-02-01T10:00:00Z","2024-06-15T14:30:00Z");
        // Main common attrs (Archaeological Objects)
        a1.mainCategoryAttributes.add(new Artifact.AttributeDoc("Excavation Site","Tomb KV-22, Valley of the Kings"));
        a1.mainCategoryAttributes.add(new Artifact.AttributeDoc("Excavation Date","1922"));
        a1.mainCategoryAttributes.add(new Artifact.AttributeDoc("Layer / Stratum","Primary burial chamber"));
        a1.mainCategoryAttributes.add(new Artifact.AttributeDoc("Dating Method","Contextual / Historical records"));
        // Sub attrs (Jewelry)
        a1.subCategoryAttributes.add(new Artifact.AttributeDoc("Gemstones","Lapis Lazuli, Carnelian, Obsidian"));
        a1.subCategoryAttributes.add(new Artifact.AttributeDoc("Craft Technique","Hammered gold with inlay"));
        a1.subCategoryAttributes.add(new Artifact.AttributeDoc("Material","Gold (22 carat)"));
        a1.subCategoryAttributes.add(new Artifact.AttributeDoc("Dimensions","54 cm height"));
        a1.connectedPersons.add(cp("per1","Burial Object","Found in the antechamber of Tutankhamun's tomb."));
        a1.connectedPersons.add(cp("per2","Historical Reference","Referenced in Cleopatra's court records."));
        a1.connectedOrganizations.add(co("o1","Originating Body","Commissioned by the Egyptian Royal Court."));
        a1.connectedArtifacts.add(ca("a5","Same Repository","Both found in sealed temple repositories."));
        l.add(a1);

        // 2. Parthenon Frieze — Fine Arts / Sculpture
        Artifact a2 = base("a2","Parthenon Frieze Fragment",
            "https://images.unsplash.com/photo-1774176105904-f3ed4b6323a0?w=600&q=80",
            "A fragment from the inner frieze of the Parthenon depicting the Panathenaic procession in high relief, showing horsemen in ceremonial dress. Retains traces of original polychrome paint.",
            "Fine Arts","Sculpture",true,true,"col3",
            "Greece","Acropolis, Athens","447 BCE","~2471 years","p3","5th Century BCE","c2",
            "2023-02-15T10:00:00Z","2023-11-20T09:00:00Z");
        a2.mainCategoryAttributes.add(new Artifact.AttributeDoc("Artistic Style","Classical Greek"));
        a2.mainCategoryAttributes.add(new Artifact.AttributeDoc("Subject","Panathenaic procession – horsemen"));
        a2.mainCategoryAttributes.add(new Artifact.AttributeDoc("Art Movement","Classical Antiquity"));
        a2.mainCategoryAttributes.add(new Artifact.AttributeDoc("Signature","No"));
        a2.subCategoryAttributes.add(new Artifact.AttributeDoc("Material","Pentelic Marble"));
        a2.subCategoryAttributes.add(new Artifact.AttributeDoc("Casting Method","N/A – carved"));
        a2.subCategoryAttributes.add(new Artifact.AttributeDoc("Carving Technique","High Relief"));
        a2.subCategoryAttributes.add(new Artifact.AttributeDoc("3D Dimensions","96 × 42 × 12 cm"));
        a2.subCategoryAttributes.add(new Artifact.AttributeDoc("Weight (kg)","~85"));
        a2.connectedArtifacts.add(ca("a3","Cultural Peer","Both represent Classical Greco-Roman craftsmanship."));
        l.add(a2);

        // 3. Roman Gladius — Archaeological Objects / Weapon
        Artifact a3 = base("a3","Roman Legion Gladius",
            "https://images.unsplash.com/photo-1672672088809-59acb7ad0e3a?w=600&q=80",
            "A well-preserved iron short sword (gladius hispaniensis) used by a Roman legionary. Blade 50 cm, bone grip wrapped in leather. Inscribed LEGIO XIV GMV.",
            "Archaeological Objects","Weapon",true,true,"col3",
            "Italy","Pompeii Barracks","70 CE","~1956 years","p4","1st Century CE","c3",
            "2023-03-01T10:00:00Z","2024-01-10T11:00:00Z");
        a3.mainCategoryAttributes.add(new Artifact.AttributeDoc("Excavation Site","Pompeii Barracks, Regio VI"));
        a3.mainCategoryAttributes.add(new Artifact.AttributeDoc("Excavation Date","1876"));
        a3.mainCategoryAttributes.add(new Artifact.AttributeDoc("Layer / Stratum","Volcanic ash layer (79 CE eruption)"));
        a3.mainCategoryAttributes.add(new Artifact.AttributeDoc("Dating Method","Stratigraphy / Inscription analysis"));
        a3.subCategoryAttributes.add(new Artifact.AttributeDoc("Weapon Type","Gladius Hispaniensis (short sword)"));
        a3.subCategoryAttributes.add(new Artifact.AttributeDoc("Combat Use","Close-quarters infantry combat"));
        a3.subCategoryAttributes.add(new Artifact.AttributeDoc("Sharpness","Partially preserved – edge blunted"));
        a3.subCategoryAttributes.add(new Artifact.AttributeDoc("Damage Marks","Notch near tip, grip wear"));
        a3.connectedPersons.add(cp("per3","Military Context","Type of weapon used by Caesar's legions."));
        a3.connectedOrganizations.add(co("o2","Issuing Authority","Issued under Roman Senate military command."));
        l.add(a3);

        // 4. Byzantine Gold Solidus — Numismatics / Coin
        Artifact a4 = base("a4","Byzantine Gold Solidus Coin",
            "https://images.unsplash.com/photo-1624365169298-a6318708491b?w=600&q=80",
            "Gold solidus minted under Emperor Justinian I, circa 540 CE. Obverse: emperor in full armour with globus cruciger. Reverse: Victory with christogram staff.",
            "Numismatics","Coin",true,false,"col1",
            "Turkey","Constantinople (Istanbul)","540 CE","~1486 years","p5","6th Century CE","c4",
            "2023-03-15T10:00:00Z","2023-12-01T13:00:00Z");
        a4.mainCategoryAttributes.add(new Artifact.AttributeDoc("Currency Type","Solidus"));
        a4.mainCategoryAttributes.add(new Artifact.AttributeDoc("Issuer","Emperor Justinian I"));
        a4.mainCategoryAttributes.add(new Artifact.AttributeDoc("Year","540 CE"));
        a4.subCategoryAttributes.add(new Artifact.AttributeDoc("Metal","Gold (24 carat)"));
        a4.subCategoryAttributes.add(new Artifact.AttributeDoc("Diameter (mm)","21"));
        a4.subCategoryAttributes.add(new Artifact.AttributeDoc("Weight (g)","4.5"));
        a4.subCategoryAttributes.add(new Artifact.AttributeDoc("Grade / Condition","EF – Extremely Fine"));
        a4.connectedPersons.add(cp("per5","Depicted On","Emperor Justinian I depicted on the obverse."));
        a4.connectedOrganizations.add(co("o3","Issuing Authority","Minted by the Byzantine Imperial Court."));
        l.add(a4);

        // 5. Minoan Snake Goddess — Archaeological Objects / Idol & Figurine
        Artifact a5 = base("a5","Minoan Snake Goddess Figurine",
            "https://images.unsplash.com/photo-1768914308444-7555a191b78b?w=600&q=80",
            "A faience figurine depicting the Minoan Snake Goddess from a temple repository at Knossos. Goddess shown in elaborate tiered skirt holding a snake in each raised hand. Height: 34.5 cm.",
            "Archaeological Objects","Idol & Figurine",true,true,"col1",
            "Greece","Knossos, Crete","1600 BCE","~3626 years","p1","17th Century BCE","c8",
            "2023-04-01T10:00:00Z","2023-10-05T08:00:00Z");
        a5.mainCategoryAttributes.add(new Artifact.AttributeDoc("Excavation Site","Temple Repository, Knossos Palace"));
        a5.mainCategoryAttributes.add(new Artifact.AttributeDoc("Excavation Date","1903"));
        a5.mainCategoryAttributes.add(new Artifact.AttributeDoc("Layer / Stratum","Middle Minoan III"));
        a5.mainCategoryAttributes.add(new Artifact.AttributeDoc("Dating Method","Stratigraphic / Style analysis"));
        a5.subCategoryAttributes.add(new Artifact.AttributeDoc("Deity / Figure Name","Snake Goddess / Household Goddess"));
        a5.subCategoryAttributes.add(new Artifact.AttributeDoc("Religious Context","Minoan palace religion"));
        a5.subCategoryAttributes.add(new Artifact.AttributeDoc("Pose","Standing, arms raised, snakes held"));
        a5.subCategoryAttributes.add(new Artifact.AttributeDoc("Material","Faience (glazed quartz paste) – 34.5 cm"));
        a5.connectedArtifacts.add(ca("a1","Same Repository","Both discovered in sealed temple repositories."));
        l.add(a5);

        // 6. Achaemenid Silver Rhyton — Archaeological Objects / Pottery & Ceramics
        Artifact a6 = base("a6","Achaemenid Silver Rhyton",
            "https://images.unsplash.com/photo-1750353625645-ffe359ace94b?w=600&q=80",
            "Ceremonial drinking vessel in the form of a griffin head, crafted in silver with gilt detailing. Used during royal Persian banquets. Inscribed with the name of King Darius.",
            "Archaeological Objects","Pottery & Ceramics",true,true,"col4",
            "Iran","Persepolis, Pars Province","490 BCE","~2514 years","p3","5th Century BCE","c6",
            "2023-04-15T10:00:00Z","2024-03-20T16:00:00Z");
        a6.mainCategoryAttributes.add(new Artifact.AttributeDoc("Excavation Site","Persepolis Treasury"));
        a6.mainCategoryAttributes.add(new Artifact.AttributeDoc("Excavation Date","1933"));
        a6.mainCategoryAttributes.add(new Artifact.AttributeDoc("Layer / Stratum","Achaemenid destruction layer"));
        a6.mainCategoryAttributes.add(new Artifact.AttributeDoc("Dating Method","Inscription / Historical context"));
        a6.subCategoryAttributes.add(new Artifact.AttributeDoc("Clay Type","N/A – Silver vessel"));
        a6.subCategoryAttributes.add(new Artifact.AttributeDoc("Firing Technique","N/A – Metalwork (silver casting)"));
        a6.subCategoryAttributes.add(new Artifact.AttributeDoc("Glaze Type","Gold gilding on silver"));
        a6.subCategoryAttributes.add(new Artifact.AttributeDoc("Decoration Pattern","Griffin head, palmette borders, cuneiform inscription"));
        a6.connectedPersons.add(cp("per6","Owner / Dedicant","Inscribed with the name of Darius the Great."));
        a6.connectedPersons.add(cp("per4","Historical Conqueror","Alexander captured Persepolis and its treasury."));
        l.add(a6);

        // 7. Mesopotamian Cylinder Seal — Documents & Archives / Manuscript
        Artifact a7 = base("a7","Mesopotamian Cylinder Seal",
            "https://images.unsplash.com/photo-1596539363080-b678f4c02d6e?w=600&q=80",
            "Lapis lazuli cylinder seal from the Akkadian period. Hero wrestling a lion with cuneiform inscription identifying the owner as a royal scribe. Produces a repeating narrative when rolled in clay.",
            "Documents & Archives","Manuscript",true,true,"col4",
            "Iraq","Ur, Southern Mesopotamia","2250 BCE","~4274 years","p1","23rd Century BCE","c5",
            "2023-05-01T10:00:00Z","2023-09-15T12:00:00Z");
        a7.mainCategoryAttributes.add(new Artifact.AttributeDoc("Language","Akkadian / Sumerian"));
        a7.mainCategoryAttributes.add(new Artifact.AttributeDoc("Script","Cuneiform"));
        a7.mainCategoryAttributes.add(new Artifact.AttributeDoc("Author","Unknown royal scribe"));
        a7.mainCategoryAttributes.add(new Artifact.AttributeDoc("Publication Date","c. 2250 BCE"));
        a7.subCategoryAttributes.add(new Artifact.AttributeDoc("Writing Material","Lapis Lazuli stone"));
        a7.subCategoryAttributes.add(new Artifact.AttributeDoc("Ink Type","N/A – Incised relief"));
        a7.subCategoryAttributes.add(new Artifact.AttributeDoc("Number of Pages","N/A – Cylindrical seal (3.2 × 1.8 cm)"));
        a7.subCategoryAttributes.add(new Artifact.AttributeDoc("Condition","Excellent – clear impressions"));
        a7.connectedArtifacts.add(ca("a6","Regional Neighbor","Both from the ancient Near East cultural sphere."));
        l.add(a7);

        // 8. La Tène Gold Torque — Archaeological Objects / Jewelry
        Artifact a8 = base("a8","La Tène Gold Torque",
            "https://images.unsplash.com/photo-1607222836945-1e4c0075ca26?w=600&q=80",
            "Twisted gold neck ring of the La Tène culture, decorated with geometric patterns and stylized animal heads at each terminal. Symbol of high Celtic social status, possibly worn by a chieftain.",
            "Archaeological Objects","Jewelry",true,true,"col1",
            "France","Seine Valley, Northern Gaul","300 BCE","~2324 years","p6","4th Century BCE","c7",
            "2023-05-15T10:00:00Z","2024-02-28T10:00:00Z");
        a8.mainCategoryAttributes.add(new Artifact.AttributeDoc("Excavation Site","Votive hoard, Seine Valley"));
        a8.mainCategoryAttributes.add(new Artifact.AttributeDoc("Excavation Date","1954"));
        a8.mainCategoryAttributes.add(new Artifact.AttributeDoc("Layer / Stratum","Iron Age deposit"));
        a8.mainCategoryAttributes.add(new Artifact.AttributeDoc("Dating Method","Typological / Radiocarbon"));
        a8.subCategoryAttributes.add(new Artifact.AttributeDoc("Gemstones","None – plain gold"));
        a8.subCategoryAttributes.add(new Artifact.AttributeDoc("Craft Technique","Twisted wire, terminal casting"));
        a8.subCategoryAttributes.add(new Artifact.AttributeDoc("Material","Gold (18 carat)"));
        a8.subCategoryAttributes.add(new Artifact.AttributeDoc("Dimensions","17 cm diameter, 265 g"));
        l.add(a8);

        return l;
    }

    private static Artifact base(String id,String name,String photo,String desc,
            String mainCat,String subCat,boolean inMuseum,boolean onDisplay,String colId,
            String country,String place,String dateOrigin,String age,String periodId,
            String century,String civId,String created,String updated) {
        Artifact a=new Artifact(); a.id=id; a.name=name; a.photo=photo; a.description=desc;
        a.mainCategory=mainCat; a.subCategory=subCat; a.inMuseum=inMuseum; a.onDisplay=onDisplay; a.collectionId=colId;
        a.origin.country=country; a.origin.place=place; a.origin.dateOrigin=dateOrigin; a.origin.age=age;
        a.origin.periodId=periodId; a.origin.century=century; a.origin.civilizationId=civId;
        a.createdAt=created; a.updatedAt=updated; return a;
    }
    private static Artifact.ConnectedPerson cp(String pid,String rel,String desc){Artifact.ConnectedPerson c=new Artifact.ConnectedPerson();c.personId=pid;c.relationType=rel;c.description=desc;return c;}
    private static Artifact.ConnectedOrganization co(String oid,String rel,String desc){Artifact.ConnectedOrganization c=new Artifact.ConnectedOrganization();c.orgId=oid;c.relationType=rel;c.description=desc;return c;}
    private static Artifact.ConnectedArtifact ca(String aid,String rel,String desc){Artifact.ConnectedArtifact c=new Artifact.ConnectedArtifact();c.artifactId=aid;c.relationType=rel;c.description=desc;return c;}

    public static List<Log> logs() {
        List<Log> l = new ArrayList<>();
        l.add(log("l1","Artifact","a1","Golden Death Mask of Amenhotep","curator1","Created","2023-02-01T10:00:00Z"));
        l.add(log("l2","Artifact","a2","Parthenon Frieze Fragment","curator1","Created","2023-02-15T10:00:00Z"));
        l.add(log("l3","Artifact","a3","Roman Legion Gladius","curator1","Created","2023-03-01T10:00:00Z"));
        l.add(log("l4","Civilization","c5","Mesopotamian","admin","Created","2023-01-07T10:00:00Z"));
        l.add(log("l5","User","u3","Dr. Lena Vasquez","admin","Created","2023-03-05T09:00:00Z"));
        l.add(log("l6","Organization","o4","Macedonian Army","curator1","Created","2023-01-08T10:00:00Z"));
        l.add(log("l7","Collection","col4","Near East Collection","admin","Created","2023-01-10T10:00:00Z"));
        Log l8 = log("l8","Artifact","a1","Golden Death Mask of Amenhotep","curator2","Updated","2024-06-15T14:30:00Z");
        l8.changes.add(new Log.LogChange("onDisplay","false","true"));
        l8.changes.add(new Log.LogChange("mainCategory","Jewelry","Archaeological Objects"));
        l.add(l8);
        Log l9 = log("l9","Period","p5","Byzantine Empire","admin","Updated","2023-08-10T09:00:00Z");
        l9.changes.add(new Log.LogChange("endYear","1400","1453"));
        l.add(l9);
        l.add(log("l10","Artifact","a6","Achaemenid Silver Rhyton","curator2","Created","2023-04-15T10:00:00Z"));
        l.add(log("l11","Artifact","a7","Mesopotamian Cylinder Seal","curator2","Created","2023-05-01T10:00:00Z"));
        return l;
    }
    private static Log log(String id,String itemType,String itemId,String itemName,String by,String type,String at){
        Log l=new Log();l.id=id;l.itemType=itemType;l.itemId=itemId;l.itemName=itemName;l.changedBy=by;l.changeType=type;l.changedAt=at;return l;
    }
}
