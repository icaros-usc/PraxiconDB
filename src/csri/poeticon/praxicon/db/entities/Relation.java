/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Constants;
import csri.poeticon.praxicon.Globals;
import csri.poeticon.praxicon.db.dao.ConceptDao;
import csri.poeticon.praxicon.db.dao.RelationTypeDao;
import csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import csri.poeticon.praxicon.db.dao.implSQL.RelationTypeDaoImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author Erevodifwntas
 * @author Dimitris Mavroeidis
 * 
 */

@XmlRootElement()
@Entity
@Table(name="Relation")
public class Relation implements Serializable
{
    public static enum derivation_supported
    {
        YES, NO, UNKNOWN ;
        @Override
        public String toString()
        {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="RelationId")
    private Long Id;

    @Column(name="Comment")
    private String Comment;
    
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "Relation")
    private List<RelationChain_Relation> MainFunctions;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    //@JoinColumn(name="Id")
    private RelationType Type;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    // @JoinColumn(name="ConceptId")
    private Concept Object;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    // @JoinColumn(name="ConceptId")
    private Concept Subject;

    @Column(name="DerivationSupported")
    @Enumerated(EnumType.STRING)
    protected derivation_supported DerivationSupported;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="RelationsWithLanguageRepresentationAsSubject")
    @JoinTable(
        name="LanguageRepresentation_RelationSubject",
        joinColumns={@JoinColumn(name="RelationId")},
        inverseJoinColumns={@JoinColumn(name="LanguageRepresentationId")}
    )
    private List<LanguageRepresentation> LanguageRepresentationSubject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="RelationsWithLanguageRepresentationAsObject")
    @JoinTable(
        name="LanguageRepresentation_RelationObject",
        joinColumns={@JoinColumn(name="RelationId")},
        inverseJoinColumns={@JoinColumn(name="LanguageRepresentationId")}
    )
    private List<LanguageRepresentation> LanguageRepresentationObject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="RelationsWithMotoricRepresentationAsSubject")
    @JoinTable(
        name="MotoricRepresentation_RelationSubject",
        joinColumns={@JoinColumn(name="RelationId")},
        inverseJoinColumns={@JoinColumn(name="MotoricRepresentationId")}
    )
    private List<MotoricRepresentation> MotoricRepresentationSubject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="RelationsWithMotoricRepresentationAsObject")
    @JoinTable(
        name="MotoricRepresentation_RelationObject",
        joinColumns={@JoinColumn(name="RelationId")},
        inverseJoinColumns={@JoinColumn(name="MotoricRepresentationId")}
    )
    private List<MotoricRepresentation> MotoricRepresentationObject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="RelationsWithVisualRepresentationAsSubject")
    @JoinTable(
        name="VisualRepresentation_RelationSubject",
        joinColumns={@JoinColumn(name="RelationId")},
        inverseJoinColumns={@JoinColumn(name="VisualRepresentationId")}
    )
    private List<VisualRepresentation> VisualRepresentationSubject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="RelationsWithVisualRepresentationAsObject")
    @JoinTable(
        name="VisualRepresentation_RelationObject",
        joinColumns={@JoinColumn(name="RelationId")},
        inverseJoinColumns={@JoinColumn(name="VisualRepresentationId")}
    )
    private List<VisualRepresentation> VisualRepresentationObject;



