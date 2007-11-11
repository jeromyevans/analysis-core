package com.blueskyminds.analysis.core.sets;

import com.blueskyminds.framework.DomainObject;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

/**
 * Contains a Set of Domain Objects and methods for manipulating the set of objects.
 *
 * The DomainSet is immutable
 *
 * Date Started: 4/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class DomainSet<T extends DomainObject> {

    private final Set<T> domainObjects;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new domain set from a collection of domain objects*/
    public DomainSet(Collection<T> domainObjects) {
        this.domainObjects = new HashSet<T>(domainObjects);
        init();
    }

    // ------------------------------------------------------------------------------------------------------    
    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the DomainSet with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Add a domain object to the set */
    /*public boolean add(T domainObject) {
        return domainObjects.add(domainObject);
    }*/

    // ------------------------------------------------------------------------------------------------------

    /** Add all of the specified domain objects to the set */
    private boolean addAll(Collection<T> domainObjects) {
        return this.domainObjects.addAll(domainObjects);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the union of the specified DomainSet.
     * @return a new set that is the intersection of the DomainSets */
    public DomainSet union(DomainSet domainSet) {
        Set<T> unionSet = new HashSet<T>(domainObjects);
        unionSet.addAll(domainSet.domainObjects);
        return newDomainSet(unionSet);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the intersection of this set with the specified DomainSet.
     * @return a new set that is the intersection of the DomainSets */
    public DomainSet intersection(DomainSet other) {
        Set<T> intersectionSet = new HashSet<T>(Math.min(domainObjects.size(), other.domainObjects.size()));
        for (T domainObject : domainObjects) {
            if (other.domainObjects.contains(domainObject)) {
                intersectionSet.add(domainObject);
            }
        }

        return newDomainSet(intersectionSet);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Return a subset of the domain set where the specifid property equals value */
    public DomainSet<T> subset(String name, Object value) {
        Set<T> subset = new HashSet<T>(domainObjects.size());
        Object p;

        for (T domainObject : domainObjects) {
            p = domainObject.getProperty(name);
            if (p != null) {
                if (p.equals(value)) {
                    subset.add(domainObject);
                }
            } else {
                // if property is null, and value is null then include
                if (value == null) {
                    subset.add(domainObject);
                }
            }
        }
        return newDomainSet(subset);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Creates a new instance of a DomainSet */
    private DomainSet<T> newDomainSet(Set<T> domainObjects) {
        return new DomainSet<T>(domainObjects);
    }

    // ------------------------------------------------------------------------------------------------------
}
