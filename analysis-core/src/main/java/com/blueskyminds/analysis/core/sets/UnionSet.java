package com.blueskyminds.analysis.core.sets;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Define a union of AggegrateSets used to analyse a data series
 *
 * The union implies the DomainObject must belong in one or more of the sets defined in this series.
 *
 * Date Started: 19/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@DiscriminatorValue("union")
public class UnionSet extends AggregateSet {

    private Set<UnionSetEntry> unionSets;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new union from one or more aggregate sets
     * @param aggregateSets
     */
    public UnionSet(AggregateSet... aggregateSets) {
        init();
        for (AggregateSet aggregateSet : aggregateSets) {
            includeAggregateSet(aggregateSet);
        }
    }

    /** Default constructor for ORM */
    protected UnionSet() {
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the UnionSet with default attributes
     */
    private void init() {
        unionSets = new HashSet<UnionSetEntry>();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the list of aggregate sets in this series */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unionSet")
    protected Set<UnionSetEntry> getUnionSets() {
        return unionSets;
    }

    protected void setUnionSets(Set<UnionSetEntry> unionSets) {
        this.unionSets = unionSets;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Add another aggregate set to the union */
    public boolean includeAggregateSet(AggregateSet aggregateSet) {
        return unionSets.add(new UnionSetEntry(this, aggregateSet));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Evaluate whether the specified domain object belongs in this union of sets.
     *
     * Iterates from the first set in the union.  If the domain object belongs in any of the sets
     *  in the series the result is true
     *
      *@param object
     * @return true if the domain object belongs in UNION of all of the sets
     */
    @Transient
    public boolean isInSet(Object object) {
        boolean inUnion = false;

        for (UnionSetEntry aggregateSet : unionSets) {
            if (aggregateSet.isInSet(object)) {
                inUnion = true;
                break;
            }
        }

        return inUnion;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the calculated name of this union */
    @Transient
    public String getName() {
        List<String> names = new LinkedList<String>();
        for (UnionSetEntry aggregateSet : unionSets) {
            names.add(aggregateSet.getAggregateSet().getName());
        }
        return "Union["+ StringUtils.join(names.iterator(), ",")+"]";
    }

    // ------------------------------------------------------------------------------------------------------

//    @Transient
//    public String getIdentityName() {
//        return super.getIdentityName()+"("+getNamed()+")";
//    }
    // ------------------------------------------------------------------------------------------------------
}
