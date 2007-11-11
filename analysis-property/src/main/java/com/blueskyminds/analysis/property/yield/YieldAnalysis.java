package com.blueskyminds.analysis.property.yield;

import com.blueskyminds.framework.AbstractDomainObject;

import javax.persistence.Entity;
import javax.persistence.Embedded;
import javax.persistence.Basic;
import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * A persistent entity that contains yield analysis summary data
 *
 * Date Started: 16/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class YieldAnalysis extends AbstractDomainObject {

    private YieldAnalysisDescriptor descriptor;
    private BigDecimal yieldOnMeans;
    private BigDecimal yieldOnMedians;
    private BigDecimal yieldOnModes;
    
    // ------------------------------------------------------------------------------------------------------

    public YieldAnalysis(YieldAnalysisDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Default constructor for ORM */
    protected YieldAnalysis() {
    }

    // ------------------------------------------------------------------------------------------------------

    @Embedded
    public YieldAnalysisDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(YieldAnalysisDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Basic
    @Column(name="YieldOnMeans")
    public BigDecimal getYieldOnMeans() {
        return yieldOnMeans;
    }

    public void setYieldOnMeans(BigDecimal yieldOnMeans) {
        this.yieldOnMeans = yieldOnMeans;
    }

    @Basic
    @Column(name="YieldOnMedians")
    public BigDecimal getYieldOnMedians() {
        return yieldOnMedians;
    }

    public void setYieldOnMedians(BigDecimal yieldOnMedians) {
        this.yieldOnMedians = yieldOnMedians;
    }

    @Basic
    @Column(name="YieldOnModes")
    public BigDecimal getYieldOnModes() {
        return yieldOnModes;
    }

    public void setYieldOnModes(BigDecimal yieldOnModes) {
        this.yieldOnModes = yieldOnModes;
    }
}
