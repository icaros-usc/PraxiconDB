/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import static gr.csri.poeticon.praxicon.db.entities.Concept.Status.VARIABLE;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static java.util.Objects.isNull;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dmavroeidis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "relationSet", namespace =
        "http://www.csri.gr/relation_set")
@Entity
@NamedQueries({
    @NamedQuery(name =
            "findRelationSetByName",
            query =
            "SELECT rs FROM RelationSet rs " +
            "WHERE rs.name = :relationSetName"),
    @NamedQuery(name =
            "findRelationSet",
            query =
            "SELECT rs FROM RelationSet rs " +
            "WHERE rs = :relationSet"),
    @NamedQuery(name =
            "findRelationSetsByRelationArgument",
            query =
            "SELECT DISTINCT rs FROM RelationSet rs " +
            "JOIN rs.relations rsr " +
            "JOIN rsr.relation r " +
            "WHERE (r.rightArgument = :relationArgument " +
            "OR r.leftArgument = :relationArgument)"),
    @NamedQuery(name =
            "findRelationSetsByLeftRelationArgument",
            query =
            "SELECT DISTINCT rs FROM RelationSet rs " +
            "JOIN rs.relations rsr " +
            "JOIN rsr.relation r " +
            "WHERE r.leftArgument = :relationArgument"),
    @NamedQuery(name =
            "findRelationSetsByRightRelationArgument",
            query =
            "SELECT DISTINCT rs FROM RelationSet rs " +
            "JOIN rs.relations rsr " +
            "JOIN rsr.relation r " +
            "WHERE r.rightArgument = :relationArgument"),
    @NamedQuery(name =
            "findRelationSetsByRelation",
            query =
            "SELECT rs FROM RelationSet rs " +
            "JOIN rs.relations rsr " +
            "WHERE rsr.relation = :relation"),})
@Table(name = "RelationSets",
        indexes = {
            @Index(columnList = "Name")})
