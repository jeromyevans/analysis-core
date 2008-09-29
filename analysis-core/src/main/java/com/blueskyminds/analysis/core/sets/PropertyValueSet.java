package com.blueskyminds.analysis.core.sets;

import com.blueskyminds.homebyfive.framework.core.tools.filters.Filter;
import com.blueskyminds.homebyfive.framework.core.tools.filters.FilterTools;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
@DiscriminatorValue("property")
public class PropertyValueSet extends AggregateSet {

    private String propertyName;
    private Set<PropertyValueSetEntry> propertyValueEntries;

    // ------------------------------------------------------------------------------------------------------

    /** Define a new set, matching on the specified property of the domain object,
     * specifying a single permissible values */
    public PropertyValueSet(String key, String propertyName, Object filterValue) {
        super(key);
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
    public PropertyValueSet(String key, String propertyName, Object... filterValues) {
        super(key);
        this.propertyName = propertyName;
        init();
        for (Object filterValue : filterValues) {
            defineValue(filterValue);
        }
    }

    /** Default constructor for ORM */
    protected PropertyValueSet() {
    }

    // ------------------------------------------------------------------------------------------------------

    private void init() {
        propertyValueEntries = new HashSet<PropertyValueSetEntry>();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the name of the property that this SimpleAggregateSet is testing */
    @Basic
    @Column(name = "PropertyName")
    public String getPropertyName() {
        return propertyName;
    }

    protected void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the list of values that determine whether a DomainObject belongs in this set */


    @Transient
   /** Get the list of values that determine whether a DomainObject belongs in this set */
    public List<String> getValues() {
        List<String> values = new LinkedList<String>();
        for (PropertyValueSetEntry value : propertyValueEntries) {
            values.add(value.getValue());
        }
        return values;
    }
    
    @OneToMany(mappedBy = "propertyValueSet", cascade = CascadeType.ALL)
    public Set<PropertyValueSetEntry> getPropertyValueEntries() {
        return propertyValueEntries;
    }

    public void setPropertyValueEntries(Set<PropertyValueSetEntry> propertyValueEntries) {
        this.propertyValueEntries = propertyValueEntries;
    }

    /**
     * Add an value that allows entry into this set
     * NOTE: the filterValue is converted to a String with toString()
     **/
    public boolean defineValue(Object filterValue) {
        return propertyValueEntries.add(new PropertyValueSetEntry(this, filterValue.toString()));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Evaluate whether the object belongs in this Set.
     * Evaluation is by string value equality
     **/
    @Transient
    public boolean isInSet(Object object) {
        final String value = lookupPropertyValue(object, propertyName);
        return FilterTools.matchesAny(propertyValueEntries, new Filter<PropertyValueSetEntry>(){
            public boolean accept(PropertyValueSetEntry filterValue) {
                return filterValue.getValue().equals(value);
            }
        });
    }

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

    /** Get the calculated name of this union */
    @Transient
    public String getRuleName() {
        return "'"+propertyName+"' in ["+ StringUtils.join(propertyValueEntries.iterator(), ",")+"]";
    }

}
