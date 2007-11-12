package com.blueskyminds.framework.analysis;

import com.blueskyminds.analysis.core.sets.DomainSet;
import com.blueskyminds.analysis.property.classification.PremiseRegionMap;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.framework.test.DbTestCase;
import com.blueskyminds.landmine.core.property.Premise;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;

/**
 * Unit tests for the DomainSet
 *
 * Date Started: 4/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Deprecated
public class TestDomainSets extends DbTestCase {

    private static final Log LOG = LogFactory.getLog(TestDomainSets.class);
    public TestDomainSets(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestDomainSets with default attributes
     */
    private void init() {
    }

//    <T extends DomainObject> DomainSet<T> lookupSet(Class<T> clazz) {
//        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
//        criteria.add(Expression.eq("region", region));
//        criteria.setProjection();
//        Subqueries.
//        return new HibernateCriteriaImpl(criteria);
//    }

    DomainSet<Premise> loopkupPropertiesInRegion(RegionOLD region) {
        DetachedCriteria subquery = DetachedCriteria.forClass(PremiseRegionMap.class);

        subquery.add(Restrictions.eq("region", region));   //equiv to subquery.add(Property.forName("region").eq(region));

        DetachedCriteria criteria = DetachedCriteria.forClass(Property.class);
        //subquery.add(Subqueries.propertyin()subquery);
        Collection<PremiseRegionMap> regions;

//        try {
//            PersistenceService gateway = getPersistenceService();
//
//            regions = gateway.find(PremiseRegionMap.class, new HibernateCriteriaImpl(subquery));
//
//            DebugTools.printCollection(regions);
//        } catch(PersistenceServiceException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    // ------------------------------------------------------------------------------------------------------

    public void testDomainSetLookup() {
//        new AnalysisTestTools(getPersistenceService()).initialiseRandomPropertiesWithAds(200, 2004, 2005);
//        new AnalysisTestTools(getPersistenceService()).mapPropertiesToRegions();
//        loopkupPropertiesInRegion(new AnalysisTestTools(getPersistenceService()).findRegionByName("New South Wales"));
        //AnalysisTestTools.mapPropertiesToAggregateSets();

        // lookup a set of domain objects - properties

    }
}
