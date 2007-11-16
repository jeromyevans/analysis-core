package com.blueskyminds.analysis;

import com.blueskyminds.analysis.core.sets.AggregateSetGroup;
import com.blueskyminds.analysis.core.sets.dao.AggregateSetDAO;
import com.blueskyminds.analysis.core.statistics.StatisticsEngine;
import com.blueskyminds.analysis.property.classification.PremiseAggregateSetSpoolerTask;
import com.blueskyminds.analysis.property.classification.PremiseRegionSpoolerTask;
import com.blueskyminds.analysis.property.yield.YieldEngine;
import com.blueskyminds.enterprise.region.service.RegionService;
import com.blueskyminds.framework.ServiceLocator;
import com.blueskyminds.framework.persistence.spooler.EntitySpooler;
import com.blueskyminds.framework.tasks.service.TaskingService;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.dao.PropertyDAO;

import javax.persistence.EntityManager;

/**
 * A ServiceLocator for the Analysis framework
 *
 * Date Started: 4/09/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AnalysisServiceImpl extends ServiceLocator implements AnalysisService{

    private AggregateSetDAO aggregateSetDAO;
    private RegionService regionService;
    private PropertyDAO propertyDAO;
    private EntityManager em;

    private TaskingService taskingService;

    /** Instance of the StatisticsEngine */
    private StatisticsEngine statisticsEngine;
    /** Instance of the YieldEngine */
    private YieldEngine yieldEngine;

    public AnalysisServiceImpl() {
    }

    public AnalysisServiceImpl(AggregateSetDAO aggregateSetDAO, RegionService regionService, PropertyDAO propertyDAO, EntityManager em) {
        this.aggregateSetDAO = aggregateSetDAO;
        this.regionService = regionService;
        this.propertyDAO = propertyDAO;
        this.em = em;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AnalysisServiceImpl with default attributes
     */
//    protected void templateInit() {
//        statisticsEngine = new StatisticsEngine();
//        yieldEngine = new YieldEngine();
//    }

    // ------------------------------------------------------------------------------------------------------

    /** Get an instance of the StatisticsEngine */
    public static StatisticsEngine statisticsEngine() {
        return instance(AnalysisServiceImpl.class).statisticsEngine;
    }
//
//    /** Get an instance of the YieldEngine */
    public static YieldEngine yieldEngine() {
        return instance(AnalysisServiceImpl.class).yieldEngine;
    }

    /**
     * Lookup an AggregateSetGroup by its unique key
     * @param key
     * @return
     */
    public AggregateSetGroup findAggregateSetGroup(String key) {
        return aggregateSetDAO.findAggregateSetGroup(key);
    }

    /**
     * In one long blocking iteration, calculates all the mappings between premises to aggregatesets in the specified group
     *
     * @param aggregateSetGroupKey
     */
    public void recalculatePremiseToAggregateSetMaps(String aggregateSetGroupKey) {
        AggregateSetGroup group = findAggregateSetGroup(aggregateSetGroupKey);

        EntitySpooler<Premise> entitySpooler = new EntitySpooler<Premise>(propertyDAO, new PremiseAggregateSetSpoolerTask(em, group));
        entitySpooler.start();
    }

    /**
     * In one long blocking iteration, calculates all the mappings between premises and regions
     *
     */
    public void recalculatePremiseToRegionMaps() {
        EntitySpooler<Premise> entitySpooler = new EntitySpooler<Premise>(propertyDAO, new PremiseRegionSpoolerTask(em, regionService));

        entitySpooler.start();
    }


    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void setPropertyDAO(PropertyDAO propertyDAO) {
        this.propertyDAO = propertyDAO;
    }

    public void setRegionService(RegionService regionService) {
        this.regionService = regionService;
    }

    public void setAggregateSetDAO(AggregateSetDAO aggregateSetDAO) {
        this.aggregateSetDAO = aggregateSetDAO;
    }
}
