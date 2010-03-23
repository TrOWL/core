/*
 * Queries.java
 *
 * Created on 08 May 2006, 17:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package eu.trowl.db;

/**
 *
 * @author ethomas
 */
public class Queries {

    /** Creates a new instance of Queries */
    public Queries() {
    }
    /**
     *
     */
    public static final String SELECT_NAMESPACES_WHERE_URI = "select * from namespaces where uri=?";
    /**
     *
     */
    public static final String INSERT_URI_INTO_NAMESPACES = "insert into namespaces (uri) values (?)";
    /**
     *
     */
    public static final String LAST_UID = "select lastval()";
    /**
     *
     */
    public static final String SELECT_CLASSES_WHERE_NAMESPACE_AND_ID = "select * from classes where namespace=? and id=?";
    /**
     *
     */
    public static final String SELECT_PROPERTIES_WHERE_NAMESPACE_AND_ID = "select * from properties where namespace=? and id=?";
    /**
     *
     */
    public static final String INSERT_ID_NS_INTO_CLASSES = "insert into classes (id, namespace) values (?,?)";
    /**
     *
     */
    public static final String INSERT_ID_NS_INTO_PROPERTIES = "insert into properties (id, namespace) values (?,?)";
    /**
     *
     */
    public static final String INSERT_SUPER_SUB_INTO_SUBCLASS = "insert into sub_class (super, sub) values (?,?)";
    /**
     *
     */
    public static final String UPDATE_CLASS_LABEL_COMMENT = "update classes set label=?, comment=? where uid=?";
    /**
     *
     */
    public static final String UPDATE_PROPERTIES_DOMAIN_RANGE = "update properties set domain=?, range=? where uid=?";
    /**
     *
     */
    public static final String GET_PATH_FOR_CLASS_URI = "SELECT path FROM classpaths, classes WHERE classpaths.class_id=classes.id AND classes.uri=?";
    /**
     *
     */
    public static final String GET_PATH_FOR_PROPERTY_URI = "SELECT path, type FROM propertypaths, properties WHERE propertypaths.property_id=properties.id AND properties.uri=?";
    /**
     *
     */
    public static final String INSERT_ONTOLOGY = "INSERT INTO ontologies (id,uri) VALUES (?, ?)";
    /**
     *
     */
    public static final String INSERT_INDIVIDUAL = "INSERT INTO individuals (id, class_id, uri, ontology) VALUES (?, ?, ?, ?)";
    /**
     *
     */
    public static final String INSERT_CLASS = "INSERT INTO classes (id, uri, ontology) VALUES (?, ?, ?)";
    /**
     *
     */
    public static final String INSERT_PROPERTY = "INSERT INTO properties (id, uri, ontology) VALUES (?, ?, ?)";
    /**
     *
     */
    public static final String INSERT_OPROPERTY_INSTANCE = "INSERT INTO oproperty_instances (subject_id, property_id, object_id, ontology) VALUES (?, ?, ?, ?)";
    /**
     *
     */
    public static final String INSERT_DPROPERTY_INSTANCE = "INSERT INTO dproperty_instances (subject_id, property_id, object, language, ontology) VALUES (?, ?, ?, ?, ?)";