public class RelationSet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @XmlTransient
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RelationSetId")
    private Long id;

    @Column(name = "Name")
    private String name;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "relationSet")
    private List<RelationSet_Relation> relations;

    @XmlElement(name = "languageRepresentation")
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "LanguageRepresentation_RelationSet",
            joinColumns = {
                @JoinColumn(name = "RelationSetId")},
            inverseJoinColumns = {
                @JoinColumn(name = "LanguageRepresentationId")}
    )
    private List<LanguageRepresentation> languageRepresentations;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "relationSet")
    private List<VisualRepresentation> visualRepresentations;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "relationSet")
    private List<MotoricRepresentation> motoricRepresentations;

    /**
     * Constructor #1.
     */
    public RelationSet() {
        this.name = "";
        this.relations = new ArrayList<>();
        this.languageRepresentations = new ArrayList<>();
        this.visualRepresentations = new ArrayList<>();
        this.motoricRepresentations = new ArrayList<>();
    }

    /**
     * Constructor #2.
     *
     * @param name
     * @param relationSetRelationsList
     * @param languageRepresentations
     * @param visualRepresentations
     * @param motoricRepresentations
     */
    public RelationSet(String name,
            List<RelationSet_Relation> relationSetRelationsList,
            List<LanguageRepresentation> languageRepresentations,
            List<VisualRepresentation> visualRepresentations,
            List<MotoricRepresentation> motoricRepresentations) {
        this.name = name;
        this.relations = relationSetRelationsList;
        this.languageRepresentations = languageRepresentations;
        this.visualRepresentations = visualRepresentations;
        this.motoricRepresentations = motoricRepresentations;
    }

    public RelationSet(RelationSet newRelationSet) {
        this.name = newRelationSet.getName();
        this.relations = newRelationSet.getRelations();
        this.languageRepresentations = newRelationSet.
                getLanguageRepresentations();
        this.visualRepresentations = newRelationSet.getVisualRepresentations();
        this.motoricRepresentations = newRelationSet.
                getMotoricRepresentations();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets a Set of all Relations contained in this RelationSet.
     *
     * @return a Set of relations
     */
    public List<Relation> getRelationsList() {
        List<RelationSet_Relation> relationSetRelationList = this.relations;
        List<Relation> relationList = new ArrayList();
        if (!relationSetRelationList.isEmpty()) {
            for (RelationSet_Relation relationSetRelation
                    : relationSetRelationList) {
                relationList.add(relationSetRelation.getRelation());
            }
        }
        return relationList;
    }

    /**
     * Gets all Relations contained in this RelationSet in the form of Set,
     * which means that there are no duplicate entries.
     *
     * @return a set of relations
     */
    public List<Relation> getRelationsSet() {
        ArrayList<Relation> relationSet = new ArrayList<>(this.
                getRelationsList());
        return relationSet;
    }

    /**
     * Gets a Set of all relations contained in this relation set.
     *
     * @return a List of Relation
     */
    public List<RelationSet_Relation> getRelations() {
        return relations;
    }

    /**
     * Sets the relations for this relation set.
     *
     * @param relations
     */
    public void setRelations(List<RelationSet_Relation> relations) {
        this.relations = relations;
    }

    /**
     * Adds a relation to the relation set without information on the order.
     *
     * @param relation
     */
    public void addRelation(Relation relation) {
        RelationSet_Relation relationSetRelation = new RelationSet_Relation();
        relationSetRelation.setRelation(relation);
        relationSetRelation.setRelationSet(this);
        this.relations.add(relationSetRelation);
    }

    /**
     * Retrieves the Set of language representations for this relation set.
     *
     * @return a Set of LanguageRepresentation
     */
    public List<LanguageRepresentation> getLanguageRepresentations() {
        return languageRepresentations;
    }

    /**
     * Retrieves the names of the language representations of this relation set.
     *
     * @return a Set strings containing the names of language representation
     */
    public List<String> getLanguageRepresentationsNames() {
        List<String> languageRepresentationNames = new ArrayList<>();
        for (LanguageRepresentation languageRepresentation
                : languageRepresentations) {
            languageRepresentationNames.add(
                    languageRepresentation.getText());
        }
        return languageRepresentationNames;
    }

    /**
     * Sets the language representation for this relation set.
     *
     * @param languageRepresentations
     */
    public void setLanguageRepresentations(
            List<LanguageRepresentation> languageRepresentations) {
        this.languageRepresentations = languageRepresentations;
    }

    /**
     * Adds a language representation to the relation set.
     *
     * @param languageRepresentation
     */
    public void addLanguageRepresentation(
            LanguageRepresentation languageRepresentation) {
        this.languageRepresentations.add(languageRepresentation);
    }

    public List<VisualRepresentation> getVisualRepresentations() {
        return visualRepresentations;
    }

    public void addVisualRepresentation(
            VisualRepresentation visualRepresentation) {
        this.visualRepresentations.add(visualRepresentation);
    }

    public void setVisualRepresentation(
            List<VisualRepresentation> visualRepresentations) {
        this.visualRepresentations = visualRepresentations;
    }

    public final List<VisualRepresentation> getVisualRepresentationsEntries() {
        List<VisualRepresentation> visualRepresentationEntries =
                new ArrayList<>();
        for (VisualRepresentation VisualRepresentation
                : this.visualRepresentations) {
            visualRepresentationEntries.add(VisualRepresentation);
        }
        return visualRepresentationEntries;
    }

    public final List<MotoricRepresentation> getMotoricRepresentations() {
        return motoricRepresentations;
    }

    public List<MotoricRepresentation> getMotoricRepresentationsEntries() {
        List<MotoricRepresentation> motoricRepresentationEntries =
                new ArrayList<>();
        for (MotoricRepresentation MotoricRepresentation
                : this.motoricRepresentations) {
            motoricRepresentationEntries.add(MotoricRepresentation);
        }
        return motoricRepresentationEntries;
    }

    public void setMotoricRepresentations(
            List<MotoricRepresentation> motoricRepresentations) {
        this.motoricRepresentations = motoricRepresentations;
    }

    public void addMotoricRepresentation(
            MotoricRepresentation motoricRepresentation) {
        this.motoricRepresentations.add(motoricRepresentation);
    }

    /**
     * If any of the concepts inside the relation set has status variable,
     * then the relation set itself is characterized as variable. It searches
     * recursively.
     *
     * @return true/false
     */
    public boolean isVariable() {
        for (Relation relation : this.getRelationsList()) {
            if (relation.getLeftArgument().isConcept()) {
                if (relation.getLeftArgument().getConcept().getStatus() ==
                        VARIABLE) {
                    return true;
                }
            } else if (relation.getLeftArgument().isRelationSet()) {
                if (relation.getLeftArgument().getRelationSet().isVariable()) {
                    return true;
                }
            }

            if (relation.getRightArgument().isConcept()) {
                if (relation.getRightArgument().getConcept().getStatus() ==
                        VARIABLE) {
                    return true;
                }
            } else if (relation.getRightArgument().isRelationSet()) {
                if (relation.getRightArgument().getRelationSet().isVariable()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.name);
        hash = 13 * hash + Objects.hashCode(this.relations);
        hash = 13 * hash + Objects.hashCode(this.languageRepresentations);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RelationSet other = (RelationSet)obj;
        if (!isNull(this.name) && !isNull(other.getName())) {
            if (!this.name.equals(other.getName())) {
                return false;
            }
        }
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(this.getRelationsList(), other.getRelationsList());
        eb.append(this.languageRepresentations, other.
                getLanguageRepresentations());
        return eb.isEquals();
    }

    @Override
    public String toString() {
        List<Relation> relationsList = this.getRelationsList();
        List<String> relationsStringsList = new ArrayList<>();
        String relationsString;
        for (Relation relation : relationsList) {
            relationsStringsList.add(relation.toString());
        }
        relationsString = "{" + StringUtils.join(relationsStringsList, "##") +
                "}";

        return relationsString;
    }
//
//    @XmlRegistry
//    class ObjectFactory {
//
//        RelationSet createRelationSet() {
//            return new RelationSet();
//        }
//    }
}
