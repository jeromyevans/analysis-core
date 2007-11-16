package com.blueskyminds.analysis.property.frequency;

import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.property.PriceAnalysisSampleDescriptor;
import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.landmine.core.property.PropertyTypes;

import javax.persistence.*;

/**
 * Frequency analysis of property data
 *
 * Date Started: 2/10/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
//@Entity incomplete
public class FrequencyAnalysis extends AbstractDomainObject {

    private PriceAnalysisSampleDescriptor descriptor;
    private Integer size;
    private PropertyTypes modeType;
    private Integer sizeModeType;
    private Integer modeBedrooms;
    private Integer sizeModeBedrooms;
    private Integer modeBathrooms;
    private Integer sizeModeBathrooms;
    private AggregateSet modeAggregateSet;
    private Integer sizeModeAggregateSet;

    public FrequencyAnalysis(PriceAnalysisSampleDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    /** Default constructor for ORM */
    protected FrequencyAnalysis() {

    }
    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the FrequencyAnalysis with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    @Embedded
    public PriceAnalysisSampleDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(PriceAnalysisSampleDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Basic
    @Column(name="Size")
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Embedded
    @Column(name="ModeType")
    public PropertyTypes getModeType() {
        return modeType;
    }

    public void setModeType(PropertyTypes modeType) {
        this.modeType = modeType;
    }

    @Embedded
    @Column(name="SizeModeType")
    public Integer getSizeModeType() {
        return sizeModeType;
    }

    public void setSizeModeType(Integer sizeModeType) {
        this.sizeModeType = sizeModeType;
    }

    @Basic
    @Column(name="ModeBedrooms")
    public Integer getModeBedrooms() {
        return modeBedrooms;
    }

    public void setModeBedrooms(Integer modeBedrooms) {
        this.modeBedrooms = modeBedrooms;
    }

    @Basic
    @Column(name="SizeModeBedrooms")
    public Integer getSizeModeBedrooms() {
        return sizeModeBedrooms;
    }

    public void setSizeModeBedrooms(Integer sizeModeBedrooms) {
        this.sizeModeBedrooms = sizeModeBedrooms;
    }

    @Basic
    @Column(name="ModeBathrooms")
    public Integer getModeBathrooms() {
        return modeBathrooms;
    }

    public void setModeBathrooms(Integer modeBathrooms) {
        this.modeBathrooms = modeBathrooms;
    }

    @Basic
    @Column(name="SizeModeBathrooms")
    public Integer getSizeModeBathrooms() {
        return sizeModeBathrooms;
    }

    public void setSizeModeBathrooms(Integer sizeModeBathrooms) {
        this.sizeModeBathrooms = sizeModeBathrooms;
    }

    /** Get the AggregateSet specified in this descriptor */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ModeAggregateSet")
    public AggregateSet getModeAggregateSet() {
        return modeAggregateSet;
    }

    public void setModeAggregateSet(AggregateSet modeAggregateSet) {
        this.modeAggregateSet = modeAggregateSet;
    }

    @Basic
    @Column(name="SizeAggregateSet")
    public Integer getSizeModeAggregateSet() {
        return sizeModeAggregateSet;
    }

    public void setSizeModeAggregateSet(Integer sizeModeAggregateSet) {
        this.sizeModeAggregateSet = sizeModeAggregateSet;
    }
}