// TODO: Uncomment each method after checking it first
    public Relation()
    {
        MainFunctions = new ArrayList<RelationChain_Relation>();
        VisualRepresentationObject = new ArrayList<VisualRepresentation>();
        VisualRepresentationSubject = new ArrayList<VisualRepresentation>();
        LanguageRepresentationObject = new ArrayList<LanguageRepresentation>();
        LanguageRepresentationSubject = new ArrayList<LanguageRepresentation>();
        MotoricRepresentationObject = new ArrayList<MotoricRepresentation>();
        MotoricRepresentationSubject = new ArrayList<MotoricRepresentation>();
        Type = new RelationType();
    }

    @XmlTransient
    public Concept getSubject()
    {
        return Subject;
    }

    public void setSubject(Concept subject)
    {
        this.Subject = subject;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="is_derivative"
     *     xmldescription="This attribute defines if the relation is derivative or not"
     */
    @XmlAttribute(name="is_derivative")
    public derivation_supported DerivationSupported()
    {
        return DerivationSupported;
    }

    public void setDerivation(derivation_supported derivation_supported)
    {
        this.DerivationSupported = derivation_supported;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="subject"
     *     xmldescription="This attribute defines the object that the relation is
     *     related to"
     */
    @XmlAttribute(name="subject")
    private String getSubject_()
    {
        StringBuilder sb = new StringBuilder();
        if(Subject.getName()!= null && Subject.getName() != "")
        {
            return Subject.getName();
        }
        else
        {
            return Subject.getId()+"";
        }
    }

    private void setSubject_(String v) throws Exception
    {
        if (Globals.ToMergeAfterUnMarshalling)
        {
            ConceptDao cDao = new ConceptDaoImpl();
            Concept subjectCon = cDao.getConceptWithNameOrID(v.trim());
            if (subjectCon!=null)
            {
                Subject = subjectCon;
            }
            else
            {
                subjectCon = cDao.getConceptWithNameOrID(v.trim());
                Concept c = new Concept();
                c.setName(v);
                Subject = c;
                cDao.persist(Subject);
            }
         }
         else
         {
            Concept c = new Concept();
            c.setName(v);
            if (Constants.globalConcepts.contains(c))
            {
                Subject = (Concept)Constants.globalConcepts.get(c.getName());
            }
            else
            {
            Subject = c;
            Constants.globalConcepts.put(c.getName(), c);
            }
         }
    }
//
//    public void addRelationChain(RelationChain relation, long order)
//    {
//        //i think that is redundant
//    }

    @XmlTransient
    public List<RelationChain_Relation> getMainFunctions()
    {
        return MainFunctions;
    }

    public void setMainFunctions(List<RelationChain_Relation> main_functions)
    {
        this.MainFunctions = main_functions;
    }

    @XmlTransient
    public Concept getObject()
    {
        return Object;
    }

    public void setObject(Concept object)
    {
        this.Object = object;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="obj"
     *     xmldescription="This attribute defines the object that the relation is
     *     related to"
     */
    @XmlAttribute(name="obj")
    private String getObject_()
    {
        StringBuilder sb = new StringBuilder();
        if(Object.getName()!=null && Object.getName() != "")
        {
            return Object.getName();
        }
        else
        {
            return Object.getId()+"";
        }
    }

    private void setObj_(String v) throws Exception
    {

        if (Globals.ToMergeAfterUnMarshalling)
        {
            ConceptDao cDao = new ConceptDaoImpl();
            Concept objCon = cDao.getConceptWithNameOrID(v.trim());
            if (objCon!=null)
            {
                Object = objCon;
            }
            else
            {
                objCon = cDao.getConceptWithNameOrID(v.trim());
                Concept c = new Concept();
                c.setName(v);
                Object = c;
                cDao.persist(Object);
            }
        }
        else
        {
            Concept c = new Concept();

            c.setName(v);
            if (Constants.globalConcepts.contains(c))
            {
                Object = (Concept)Constants.globalConcepts.get(c.getName());
            }
            else
            {
                Object = c;
                Constants.globalConcepts.put(c.getName(), c);
            }
        }
    }
//
    /**
     * @xmlcomments.args
     *	   xmltag="&lt;type&gt;"
     *     xmldescription="This tag defines the type of the relation"
     */
   @XmlElement
    public RelationType getType()
    {
        return Type;
    }

   /**
    * Sets the type of the Relation but it doesn't check if there is the same
    * type twice
    * @param type the tyep of the relation
    */
    public void setTypeSimple(RelationType type)
    {
        this.Type = type;
    }

    public void setType(RelationType type)
    {
        if(type.getId() == null)
        {
            RelationTypeDao tmp = new RelationTypeDaoImpl();
            RelationType res = tmp.getEntity(type);
            if(res!=null)
            {
                type = res;
            }
        }
        this.Type = type;
    }

    @XmlAttribute
    public Long getId()
    {
        return Id;
    }

    public void setId(Long id)
    {
        this.Id = id;
    }

    @XmlAttribute()
    public String getComment()
    {
        return Comment;
    }

    public void setComment(String comment)
    {
        this.Comment = comment;
    }

   @XmlTransient
    public List<LanguageRepresentation> getLanguageRepresentationObject()
    {
        return LanguageRepresentationObject;
    }

   /**
     * @xmlcomments.args
     *	   xmltag="&lt;LanguageRepresentationGroupObject&gt;"
     *     xmldescription="This tag defines the LanguageRepresentationGroup that should be used to express the Object in this relation"
     */
   @XmlElement(name="LanguageRepresentationObject")
    public String getLanguageRepresentationObject_()
    {
        String language_representation_object_ = new String();
        // TODO: Not sure about the data type below.
        language_representation_object_ = LanguageRepresentationObject.toString();  //.getLanguaText();
        return language_representation_object_;
    }

    public void setLanguageRepresentationObject(List<LanguageRepresentation> language_representation_object)
    {
        this.LanguageRepresentationObject = language_representation_object;
    }

    
// TODO: REDUNDANT???
//    private void setLanguageRepresentationObject_(String v) throws Exception
//    {
//        if (Globals.ToMergeAfterUnMarshalling)
//        {
//       //     System.err.println("start "+v);
//            LanguageRepresentationDao lrgDao = new LanguageRepresentationGroupDaoImpl();
//            LanguageRepresentation lrg = lrgDao.findAllByName(v.trim());
//            if(lrg!=null && lrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//            {
//                lrg.add((LanguageRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//            }
//            if (lrg!=null && !lrg.isEmpty())
//            {
//                LanguageRepresentationGroupObject.addAll(lrg);
//                for(int j  = 0; j < lrg.size(); j++)
//                {
//                    lrg.get(j).getLanguageRepresentationObject().add(this);
//                }
//            }
//            else
//            {
//                LanguageRepresentationGroup c = new LanguageRepresentationGroup();
//
//                c.setName(v.get(i));
//                c.getLanguageRepresentationObject().add(this);
//                lrgDao.persist(c);
//                LanguageRepresentationGroupObject.add(c);
//            }
//
//         }
//         else
//         {
//            LanguageRepresentationGroup c = new LanguageRepresentationGroup();
//            c.setName(v.get(i));
//            c.getLanguageRepresentationObject().add(this);
//            if (Constants.globalConcepts.contains(c))
//            {
//                LanguageRepresentationGroupObject.add((LanguageRepresentationGroup)Constants.globalConcepts.get(c.getName()));
//            }
//            else
//            {
//                LanguageRepresentationGroupObject.add(c);
//                Constants.globalConcepts.put(c.getName(), c);
//            }
//         }
//    }

    @XmlTransient
    public List<LanguageRepresentation> getLanguageRepresentationSubject()
    {
        return LanguageRepresentationSubject;
    }

// TODO: Obsolete???
//    /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;LanguageRepresentationGroupSubject&gt;"
//     *     xmldescription="This tag defines the LanguageRepresentationGroup that should be used to express the Subject in this relation"
//     */
//    @XmlElement(name="LanguageRepresentationGroupSubject")
//    public List<String> getLanguageRepresentationGroupSubject_()
//    {
//        List<String> LanguageRepresentationGroupSubject_ = new ArrayList<String>();
//       for(int i = 0; i < LanguageRepresentationGroupSubject.size(); i++)
//       {
//           LanguageRepresentationGroupSubject_.add(LanguageRepresentationGroupSubject.get(i).getName());
//       }
//        return LanguageRepresentationGroupSubject_;
//    }
//

// TODO: Obsolete???
//    public void setLanguageRepresentationGroupSubject(List<LanguageRepresentationGroup> LanguageRepresentationGroupSubject)
//    {
//        this.LanguageRepresentationGroupSubject = LanguageRepresentationGroupSubject;
//    }
//
//    private void setLanguageRepresentationGroupSubject_(List<String> v) throws Exception
//    {
//        for (int i = 0; i < v.size(); i++)
//        {
//            if (Globals.ToMergeAfterUnMarshalling)
//            {
//                LanguageRepresentationGroupDao lrgDao = new LanguageRepresentationGroupDaoImpl();
//                List<LanguageRepresentationGroup> lrg = lrgDao.findAllByName(v.get(i).trim());
//                if(lrg!=null && lrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//                {
//                    lrg.add((LanguageRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//                }
//                if (lrg!=null && !lrg.isEmpty())
//                {
//                    LanguageRepresentationGroupSubject.addAll(lrg);
//                    for(int j  = 0; j < lrg.size(); j++)
//                    {
//                        lrg.get(j).getLanguageRepresentationSubject().add(this);
//                    }
//                }
//                else
//                {
//                    LanguageRepresentationGroup c = new LanguageRepresentationGroup();
//
//                    c.setName(v.get(i));
//                    c.getLanguageRepresentationSubject().add(this);
//                    lrgDao.persist(c);
//                    LanguageRepresentationGroupSubject.add(c);
//                }
//
//             }
//             else
//             {
//                LanguageRepresentationGroup c = new LanguageRepresentationGroup();
//                c.setName(v.get(i));
//                c.getLanguageRepresentationSubject().add(this);
//                if (Constants.globalConcepts.contains(c))
//                {
//                    LanguageRepresentationGroupSubject.add((LanguageRepresentationGroup)Constants.globalConcepts.get(c.getName()));
//                }
//                else
//                {
//                    LanguageRepresentationGroupSubject.add(c);
//                    Constants.globalConcepts.put(c.getName(), c);
//                }
//             }
//        }
//    }
//
    @XmlTransient
    public List<MotoricRepresentation> getMotoricRepresentationObject()
    {
        return MotoricRepresentationObject;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;MotoricRepresentationGroupObject&gt;"
     *     xmldescription="This tag defines the MotoricRepresentationGroup that should be used to express the Object in this relation"
     */
    @XmlElement(name="MotoricRepresentationGroupObject")
    public List<String> getMotoricRepresentationObject_()
    {
        List<String> motoric_representation_object_ = new ArrayList<String>();
       for(int i = 0; i < MotoricRepresentationObject.size(); i++)
       {
           motoric_representation_object_.add(MotoricRepresentationObject.get(i).toString());
       }
        return motoric_representation_object_;
    }

    public void setMotoricRepresentationObject(List<MotoricRepresentation> motoric_representation_object)
    {
        this.MotoricRepresentationObject = motoric_representation_object;
    }

// TODO: Obsolete???
//    private void setMotoricRepresentationGroupObject_(List<String> v) throws Exception
//    {
//        for (int i = 0; i < v.size(); i++)
//        {
//            if (Globals.ToMergeAfterUnMarshalling)
//            {
//                MotoricRepresentationGroupDao mrgDao = new MotoricRepresentationGroupDaoImpl();
//                List<MotoricRepresentationGroup> mrg = mrgDao.findAllByName(v.get(i).trim());
//                if(mrg!=null && mrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//                {
//                    mrg.add((MotoricRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//                }
//                if (mrg!=null && !mrg.isEmpty())
//                {
//                    MotoricRepresentationGroupObject.addAll(mrg);
//                    for(int j  = 0; j < mrg.size(); j++)
//                    {
//                        mrg.get(j).getMotoricRepresentationObject().add(this);
//                    }
//                }
//                else
//                {
//                    MotoricRepresentationGroup c = new MotoricRepresentationGroup();
//
//                    c.setName(v.get(i));
//                    c.getMotoricRepresentationObject().add(this);
//                    mrgDao.persist(c);
//                    MotoricRepresentationGroupObject.add(c);
//                }
//             }
//             else
//             {
//                MotoricRepresentationGroup c = new MotoricRepresentationGroup();
//                c.setName(v.get(i));
//                c.getMotoricRepresentationObject().add(this);
//                if (Constants.globalConcepts.contains(c))
//                {
//                    MotoricRepresentationGroupObject.add((MotoricRepresentationGroup)Constants.globalConcepts.get(c.getName()));
//                }
//                else
//                {
//                    MotoricRepresentationGroupObject.add(c);
//                    Constants.globalConcepts.put(c.getName(), c);
//                }
//
//             }
//        }
//    }

    @XmlTransient
    public List<MotoricRepresentation> getMotoricRepresentationSubject()
    {
        return MotoricRepresentationSubject;
    }

// TODO: Obsolete???
//        /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;MotoricRepresentationGroupSubject&gt;"
//     *     xmldescription="This tag defines the MotoricRepresentationGroup that should be used to express the Subject in this relation"
//     */
//    @XmlElement(name="MotoricRepresentationGroupSubject")
//    public List<String> getMotoricRepresentationGroupSubject_()
//    {
//        List<String> MotoricRepresentationGroupSubject_ = new ArrayList<String>();
//       for(int i = 0; i < MotoricRepresentationGroupSubject.size(); i++)
//       {
//           MotoricRepresentationGroupSubject_.add(MotoricRepresentationGroupSubject.get(i).getName());
//       }
//        return MotoricRepresentationGroupSubject_;
//    }
//
//    public void setMotoricRepresentationGroupSubject(List<MotoricRepresentationGroup> MotoricRepresentationGroupSubject)
//    {
//        this.MotoricRepresentationGroupSubject = MotoricRepresentationGroupSubject;
//    }
//
//    private void setMotoricRepresentationGroupSubject_(List<String> v) throws Exception
//    {
//        for (int i = 0; i < v.size(); i++)
//        {
//            if (Globals.ToMergeAfterUnMarshalling)
//            {
//                MotoricRepresentationGroupDao mrgDao = new MotoricRepresentationGroupDaoImpl();
//                List<MotoricRepresentationGroup> mrg = mrgDao.findAllByName(v.get(i).trim());
//                if(mrg!=null && mrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//                {
//                    mrg.add((MotoricRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//                }
//                if (mrg!=null && !mrg.isEmpty())
//                {
//                    MotoricRepresentationGroupSubject.addAll(mrg);
//                    for(int j  = 0; j < mrg.size(); j++)
//                    {
//                        mrg.get(j).getMotoricRepresentationSubject().add(this);
//                    }
//                }
//                else
//                {
//                    MotoricRepresentationGroup c = new MotoricRepresentationGroup();
//
//                    c.setName(v.get(i));
//                    c.getMotoricRepresentationSubject().add(this);
//                    mrgDao.persist(c);
//                    MotoricRepresentationGroupSubject.add(c);
//                }
//
//             }
//             else
//             {
//                MotoricRepresentationGroup c = new MotoricRepresentationGroup();
//                c.setName(v.get(i));
//                c.getMotoricRepresentationSubject().add(this);
//                if (Constants.globalConcepts.contains(c))
//                {
//                    MotoricRepresentationGroupSubject.add((MotoricRepresentationGroup)Constants.globalConcepts.get(c.getName()));
//                }
//                else
//                {
//                    MotoricRepresentationGroupSubject.add(c);
//                    Constants.globalConcepts.put(c.getName(), c);
//                }
//
//             }
//        }
//    }
//
    @XmlTransient
    public List<VisualRepresentation> getVisualRepresentationObject()
    {
        return VisualRepresentationObject;
    }


// TODO: Obsolete???
//        /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;VisualRepresentationGroupObject&gt;"
//     *     xmldescription="This tag defines the VisualRepresentationGroup that should be used to express the Object in this relation"
//     */
//    @XmlElement(name="VisualRepresentationGroupObject")
//    public List<String> getVisualRepresentationGroupObject_()
//    {
//        List<String> VisualRepresentationGroupObject_ = new ArrayList<String>();
//       for(int i = 0; i < VisualRepresentationGroupObject.size(); i++)
//       {
//           VisualRepresentationGroupObject_.add(VisualRepresentationGroupObject.get(i).getName());
//       }
//        return VisualRepresentationGroupObject_;
//    }
//
    public void setVisualRepresentationObject(List<VisualRepresentation> visual_representation_object)
    {
        this.VisualRepresentationObject = visual_representation_object;
    }

// TODO: Obsolete???
//    private void setVisualRepresentationGroupObject_(List<String> v) throws Exception
//    {
//        for (int i = 0; i < v.size(); i++)
//        {
//            if (Globals.ToMergeAfterUnMarshalling)
//            {
//                VisualRepresentationGroupDao vrgDao = new VisualRepresentationGroupDaoImpl();
//                List<VisualRepresentationGroup> vrg = vrgDao.findAllByName(v.get(i).trim());
//                if(vrg!=null && vrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//                {
//                    vrg.add((VisualRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//                }
//                if (vrg!=null && !vrg.isEmpty())
//                {
//                    VisualRepresentationGroupObject.addAll(vrg);
//                    for(int j  = 0; j < vrg.size(); j++)
//                    {
//                        vrg.get(j).getVisualRepresentationObject().add(this);
//                    }
//                }
//                else
//                {
//                    VisualRepresentationGroup c = new VisualRepresentationGroup();
//
//                    c.setName(v.get(i));
//                    c.getVisualRepresentationObject().add(this);
//                    vrgDao.persist(c);
//                    VisualRepresentationGroupObject.add(c);
//                }
//
//             }
//             else
//             {
//                VisualRepresentationGroup c = new VisualRepresentationGroup();
//                c.setName(v.get(i));
//                c.getVisualRepresentationObject().add(this);
//                if (Constants.globalConcepts.contains(c))
//                {
//                    VisualRepresentationGroupObject.add((VisualRepresentationGroup)Constants.globalConcepts.get(c.getName()));
//                }
//                else
//                {
//                    VisualRepresentationGroupObject.add(c);
//                    Constants.globalConcepts.put(c.getName(), c);
//                }
//
//             }
//        }
//    }
//
    @XmlTransient
    public List<VisualRepresentation> getVisualRepresentationSubject()
    {
        return VisualRepresentationSubject;
    }

// TODO: Obsolete???
//    /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;VisualRepresentationGroupSubject&gt;"
//     *     xmldescription="This tag defines the VisualRepresentationGroup that should be used to express the Subject in this relation"
//     */
//    @XmlElement(name="VisualRepresentationGroupSubject")
//    public List<String> getVisualRepresentationGroupSubject_()
//    {
//        List<String> VisualRepresentationGroupSubject_ = new ArrayList<String>();
//       for(int i = 0; i < VisualRepresentationGroupSubject.size(); i++)
//       {
//           VisualRepresentationGroupSubject_.add(VisualRepresentationGroupSubject.get(i).getName());
//       }
//        return VisualRepresentationGroupSubject_;
//    }

    public void setVisualRepresentationSubject(List<VisualRepresentation> visual_representation_subject)
    {
        this.VisualRepresentationSubject = VisualRepresentationSubject;
    }

// TODO: Obsolete???
//
//    private void setVisualRepresentationGroupSubject_(List<String> v) throws Exception
//    {
//        for (int i = 0; i < v.size(); i++)
//        {
//            if (Globals.ToMergeAfterUnMarshalling)
//            {
//                VisualRepresentationGroupDao vrgDao = new VisualRepresentationGroupDaoImpl();
//                List<VisualRepresentationGroup> vrg = vrgDao.findAllByName(v.get(i).trim());
//                if(vrg!=null && vrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//                {
//                    vrg.add((VisualRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//                }
//                if (vrg!=null && !vrg.isEmpty())
//                {
//                    VisualRepresentationGroupSubject.addAll(vrg);
//                    for(int j  = 0; j < vrg.size(); j++)
//                    {
//                        vrg.get(j).getVisualRepresentationSubject().add(this);
//                    }
//                }
//                else
//                {
//                    VisualRepresentationGroup c = new VisualRepresentationGroup();
//
//                    c.setName(v.get(i));
//                    c.getVisualRepresentationSubject().add(this);
//                    vrgDao.persist(c);
//                    VisualRepresentationGroupSubject.add(c);
//                }
//
//             }
//             else
//             {
//                VisualRepresentationGroup c = new VisualRepresentationGroup();
//                c.setName(v.get(i));
//                c.getVisualRepresentationSubject().add(this);
//                if (Constants.globalConcepts.contains(c))
//                {
//                    VisualRepresentationGroupSubject.add((VisualRepresentationGroup)Constants.globalConcepts.get(c.getName()));
//                }
//                else
//                {
//                    VisualRepresentationGroupSubject.add(c);
//                    Constants.globalConcepts.put(c.getName(), c);
//                }
//
//             }
//        }
//    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (Id != null ? Id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Relation))
        {
            return false;
        }
        Relation other = (Relation) object;
        try
        {
            if ((this.Type!=null && this.Object!=null && this.Subject!=null
                && this.Type.equals(other.Type) && this.Object.equals(other.Object) && this.Subject.equals(other.Subject))||
                (this.Type!=null && this.Object!=null && this.Subject!=null
                &&this.Type.equals(other.Type) && this.Object.equals(other.Subject) && this.Subject.equals(other.Object)))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return this.getSubject() + " " + this.getType().getForwardName() + " " + this.getObject();
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        if (Globals.ToMergeAfterUnMarshalling)
        {
            RelationTypeDao rDao = new RelationTypeDaoImpl();
            this.Type = rDao.getEntity(Type);
            ConceptDao cDao = new ConceptDaoImpl();
            this.Object = cDao.getEntity(Object);
            Object.getRelationsContainingConceptAsObject().add(this);
            this.Subject = cDao.getEntity(Subject);
        }
    }
}