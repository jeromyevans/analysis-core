package com.blueskyminds.analysis.core.sets;

import com.blueskyminds.framework.tools.filters.Filter;
import com.blueskyminds.framework.tools.filters.FilterTools;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple implementation of an AggregrateSet that matches on whether the named property of the
 *  Object EQUALS a specific value (exactly), or EQUALS one of a set of filterValues (exactly)
 *
 * ie. it is in this set if the property equals the value
 *
 * Date Started: 19/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@DiscriminatorValue("equals")
public class SimpleAggregateSet extends AggregateSet {

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

    /**
     * Add an value that allows entry into this set
     * NOTE: the filterValue is converted to a String with toString()
     **/
    public boolean defineValue(Object filterValue) {
        return filterValues.add(new SimpleAggregateSetValue(this, filterValue.toString()));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Evaluate whether the object belongs in this Set.
     * Evaluation is by string value equality
     **/
    @Transient
    public boolean isInSet(Object object) {
        final String value = lookupPropertyValue(object, propertyName);
        return FilterTools.matchesAny(filterValues, new Filter<SimpleAggregateSetValue>(){
            public boolean accept(SimpleAggregateSetValue filterValue) {
                return filterValue.getValue().equals(value);
            }
        });
    }

    // ------------------------------------------------------------------------------------------------------
    /**
     * Get the value for the named property in this domain object, converted to a string.
     *
     * @param name
     * @return the property value, or null if it's not recognised.
     */
    @Transient
    protected String lookupPropertyValue(Object object, String name) {
        Object value = null;

        try {
            value = BeanUtils.getSimpleProperty(object, name);
        } catch(IllegalAccessException e) {
            //
        } catch(InvocationTargetException e) {
            //
        } catch(NoSuchMethodException e) {
            //
        }
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
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
