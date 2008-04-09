package com.blueskyminds.analysis.property.classification;

import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.landmine.core.property.Premise;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.PrintStream;

/**
 * A mapping of Premises to Regions
 * Used to persist the mapping data for use in queries
 * Mapping must be recalculated if the region boundaries are changed
 *
 * Date Started: 27/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@Table(name="landmine_PremiseRegionMap")
public class PremiseRegionMap extends AbstractDomainObject {

    private Premise premise;
    private RegionHandle region;

    public PremiseRegionMap(Premise premise, RegionHandle region) {
        this.premise = premise;
        this.region = region;
    }

    /** Default constructor for ORM */
    protected PremiseRegionMap() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PropertyRegionMap with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the property that this entry belongs to */
    @ManyToOne
    @JoinColumn(name="PremiseId")
    public Premise getPremise() {
        return premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    /** Get the Region that this property is mapped to by this entry */
    @ManyToOne
    @JoinColumn(name="RegionId")
    public RegionHandle getRegion() {
        return region;
    }

    public void setRegion(RegionHandle region) {
        this.region = region;
    }

    public void print(PrintStream out) {
        out.println(getIdentityName()+" ("+ premise.getIdentityName()+":"+region.getIdentityName()+")");
    }
}
