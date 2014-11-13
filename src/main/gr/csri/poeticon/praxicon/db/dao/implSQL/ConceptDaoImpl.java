/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concept.Status;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class ConceptDaoImpl extends JpaDao<Long, Concept> implements
        ConceptDao {

    private String String;

    /**
     * Finds all the concepts
     *
     * @return a list of all concepts in the database
     */
    @Override
    public List<Concept> findAllConcepts() {
        Query query = getEntityManager().createNamedQuery("findAllConcepts");
        List<Concept> concepts = query.getResultList();
        return concepts;
    }

    /**
     * Finds all concepts that have a specific conceptId
     *
     * @param conceptId the concept id to search for
     * @return a list of concepts found in the database
     */
    @Override
    public Concept findConceptByConceptId(long conceptId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByConceptId").
                setParameter("conceptId", conceptId);
        return (Concept)query.getSingleResult();
    }

    /**
     * Finds all concepts that have a name or language representation containing
     * a given string
     *
     * @param conceptExternalSourceId the external source id of the concept
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findConceptsByExternalSourceId(
            String conceptExternalSourceId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByExternalSourceId").
                setParameter("conceptExternalSourceId", "%" +
                        conceptExternalSourceId + "%");
        return query.getResultList();
    }

    /**
     * Finds all concepts that have a name equal to a given string
     *
     * @param conceptExternalSourceId the concept name to search for
     * @return a list of concepts found in the database
     */
    @Override
    public Concept findConceptByExternalSourceIdExact(
            String conceptExternalSourceId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptByExternalSourceIdExact").
                setParameter("conceptExternalSourceId", conceptExternalSourceId);
        return (Concept)query.getSingleResult();
    }

    /**
     * Finds all concepts that have a language representation containing a given
     * string
     *
     * @param languageRepresentationName the language representation name to
     *                                   search for
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findConceptsByLanguageRepresentation(
            String languageRepresentationName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentation").
                setParameter("languageRepresentationName", "%" +
                        languageRepresentationName + "%");
        return query.getResultList();
    }

    /**
     * Finds all concepts that have a language representation equal to a given
     * string
     *
     * @param queryString the string to search for
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findConceptsByLanguageRepresentationExact(
            String languageRepresentationName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentationExact").
                setParameter("languageRepresentationName",
                        languageRepresentationName);
        return query.getResultList();
    }

    /**
     * Finds all concepts that have a Status equal to a given Status
     *
     * @param status the concept Status to search for
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findConceptsByStatus(Status status) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByStatusExact").
                setParameter("status", status);
        return query.getResultList();
    }

    /**
     * Finds all concepts that have a name or id equal to a given string
     *
     * @param v
     * @return the concept found in the database (null if not found)
     */
    @Override
    public Concept getConceptWithExternalSourceIdOrID(String v) {
        Query q;
        long id = -1;
        try {
            id = Long.parseLong(v);
        } catch (NumberFormatException e) {
            //it is the name of the concept
        }
        if (id == -1) {
            return findConceptByExternalSourceIdExact(v.trim());
        } else {
            return findConceptByConceptId(id);
        }
    }

    /**
     * Updates a concept from the database that has the same name as another
     * concept that is used as source of the update
     *
     * @param newConcept concept to use as source
     * @return the updated concept
     */
    @Override
    public Concept updatedConcept(Concept newConcept) {

        Concept oldConcept = new Concept();
        try {
            oldConcept = this.findConceptByExternalSourceIdExact(newConcept.
                    getExternalSourceId());
        } catch (Exception e) {

            return newConcept;
        } finally {
            oldConcept.setConceptType(newConcept.getConceptType());
            oldConcept.setStatus(newConcept.getStatus());
            oldConcept.setSpecificityLevel(newConcept.getSpecificityLevel());
            oldConcept.setComment(newConcept.getComment());

            updateLanguageRepresentations(newConcept, oldConcept);
            updateVisualRepresentations(newConcept, oldConcept);
            updateMotoricRepresentations(newConcept, oldConcept);

// These are not needed any more since the relation argument has replace concept 
// as the rightArgument of a relation
//            updateObjOfRelations(newConcept, oldConcept);
//            updateRelations(newConcept, oldConcept);
            return oldConcept;
        }
    }

    /**
     * Updates a concept from the database (in place) that has the same name as
     * another concept that is used as source of the update
     *
     * @param newConcept concept to use as source
     */
    @Override
    public void update(Concept newConcept) {
        try {
            Query query = getEntityManager().createNamedQuery(
                    "findConceptByExternalSourceIdExact").
                    setParameter("conceptExternalSourceId", newConcept.
                            getExternalSourceId());
            Concept tmpConcept = (Concept)query.getSingleResult();

            Concept oldConcept = null;

            if (tmpConcept.equals(null)) {
                oldConcept = new Concept();
            } else {
                oldConcept = tmpConcept;
            }
            if (oldConcept.getConceptType() == null ||
                    oldConcept.getConceptType() == Concept.type.UNKNOWN) {
                oldConcept.setConceptType(newConcept.getConceptType());
            }
            if (oldConcept.getStatus() == null) {
                oldConcept.setStatus(newConcept.getStatus());
            }

            oldConcept.setSpecificityLevel(newConcept.getSpecificityLevel());
            if (oldConcept.getComment() == null ||
                    oldConcept.getComment().equalsIgnoreCase("") ||
                    oldConcept.getComment().equalsIgnoreCase("Unknown")) {
                oldConcept.setComment(newConcept.getComment());
            }
            if (newConcept.getSource() != null && !newConcept.getSource().
                    isEmpty()) {
                oldConcept.setSource(newConcept.getSource());
            }

            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateLanguageRepresentations(newConcept, oldConcept);
            oldConcept = entityManager.merge(oldConcept);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateVisualRepresentations(newConcept, oldConcept);
            oldConcept = entityManager.merge(oldConcept);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateMotoricRepresentations(newConcept, oldConcept);
            oldConcept = entityManager.merge(oldConcept);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
// These are not needed any more since the relation argument has replace concept 
// as the rightArgument of a relation
//            updateObjOfRelations(newConcept, oldConcept);
            oldConcept = entityManager.merge(oldConcept);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
// These are not needed any more since the relation argument has replace concept 
// as the object of a relation
//            updateRelations(newConcept, oldConcept);
            oldConcept = entityManager.merge(oldConcept);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

    }

    /**
     * Updates a concept using another concept (in place).
     *
     * @param oldConcept concept to be updated
     * @param newConcept concept to use as source
     */
    @Override
    public void update(Concept oldConcept, Concept newConcept) {
        try {
            //     entityManager.getTransaction().begin();
            if (oldConcept.getConceptType() == null ||
                    oldConcept.getConceptType() == Concept.type.UNKNOWN) {
                oldConcept.setConceptType(newConcept.getConceptType());
            }
            if (oldConcept.getStatus() == null) {
                oldConcept.setStatus(newConcept.getStatus());
            }
            oldConcept.setSpecificityLevel(newConcept.getSpecificityLevel());
            if (oldConcept.getComment() == null ||
                    oldConcept.getComment().equalsIgnoreCase("") ||
                    oldConcept.getComment().equalsIgnoreCase("Unknown")) {
                oldConcept.setComment(newConcept.getComment());
            }
            updateVisualRepresentations(newConcept, oldConcept);
            updateMotoricRepresentations(newConcept, oldConcept);
            // These are not needed any more since the relation argument has replace concept 
// as the object of a relation
//            updateObjOfRelations(newConcept, oldConcept);
//            updateRelations(newConcept, oldConcept);

            merge(oldConcept);
            updateLanguageRepresentations(newConcept, oldConcept);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Finds all concepts that are children (type-token related) of a given
     * concept
     *
     * @param concept the concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getChildrenOfConcept(Concept concept) {
        List<Concept> conceptList = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.getAllRelationsOfConcept(concept);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.RelationNameForward.TYPE_TOKEN &&
                    relation.getLeftArgument().getConcept().equals(concept)) {
                if (relation.getRightArgument().isConcept()) {
                    conceptList.add(relation.getRightArgument().getConcept());
                } else {
                    System.err.println("A relation set cannot have children");
                }
            }
        }
        entityManager.clear();
        return conceptList;
    }

    /**
     * Finds all concepts that are parents (token-type related) of a given
     * concept
     *
     * @param concept the concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getParentsOfConcept(Concept concept) {
        List<Concept> res = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.getAllRelationsOfConcept(concept);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.RelationNameForward.TYPE_TOKEN) {
                if (relation.getLeftArgument().isConcept() &&
                        relation.getRightArgument().getConcept().equals(concept)) {
                    res.add(relation.getLeftArgument().getConcept());
                }
            }
        }
        entityManager.clear();
        return res;
    }

    /**
     * Finds all concepts that are ancestors (higher in hierarchy) of a given
     * concept
     *
     * @param concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getAllAncestors(Concept concept) {
        List<Concept> res = new ArrayList<>();

        List<Concept> parents = getParentsOfConcept(concept);
        for (Concept parent : parents) {
            if (!res.contains(parent)) {
                res.add(parent);
            }
            List<Concept> tmp = getAllAncestors(parent);
            for (Concept tmpC : tmp) {
                if (!res.contains(tmpC)) {
                    res.add(tmpC);
                }
            }
        }
        return res;
    }

    /**
     * Finds all concepts that are offsprings (lower in hierarchy) of a given
     * concept
     *
     * @param concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getAllOffsprings(Concept concept) {
        List<Concept> res = new ArrayList<>();
        List<Concept> children = getChildrenOfConcept(concept);
        for (Concept child : children) {
            if (!res.contains(child)) {
                res.add(child);
            }
            List<Concept> tmp = getAllOffsprings(child);
            for (Concept tmpC : tmp) {
                if (!res.contains(tmpC)) {
                    res.add(tmpC);
                }
            }
        }
        return res;
    }

    /**
     * Finds all concepts that are classes of instance (has-instance related) of
     * a given concept
     *
     * @param concept the concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getClassesOfInstance(Concept concept) {
        List<Concept> res = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.getAllRelationsOfConcept(concept);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.RelationNameForward.HAS_INSTANCE &&
                    relation.getRightArgument().equals(concept)) {
                if (relation.getRightArgument().isConcept()) {
                    res.add(relation.getLeftArgument().getConcept());
                }
            }
        }
        return res;
    }

    /**
     * Finds all concepts that are instances (instance-of related) of a given
     * concept
     *
     * @param concept the concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getInstancesOf(Concept concept) {
        List<Concept> res = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.getAllRelationsOfConcept(concept);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.RelationNameForward.HAS_INSTANCE &&
                    relation.getRightArgument().equals(concept)) {
                if (relation.getRightArgument().isConcept()) {
                    res.add(relation.getLeftArgument().getConcept());
                }
            }
        }

        return res;
    }

    /**
     * Finds all the Basic Level concepts for the given concept
     *
     * @param concept concept to be checked
     * @return The list of BL
     */
    @Override
    public List<Concept> getBasicLevel(Concept concept) {
// Temporarily disabled the block below until cleared
//        if (c.getOrigin() == Concept.Origin.MOVEMENT)
//        {
//            return getBasicLevelOfMovementOriginConcept(concept);
//        }
//      else
//        {

        // AN BL επιστρέφει λίστα με τον εαυτό του.
        // Αν είναι above BL getBLofanabstractlevel
        // ελσε ιφ below BL τρέξε BL entity concept
        if (concept.getConceptType() == Concept.type.ABSTRACT) {
            return getBasicLevelOfAnAbstractConcept(concept);
        } else {
            if (concept.getConceptType() == Concept.type.ENTITY ||
                    concept.getConceptType() == Concept.type.MOVEMENT ||
                    concept.getConceptType() == Concept.type.FEATURE) {
                return getBasicLevelOfAnEntityConcept(concept);
            }
        }
//        }

        return new ArrayList<>();
    }

    /**
     * Finds all the Basic Level concepts for the given non abstract concept.
     *
     * @param concept
     * @return The list of BL
     */
    @Override
    public List<Concept> getBasicLevelOfAnEntityConcept(Concept concept) {
        List<Concept> res = new ArrayList<>();
        if (concept.getSpecificityLevel() !=
                Concept.SpecificityLevel.BASIC_LEVEL &&
                concept.getConceptType() != Concept.type.ABSTRACT) {
            List<Concept> parents = getParentsOfConcept(concept);
            for (Concept parent : parents) {
                res.addAll(getBasicLevelOfAnEntityConcept(parent));
            }

            if (parents.isEmpty()) {
                List<Concept> classes = getClassesOfInstance(concept);
                for (Concept classe : classes) {
                    res.addAll(getBasicLevelOfAnEntityConcept(classe));
                }
            }
        } else {
            if (concept.getSpecificityLevel() ==
                    Concept.SpecificityLevel.BASIC_LEVEL) {
                res.add(concept);
            }
        }
        return res;
    }

    /**
     * Finds all the Basic Level concepts for the given abstract concept.
     *
     * @param concept concept to be checked
     * @return The list of BL
     */
    @Override
    public List<Concept> getBasicLevelOfAnAbstractConcept(Concept concept) {
        List<Concept> res = new ArrayList<>();

        if (concept.getSpecificityLevel() !=
                Concept.SpecificityLevel.BASIC_LEVEL &&
                concept.getConceptType() == Concept.type.ABSTRACT) {
            List<Concept> children = getChildrenOfConcept(concept);
            for (Concept children1 : children) {
                res.addAll(getBasicLevelOfAnAbstractConcept(children1));
            }
        } else {
            if (concept.getSpecificityLevel() ==
                    Concept.SpecificityLevel.BASIC_LEVEL) {
                res.add(concept);
            }
        }
        return res;
    }

    /**
     * Finds all the Basic Level concepts for the given movement origin concept.
     *
     * @param concept concept to be checked
     * @return The list of BL
     */
    // special getting BL for movement origin concepts 
    // lookin up and down regardless type
    private List<Concept> getBasicLevelOfMovementOriginConcept(Concept concept) {
        List<Concept> res = new ArrayList<>();

        if (concept.getSpecificityLevel() ==
                Concept.SpecificityLevel.BASIC_LEVEL) {
            res.add(concept);
        } else {
            res.addAll(getBasicLevelOfMovementOriginConceptGoingDown(concept));
            res.addAll(getBasicLevelOfMovementOriginConceptGoingUp(concept));
        }
        return res;
    }

    /**
     * Finds all the Basic Level concepts for the given concept, moving only up
     * in the hierarchy.
     *
     * @param concept concept to be checked
     * @return The list of BL
     */
    private List<Concept> getBasicLevelOfMovementOriginConceptGoingUp(
            Concept concept) {
        List<Concept> res = new ArrayList<>();

        if (concept.getSpecificityLevel() !=
                Concept.SpecificityLevel.BASIC_LEVEL) {
            List<Concept> parents = getParentsOfConcept(concept);
            for (Concept parent : parents) {
                res.addAll(getBasicLevelOfMovementOriginConceptGoingUp(parent));
            }

            if (parents.isEmpty()) {
                List<Concept> classes = getClassesOfInstance(concept);
                for (Concept classe : classes) {
                    res.addAll(getBasicLevelOfMovementOriginConceptGoingUp(
                            classe));
                }
            }
        } else {
            if (concept.getSpecificityLevel() ==
                    Concept.SpecificityLevel.BASIC_LEVEL) {
                res.add(concept);
            }
        }
        return res;
    }

    /**
     * Finds all the Basic Level concepts for the given concept, moving only
     * down in the hierarchy.
     *
     * @param concept concept to be checked
     * @return The list of BL
     */
    private List<Concept> getBasicLevelOfMovementOriginConceptGoingDown(
            Concept concept) {
        List<Concept> res = new ArrayList<>();

        if (concept.getSpecificityLevel() !=
                Concept.SpecificityLevel.BASIC_LEVEL) {
            List<Concept> children = getChildrenOfConcept(concept);
            for (Concept children1 : children) {
                res.addAll(getBasicLevelOfMovementOriginConceptGoingDown(
                        children1));
            }
        } else {
            if (concept.getSpecificityLevel() ==
                    Concept.SpecificityLevel.BASIC_LEVEL) {
                res.add(concept);
            }
        }
        return res;
    }

    /**
     * Finds all concepts that are related to a given concept using a given
     * relation type
     *
     * @param concept      the concept
     * @param relationType the type of relation (direction sensitive)
     * @return a list of concepts
     */
    @Override
    public List<Concept> getConceptsRelatedWithByRelationType(
            Concept concept, RelationType relationType) {
        List<Concept> res = new ArrayList<>();
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationType").
                setParameter("leftArgumentConceptId", concept.getId()).
                setParameter("rightArgumentConceptId", concept.getId()).
                setParameter("relationTypeId", relationType);
        Concept tmpConcept = (Concept)query.getSingleResult();

        List<Relation> tmpR = query.getResultList();
        if (tmpR != null && tmpR.size() > 0) {
            for (Relation tmpR1 : tmpR) {
                if (tmpR1.getLeftArgument().isConcept()) {
                    if (tmpR1.getLeftArgument().getConcept().equals(concept)) {
                        res.add(tmpR1.getRightArgument().getConcept());
                    } else {
                        res.add(tmpR1.getLeftArgument().getConcept());
                    }
                }
            }
        }
        return res;
    }

    /**
     * Clears the entity manager
     */
    @Override
    public void clearManager() {
        getEntityManager().clear();
    }

    // TODO: All methods below are not referenced in ConceptDao
    /**
     * Creates q query to search for a concept using name, type, Status and
     * pragmatic Status
     *
     * @param concept the concept to be searched
     * @return a query to search for the concept
     */
    @Override
    public Query getEntityQuery(Concept concept) {
        Query query = getEntityManager().createNamedQuery(
                "getConceptEntityQuery").
                setParameter("externalSourceId", concept.getExternalSourceId()).
                setParameter("type", concept.getStatus()).
                setParameter("status", concept.getStatus());
        System.out.println("Concept externalSourceId: " + concept.
                getExternalSourceId());
        return query;
    }

    /**
     * Updates the language representations of a concept, adding the
     * LanguageRepresentation of another concept (removing them from that
     * concept).
     *
     * @param newConcept concept with LanguageRepresentation to be moved
     * @param oldConcept concept to be updated
     */
    private void updateLanguageRepresentations(Concept newConcept,
            Concept oldConcept) {
        try {
            for (int i = 0; i < newConcept.getLanguageRepresentations().size();
                    i++) {
                if (!oldConcept.getLanguageRepresentations().
                        contains(newConcept.getLanguageRepresentations().
                                get(i))) {
                    LanguageRepresentation tmpLanguageRepresentation =
                            newConcept.getLanguageRepresentations().get(i);
                    tmpLanguageRepresentation.getConcepts().remove(newConcept);
                    tmpLanguageRepresentation.getConcepts().add(oldConcept);
                    oldConcept.getLanguageRepresentations().
                            add(tmpLanguageRepresentation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //    entityManager.getTransaction().rollback();
        }
        // System.out.println("DONE WITH LanguageRepresentation");

    }

    /**
     * Updates the motoric representations of a concept, adding the
     * MotoricRepresentations of another concept (removing them from that
     * concept).
     *
     * @param newConcept concept with MotoricRepresentations to be moved
     * @param oldConcept concept to be updated
     */
    private void updateMotoricRepresentations(Concept newConcept,
            Concept oldConcept) {
        for (int i = 0; i < newConcept.getMotoricRepresentations().size();
                i++) {
            if (!oldConcept.getMotoricRepresentations().
                    contains(newConcept.getMotoricRepresentations().get(i))) {
                oldConcept.getMotoricRepresentations().
                        add(newConcept.getMotoricRepresentations().get(i));
            }
        }
    }

    //TODO: It is commented out until proven that is needed. 
    // Will be deleted otherwise.
//    /**
//     * Updates the ObjectOf relations of a concept, adding the ObjectOf
//     * relations of another concept (removing them from that concept).
//     *
//     * @param newRelationArgument concept with ObjectOf relations to be moved
//     * @param oldRelationArgument concept to be updated
//     */
//    private void updateObjOfRelations(RelationArgument newRelationArgument,
//            RelationArgument oldRelationArgument) {
//        for (int i = 0;
//                i < newRelationArgument.getConcept().
//                getRelationsContainingConceptAsObject().
//                size();
//                i++) {
//            if (!oldRelationArgument.getConcept().
//                    getRelationsContainingConceptAsObject().
//                    contains(newRelationArgument.getConcept().
//                            getRelationsContainingConceptAsObject().
//                            get(i))) {
//                if (newRelationArgument.getConcept().
//                        getRelationsContainingConceptAsObject().
//                        get(i).
//                        getObject().equals(newRelationArgument)) {
//                    newRelationArgument.getConcept().
//                            getRelationsContainingConceptAsObject().
//                            get(i).
//                            setObject(oldRelationArgument);
//                } else {
//                    newRelationArgument.getConcept().
//                            getRelationsContainingConceptAsObject().
//                            get(i).
//                            setRightArgument(oldRelationArgument);
//                }
//                oldRelationArgument.getConcept().
//                        getRelationsContainingConceptAsObject().
//                        add(newRelationArgument.getConcept().
//                                getRelationsContainingConceptAsObject().
//                                get(i));
//            }
//        }
//    }
// TODO: 10/7/2014 - I think this is not needed. Will check and reinstate 
//                  if necessary
//    /**
//     * Updates the relations of a concept, adding the relations of another
//     * concept (removing them from that concept).
//     *
//     * @param newConcept concept with relations to be moved
//     * @param oldConcept concept to be updated
//     */
//    private void updateRelations(Concept newConcept, Concept oldConcept) {
//        for (int i = 0; i < newConcept.getIntersectionsOfRelationChains().size();
//                i++) {
//            if (!oldConcept.getIntersectionsOfRelationChains().
//                    contains(newConcept.getIntersectionsOfRelationChains().
//                            get(i))) {
//                newConcept.getIntersectionsOfRelationChains().get(i).
//                        setConcept(oldConcept);
//                IntersectionOfRelationChains inter =
//                        newConcept.getIntersectionsOfRelationChains().get(i);
//                for (int k = 0; k < inter.getRelationChains().size(); k++) {
//                    RelationSet rc = inter.getRelationChains().get(k);
//                    for (int l = 0; l < rc.getRelations().size(); l++) {
//                        RelationChain_Relation rcr =
//                                rc.getRelations().get(l);
//                        Relation rel = rcr.getRelation();
//                        if (rel.getRightArgument().getExternalSourceId().
//                                equalsIgnoreCase(newConcept.getExternalSourceId())) {
//                            rel.setRightArgument(oldConcept);
//                        } else {
//                            if (rel.getObject().getExternalSourceId().
//                                    equalsIgnoreCase(newConcept.getExternalSourceId())) {
//                                rel.setObject(oldConcept);
//                            }
//                        }
//                    }
//                }
//                oldConcept.getIntersectionsOfRelationChains().
//                        add(newConcept.getIntersectionsOfRelationChains().
//                                get(i));
//            }
//        }
//    }
    /**
     * Updates the visual representations of a concept, adding the
     * VisualRepresentations of another concept (removing them from that
     * concept).
     *
     * @param newConcept concept with VisualRepresentations to be moved
     * @param oldConcept concept to be updated
     */
    private void updateVisualRepresentations(Concept newConcept,
            Concept oldConcept) {
        for (int i = 0; i < newConcept.getVisualRepresentationsEntries().size();
                i++) {
            if (!oldConcept.getVisualRepresentationsEntries().
                    contains(newConcept.getVisualRepresentationsEntries().
                            get(i))) {
                oldConcept.getVisualRepresentationsEntries().
                        add(newConcept.getVisualRepresentationsEntries().
                                get(i));
            }
        }
    }
}
