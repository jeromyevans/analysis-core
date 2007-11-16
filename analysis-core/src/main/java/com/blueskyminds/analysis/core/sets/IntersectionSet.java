package com.blueskyminds.analysis.core.sets;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Define an intesection of AggegrateSets used to analyse a data series
 *
 * Intersection implies the Object belongs to all listed AggregateSets.
 *
 * Date Started: 19/08/2006
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@DiscriminatorValue("intersection")
public class IntersectionSet extends AggregateSet {

    private Set<IntersectionSetEntry> intersectingSets;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new intersection from one or more aggregate sets
     * @param aggregateSets
     */
    public IntersectionSet(String key, AggregateSet... aggregateSets) {
        super(key);
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
        intersectingSets = new HashSet<IntersectionSetEntry>();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the list of aggregate sets in this series */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "intersectionSet")
    public Set<IntersectionSetEntry> getIntersectingSets() {
        return intersectingSets;
    }

    public void setIntersectingSets(Set<IntersectionSetEntry> intersectingSets) {
        this.intersectingSets = intersectingSets;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Add another aggregate set to the union */
    public boolean includeAggregateSet(AggregateSet aggregateSet) {
        return intersectingSets.add(new IntersectionSetEntry(this, aggregateSet));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Evaluate whether the specified object belongs in this intersection of sets.
     *
     * Iterates through all the sets in the intersection.  If the domain object belongs in that set, it then
     *  traverses into the next set in the series and the same test applied until a set is encountered
     *  that the domain object doesn't belong in, or the end of the series is reached (success)
     *
      *@param object
     * @return true if the domain object belongs in UNION of all of the sets
     */
    @Transient
    public boolean isInSet(Object object) {
        boolean inIntersection = true;

        for (IntersectionSetEntry aggregateSet : intersectingSets) {
            if (!aggregateSet.isInSet(object)) {
                inIntersection = false;
                break;
            }
        }

        return inIntersection;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the calculated name of this intersection */
    @Transient
    public String getRuleName() {
        List<String> names = new LinkedList<String>();
        for (IntersectionSetEntry entry : intersectingSets) {
            names.add(entry.getAggregateSet().getRuleName());
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
