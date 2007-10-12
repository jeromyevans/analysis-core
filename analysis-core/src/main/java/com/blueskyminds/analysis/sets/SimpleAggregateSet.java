package com.blueskyminds.analysis.sets;

import com.blueskyminds.framework.DomainObject;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;

/**
 * Simple implementation of an AggregrateSet that matches on whether the named property of the
 *  DomainObject EQUALS a specific value (exactly), or EQUALS of of a set of filterValues (exactly)
 *
 * ie. it is in this set if the property equals the value
 *
 * Date Started: 19/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@Table(name = "SimpleAggregateSet")
public class SimpleAggregateSet<T extends DomainObject> extends AggregateSet<T> {

    private String propertyName;
    private List<SimpleAggregateSetValue> filterValues;

    // ------------------------------------------------------------------------------------------------------

    /** Define a new set, matching on the specified property of the domain object,
     * specifying a single permissible values */
    public SimpleAggregateSet(String propertyName, Object filterValue) {
        this.propertyName = propertyName;
        init();
        defineValue(filterValue);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Define a new set, matching on the specified property of the domain object,
     * specifying multiple permissible values
     *
     * @param propertyName
     * @param filterValues
     */
    public SimpleAggregateSet(String propertyName, Object... filterValues) {
        this.propertyName = propertyName;
        init();
        for (Object filterValue : filterValues) {
            defineValue(filterValue);
        }
    }

    /** Default constructor for ORM */
    protected SimpleAggregateSet() {
    }

    // ------------------------------------------------------------------------------------------------------

    private void init() {
        filterValues = new LinkedList<SimpleAggregateSetValue>();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the name of the property that this SimpleAggregateSet is testing */
    @Basic
    @Column(name = "Property")
    public String getPropertyName() {
        return propertyName;
    }

    protected void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the list of values that determine whether a DomainObject belongs in this set */
//    @CollectionOfElements
//    @JoinTable(
//            name="SimpleAggreateSetValues",
//            joinColumns=@JoinColumn(name="SimpleAggregateSetId")
//    )
//    @Column(name="Value")
    @OneToMany(mappedBy = "aggregateSet", cascade = CascadeType.ALL)
    public List<SimpleAggregateSetValue> getFilterValueMaps() {
        return filterValues;
    }

    protected void setFilterValueMaps(List<SimpleAggregateSetValue> filterValues) {
        this.filterValues = filterValues;
    }

    @Transient
   /** Get the list of values that determine whether a DomainObject belongs in this set */
    public List<String> getFilterValues() {
        List<String> values = new LinkedList<String>();
        for (SimpleAggregateSetValue value : filterValues) {
            values.add(value.getValue());
        }
        return values;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Add an additional value that allows entry into this set
     * NOTE: the filterValue is converted to a String with toString() */
    public boolean defineValue(Object filterValue) {
        return filterValues.add(new SimpleAggregateSetValue(this, filterValue.toString()));
    }

    // ------------------------------------------------------------------------------------------------------

    /** Evaluate whether the domain object belongs in this Set */
    @Transient
    public boolean isInSet(T domainObject) {
        return (filterValues.contains(domainObject.getProperty(propertyName)));
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the calculated name of this union */
    @Transient
    public String getName() {
        return "'"+propertyName+"' in ["+ StringUtils.join(filterValues.iterator(), ",")+"]";
    }

    // ------------------------------------------------------------------------------------------------------

    /*@Transient
    public String getIdentityName() {
        return super.getIdentityName()+"("+getNamed()+")";
    }*/

    // ------------------------------------------------------------------------------------------------------
}
