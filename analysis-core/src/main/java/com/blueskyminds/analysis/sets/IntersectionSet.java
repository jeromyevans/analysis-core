package com.blueskyminds.analysis.sets;

import com.blueskyminds.framework.DomainObject;
import com.blueskyminds.analysis.sets.AggregateSet;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;

/**
 * Define an intesection of AggegrateSets used to analyse a data series
 *
 * The intersection implies the DomainObject must belong on all sets defined in this series.
 *
 * Date Started: 19/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@Table(name="IntersectionSet")
public class IntersectionSet extends AggregateSet {

    private List<AggregateSet> intersectingSets;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new intersection from one or more aggregate sets
     * @param aggregateSets
     */
    public IntersectionSet(AggregateSet... aggregateSets) {
        init();
        for (AggregateSet aggregateSet : aggregateSets) {
            includeAggregateSet(aggregateSet);
        }
    }

    /** Default constructor for ORM */
    protected IntersectionSet() {
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the IntersectionSet with default attributes
     */
    private void init() {
        intersectingSets = new LinkedList<AggregateSet>();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the list of aggregate sets in this series */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="IntersectionSetEntry",
            joinColumns=@JoinColumn(name="IntersectionSetId"),
            inverseJoinColumns = @JoinColumn(name="AggregateSetId")
    )
    protected List<AggregateSet> getIntersectingSets() {
        return intersectingSets;
    }

    protected void setIntersectingSets(List<AggregateSet> intersectingSets) {
        this.intersectingSets = intersectingSets;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Add another aggregate set to the union */
    public boolean includeAggregateSet(AggregateSet aggregateSet) {
        return intersectingSets.add(aggregateSet);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Evaluate whether the specified domain object belongs in this union of sets.
     *
     * Iterates from the first set in the union.  If the domain object belongs in that set, it then
     *  traverses into the next set in the series and the same test applied until a set is encountered
     *  that the domain object doesn't belong in, or the end of the series is reached (success)
     *
      *@param domainObject
     * @return true if the domain object belongs in UNION of all of the sets
     */
    @Transient
    public boolean isInSet(DomainObject domainObject) {
        boolean inIntersection = true;

        for (AggregateSet aggregateSet : intersectingSets) {
            if (!aggregateSet.isInSet(domainObject)) {
                inIntersection = false;
                break;
            }
        }

        return inIntersection;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the calculated name of this union */
    @Transient
    public String getName() {
        List<String> names = new LinkedList<String>();
        for (AggregateSet aggregateSet : intersectingSets) {
            names.add(aggregateSet.getName());
        }
        return "Intersection["+ StringUtils.join(names.iterator(), ",")+"]";
    }

    // ------------------------------------------------------------------------------------------------------
        
   /* @Transient
    public String getIdentityName() {
        return super.getIdentityName()+"("+getNamed()+")";
    }*/

    // ------------------------------------------------------------------------------------------------------
}
