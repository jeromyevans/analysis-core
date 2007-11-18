package com.blueskyminds.analysis;

import com.blueskyminds.analysis.core.datasource.AnalysisDataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.core.sets.AggregateSetGroup;
import com.blueskyminds.analysis.core.sets.dao.AggregateSetDAO;
import com.blueskyminds.analysis.core.statistics.StatisticsEngine;
import com.blueskyminds.analysis.property.PriceAnalysisSampleDescriptor;
import com.blueskyminds.analysis.property.classification.PremiseAggregateSetSpoolerTask;
import com.blueskyminds.analysis.property.classification.PremiseRegionSpoolerTask;
import com.blueskyminds.analysis.property.dao.AdvertisementAnalysisDAO;
import com.blueskyminds.analysis.property.dao.PropertyAnalysisDAO;
import com.blueskyminds.analysis.property.pricestatistics.AskingPriceStatisticsSpooler;
import com.blueskyminds.analysis.property.yield.YieldEngine;
import com.blueskyminds.enterprise.region.service.RegionService;
import com.blueskyminds.enterprise.regionx.RegionHandle;
import com.blueskyminds.framework.ServiceLocator;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;
import com.blueskyminds.framework.persistence.spooler.EntitySpooler;
import com.blueskyminds.framework.tasks.service.TaskingService;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.dao.PropertyDAO;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

/**
 * A ServiceLocator for the Analysis framework
 *
 * Date Started: 4/09/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AdvertisementAnalysisServiceImpl extends ServiceLocator implements AnalysisService{

    public static final String ASKING_PRICE_PRIVATE_TREATY_DATASOURCE = "AskingPricePrivateTreaty";
    public static final String ASKING_PRICE_LEASE_DATASOURCE = "AskingPriceLease";

    private AggregateSetDAO aggregateSetDAO;
    private RegionService regionService;
    private PropertyDAO propertyDAO;
    private AdvertisementAnalysisDAO advertisementAnalysisDAO;
    private EntityManager em;

    private TaskingService taskingService;

    /** Instance of the StatisticsEngine */
    private StatisticsEngine statisticsEngine;
    /** Instance of the YieldEngine */
    private YieldEngine yieldEngine;

    public AdvertisementAnalysisServiceImpl() {
    }


    public AdvertisementAnalysisServiceImpl(AggregateSetDAO aggregateSetDAO, RegionService regionService, PropertyDAO propertyDAO, AdvertisementAnalysisDAO advertisementAnalysisDAO, StatisticsEngine statisticsEngine, EntityManager em) {
        this.aggregateSetDAO = aggregateSetDAO;
        this.regionService = regionService;
        this.propertyDAO = propertyDAO;
        this.advertisementAnalysisDAO = advertisementAnalysisDAO;
        this.statisticsEngine = statisticsEngine;
        this.em = em;
    }

    /**
     * Define a new analysis data source
     *
     * @param analysisDataSource
     * @return
     */
    public AnalysisDataSource persistDataSource(AnalysisDataSource analysisDataSource) {
        em.persist(analysisDataSource);
        return analysisDataSource;
    }

    /**
     * Lookup an analysis datasource by its unique key
     *
     * @param key
     * @return
     */
    public AnalysisDataSource findDataSource(String key) {
        return new PropertyAnalysisDAO(em).lookupAnalysisDataSource(key);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AdvertisementAnalysisServiceImpl with default attributes
     */
//    protected void templateInit() {
//        statisticsEngine = new StatisticsEngine();
//        yieldEngine = new YieldEngine();
//    }

    // ------------------------------------------------------------------------------------------------------

    /** Get an instance of the StatisticsEngine */
    public static StatisticsEngine statisticsEngine() {
        return instance(AdvertisementAnalysisServiceImpl.class).statisticsEngine;
    }
//
//    /** Get an instance of the YieldEngine */
    public static YieldEngine yieldEngine() {
        return instance(AdvertisementAnalysisServiceImpl.class).yieldEngine;
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


    /**
     * Perform the analysis for the specified region over a date range for the specified aggregate sets and interval
     *
     * @param topRegion
     * @param recurseSubregions    if true, each subregion will also be analysed
     * @param aggregateSetGroup
     * @param startDate
     * @param endDate
     * @param interval
     */
    public void analyseRegion(RegionHandle topRegion, boolean recurseSubregions, AggregateSetGroup aggregateSetGroup, MonthOfYear startDate, MonthOfYear endDate, Interval interval) {
        MonthOfYear currentPeriod = startDate;
        MonthOfYear finalPeriod = endDate.subtract(interval);

        AnalysisDataSource dataSource = findDataSource(ASKING_PRICE_PRIVATE_TREATY_DATASOURCE);
        List<RegionHandle> regionList = new LinkedList<RegionHandle>();
        regionList.add(topRegion);
        if (recurseSubregions) {
            regionList.addAll(regionService.listDescendants(topRegion));
        }

        // todo: one query across all aggregate sets and startdate to enddate may be better
        for (RegionHandle region : regionList) {
            // for each period and aggregate group...
            while (currentPeriod.compareTo(finalPeriod) < 0) {
                for (AggregateSet aggregateSet : aggregateSetGroup.getAggregateSets()) {
                    PriceAnalysisSampleDescriptor descriptor = new PriceAnalysisSampleDescriptor(region, aggregateSet, interval, currentPeriod, dataSource);

                    AskingPriceStatisticsSpooler spooler = new AskingPriceStatisticsSpooler(descriptor, advertisementAnalysisDAO, statisticsEngine, em);
                    spooler.start();
                }
                currentPeriod = currentPeriod.add(1);
            }
        }
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

    public void setAdvertisementAnalysisDAO(AdvertisementAnalysisDAO advertisementAnalysisDAO) {
        this.advertisementAnalysisDAO = advertisementAnalysisDAO;
    }

    public void setStatisticsEngine(StatisticsEngine statisticsEngine) {
        this.statisticsEngine = statisticsEngine;
    }
}