    /**
     *
     */
    public static final String INSERT_PROPERTY_PATH = "INSERT INTO propertypaths (property_id, path, type, ontology) VALUES (?, ?, 'b', ?)";
    /**
     *
     */
    public static final String INSERT_CLASS_PATH = "INSERT INTO classpaths (class_id, path, ontology) VALUES (?, ?, ?)";
    /**
     *
     */
    public static final String TABLE_EXISTS = "SELECT 'hello' FROM ? WHERE 0=1";
    /**
     *
     */
    public static final String[] TABLES = {"ontologies", "classes", "properties", "individuals", "oproperty_instances", "dproperty_instances", "classpaths", "classpaths_pop", "propertypaths"};
    /**
     *
     */
    public static final String[] CREATE_TABLES = {"CREATE TABLE ontologies (id bigint not null, uri varchar(2048))",
        "CREATE TABLE classes (id bigint not null, uri varchar(2048) not null, ontology bigint not null)",
        "CREATE TABLE properties (id bigint not null, uri varchar(2048) not null, ontology bigint not null)",
        "CREATE TABLE individuals (id bigint not null, class_id bigint not null, uri varchar(2048) not null, ontology bigint not null)",
        "CREATE TABLE oproperty_instances (subject_id bigint not null, property_id bigint not null, object_id bigint not null, ontology bigint not null)",
        "CREATE TABLE dproperty_instances (subject_id bigint not null, property_id bigint not null, object varchar(262144) not null, language varchar(64), datatype bigint, ontology bigint not null)",
        "CREATE TABLE classpaths (class_id bigint not null, path varchar(4096), ontology bigint)",
        "CREATE TABLE classpaths_pop (class_id bigint not null, path varchar(4096), ontology bigint)",
        "CREATE TABLE propertypaths (property_id bigint not null, path varchar(262144), type char(1) not null, ontology bigint)"};
    /**
     *
     */
    public static final String[] DROP_TABLES = {};
    /**
     *
     */
    public static final String[] CREATE_PRIMARY_KEYS = {};
    /**
     *
     */
    public static final String[] DROP_PRIMARY_KEYS = {};
    /**
     *
     */
    public static final String[] CREATE_INDEXES = {
        "CREATE INDEX classpaths_pop_class_idx ON classpaths_pop(class_id)",
        "CREATE INDEX classpaths_pop_path_idx ON classpaths_pop(path)",
        //"CREATE INDEX propertypaths_pop_class_idx ON propertypaths_pop(property_id)",
        //"CREATE INDEX propertypaths_pop_path_idx ON propertypaths_pop(path)",

        "CREATE INDEX classpaths_class_idx ON classpaths(class_id)",
        "CREATE INDEX classpaths_path_idx ON classpaths(path)",
        "CREATE INDEX propertypaths_property_idx ON propertypaths(property_id)",
        "CREATE INDEX propertypaths_path_idx ON propertypaths(path)",

        "CREATE INDEX individuals_class_idx ON individuals(class_id)",
        "CREATE INDEX individuals_uri ON individuals(uri)",

        "CREATE INDEX classes_id_idx ON classes(id)",
        "CREATE INDEX classes_uri_idx ON classes(uri)",
        "CREATE INDEX propertes_id_idx ON properties(id)",
        "CREATE INDEX properties_uri_idx ON properties(uri)",
        
        "CREATE INDEX dproperty_instances_subject_idx ON dproperty_instances(subject_id)",
        "CREATE INDEX dproperty_instances_predicate_idx ON dproperty_instances(property_id)",
        "CREATE INDEX dproperty_instances_object_idx ON dproperty_instances(object)",
        "CREATE INDEX dproperty_instances_language_idx ON dproperty_instances(language)",
        "CREATE INDEX dproperty_instances_subjectpredicate_idx ON dproperty_instances(subject_id, property_id)",

        "CREATE INDEX oproperty_instances_subject_idx ON oproperty_instances(subject_id)",
        "CREATE INDEX oproperty_instances_predicate_idx ON oproperty_instances(property_id)",
        "CREATE INDEX oproperty_instances_object_idx ON oproperty_instances(object_id)",
        "CREATE INDEX oproperty_instances_subjectpredicate_idx ON oproperty_instances(subject_id, property_id)"
        };

    /**
     *
     */
    public static final String CLASSPATHS_POP = "INSERT INTO classpaths_pop SELECT * FROM classpaths WHERE classpaths.class_id in (select class_id from individuals)";
//    public static final String PROPERTYPATHS_POP = "INSERT INTO classpaths_pop SELECT * FROM classpaths WHERE classpaths.class_id in (select class_id from individuals)";

    /**
     *
     */
    public static final String[] DROP_INDEXES = {
        "DROP INDEX IF EXISTS classpaths_pop_class_idx",
        "DROP INDEX IF EXISTS classpaths_pop_path_idx",
        "DROP INDEX IF EXISTS classpaths_class_idx",
        "DROP INDEX IF EXISTS classpaths_path_idx",
        "DROP INDEX IF EXISTS propertypaths_property_idx",
        "DROP INDEX IF EXISTS propertypaths_path_idx",
        "DROP INDEX IF EXISTS propertypaths_pop_property_idx",
        "DROP INDEX IF EXISTS propertypaths_pop_path_idx",

        "DROP INDEX IF EXISTS individuals_class_idx",
        "DROP INDEX IF EXISTS individuals_uri",

        "DROP INDEX IF EXISTS classes_id_idx",
        "DROP INDEX IF EXISTS classes_uri_idx",
        "DROP INDEX IF EXISTS propertes_id_idx",
        "DROP INDEX IF EXISTS properties_uri_idx",

        "DROP INDEX IF EXISTS oproperty_instances_subject_idx",
        "DROP INDEX IF EXISTS oproperty_instances_predicate_idx",
        "DROP INDEX IF EXISTS oproperty_instances_object_idx",
        "DROP INDEX IF EXISTS oproperty_instances_subjectpredicate_idx",
        "DROP INDEX IF EXISTS dproperty_instances_subject_idx",
        "DROP INDEX IF EXISTS dproperty_instances_predicate_idx",
        "DROP INDEX IF EXISTS dproperty_instances_object_idx",
        "DROP INDEX IF EXISTS dproperty_instances_subjectpredicate_idx",
        "DROP INDEX IF EXISTS dproperty_instances_language_idx",
    };
}
