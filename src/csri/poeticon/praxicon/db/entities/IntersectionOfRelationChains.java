/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Constants;
import csri.poeticon.praxicon.Globals;
import csri.poeticon.praxicon.db.dao.RelationChainDao;
import csri.poeticon.praxicon.db.dao.implSQL.RelationChainDaoImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
 */
@XmlRootElement()
@Entity
@Table(name="IntersectionOfRelationChains")
public class IntersectionOfRelationChains implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="IntersectionId")
    private Long Id;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
    name="IntersectionRelation",
    joinColumns={@JoinColumn(name="IntersectionId")},
    inverseJoinColumns={@JoinColumn(name="RelationId")}
    )
    List<RelationChain> Relations;

//    @ManyToMany(cascade=CascadeType.ALL, mappedBy="intersections")
//    @JoinTable(
//    name="UNION_INTERSECTIONRELATION",
//    joinColumns={@JoinColumn(name="INTERSECTION_ID")},
//    inverseJoinColumns={@JoinColumn(name="UNION_ID")}
//    )
//    List<UnionOfIntersections> unions;


    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="ConceptId")
    Concept Concept;


    @ManyToMany(cascade=CascadeType.ALL, mappedBy="LanguageRepresentationIntersection")
    @JoinTable(
        name="LanguageRepresentationIntersection",
        joinColumns={@JoinColumn(name="IntersectionId")},
        inverseJoinColumns={@JoinColumn(name="LanguageRepresentationId")}
    )
    List<LanguageRepresentation> LanguageRepresentationNames;

    @Column(name="Name")
    String Name;


    public IntersectionOfRelationChains()
    {
        Relations = new ArrayList<RelationChain>();
//        unions = new ArrayList<UnionOfIntersections>();  //Obsolete
        LanguageRepresentationNames = new ArrayList<LanguageRepresentation>();
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;relation_chain&gt;"
     *     xmldescription="This tag defines a relation"
     */
    @XmlElement(name="relation_chain")
    public List<RelationChain> getRelations()
    {
        return Relations;
    }

    @XmlTransient
    public List<LanguageRepresentation> getLanguageRepresentationNames()
    {
        return LanguageRepresentationNames;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;LRGroupNames&gt;"
     *     xmldescription="This tag defines the names of the LanguageRepresentationGroup that should be used to express this intersection"
     */
   @XmlElement(name="LanguageRepresentationName")
    public List<String> getLanguageRepresentationNames_()
    {
       List<String> language_representation_names_ = new ArrayList<String>();
       for(int i = 0; i < LanguageRepresentationNames.size(); i++)
       {
           language_representation_names_.add(LanguageRepresentationNames.get(i).getText());
       }
        return language_representation_names_;
    }

    public void setLanguageRepresentationNames(List<LanguageRepresentation> language_representation_names)
    {
        this.LanguageRepresentationNames = language_representation_names;
    }

//    private void setLanguageRepresentationNames_(List<String> v) throws Exception
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
//                    language_representation_names.addAll(lrg);
//                    for(int j  = 0; j < lrg.size(); j++)
//                    {
//                        lrg.get(j).getLanguageRepresentationIntersections().add(this);
//                    }
//                }
//                else
//                {
//                    LanguageRepresentation c = new LanguageRepresentation();
//
//                    c.setText(v.get(i));
//                    c.getLanguageRepresentationIntersections().add(this);
//                    lrgDao.persist(c);
//                    language_representation_names.add(c);
//                }
//
//             }
//             else
//             {
//                LanguageRepresentation c = new LanguageRepresentation();
//                c.setText(v.get(i));
//                c.getLanguageRepresentationIntersections().add(this);
//                if (Constants.globalConcepts.contains(c))
//                {
//                    language_representation_names.add((LanguageRepresentation)Constants.globalConcepts.get(c.getText()));
//                }
//                else
//                {
//                    language_representation_names.add(c);
//                    Constants.globalConcepts.put(c.getText(), c);
//                }
//
//             }
//        }
//    }

    public void addRelationChain(RelationChain rc)
    {
        rc.getIntersections().add(this);
        Relations.add(rc);
    }

    public void addRelationChain1way(RelationChain rc)
    {
        Relations.add(rc);
    }

    public void setRelations(List<RelationChain> relations) {
        this.Relations = relations;
    }

// Obsolete
//    @XmlTransient
//    public List<UnionOfIntersections> getUnions() {
//        return unions;
//    }

// Obsolete
//    public void setUnions(List<UnionOfIntersections> unions) {
//        this.unions = unions;
//    }

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
    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        this.Name = name;
    }

    @XmlTransient
    public Concept getConcept()
    {
        return Concept;
    }

    public void setConcept(Concept concept)
    {
        this.Concept = concept;
    }


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
        if (!(object instanceof IntersectionOfRelationChains))
        {
            return false;
        }
        IntersectionOfRelationChains other = (IntersectionOfRelationChains) object;
        if (this.Relations.size() == other.Relations.size())
        {
            boolean eq= true;
            for (int i = 0; i < this.Relations.size(); i++)
            {
                if (!other.Relations.contains(this.Relations.get(i)))
                {
                    eq = false;
                    break;
                }
            }
            if (eq)
            {
                return true;
            }
        }
        if ((this.Id == null && other.Id != null) || (this.Id != null && !this.Id.equals(other.Id)))
        {
            return false;
        }

        if (this.Id == null && other.Id == null)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "csri.poeticon.praxicon.db.entities.IntersectionOfRelations[id=" + Id + "]";
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        if (Globals.ToMergeAfterUnMarshalling)
        {
            RelationChainDao rcDao = new RelationChainDaoImpl();
            for (int i = 0; i<this.getRelations().size(); i++)
            {
                RelationChain rc =  rcDao.getEntity(this.getRelations().get(i));
                this.getRelations().set(i,rc);
            }
        }
        else
        {
            for (int i = 0; i<this.getRelations().size(); i++)
            {
                this.getRelations().get(i).getIntersections().add(this);
            }
        }
    }
}