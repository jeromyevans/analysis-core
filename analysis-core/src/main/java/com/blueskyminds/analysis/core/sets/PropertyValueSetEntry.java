package com.blueskyminds.analysis.core.sets;

import com.blueskyminds.framework.AbstractEntity;

import javax.persistence.*;

/**
 * A matching value for an aggregate set
 *
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@Table(name="analysis_PropertyValueSetEntry")
public class PropertyValueSetEntry extends AbstractEntity {

    private PropertyValueSet propertyValueSet;
    private String value;

    public PropertyValueSetEntry(PropertyValueSet aggregateSet, String value) {
        this.propertyValueSet = aggregateSet;
        this.value = value;
    }

    /** Default constructor for ORM */
    protected PropertyValueSetEntry() {
    }

    @ManyToOne
    @JoinColumn(name="PropertyValueSetId")
    public PropertyValueSet getPropertyValueSet() {
        return propertyValueSet;
    }

    public void setPropertyValueSet(PropertyValueSet propertyValueSet) {
        this.propertyValueSet = propertyValueSet;
    }

    @Basic
    @Column(name="Value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
