package com.blueskyminds.analysis.property.frequency;

import com.blueskyminds.framework.persistence.spooler.DomainObjectSpooler;
import com.blueskyminds.framework.persistence.spooler.SpoolerException;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.paging.Pager;
import com.blueskyminds.framework.persistence.paging.QueryPager;
import com.blueskyminds.framework.persistence.query.PersistenceQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Spools frequency information from actuals or advertisements into the ComputeEngine for analysis
 *
 * Date Started: 2/10/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class FrequencyAnalysisSpooler extends DomainObjectSpooler {

    private static final Log LOG = LogFactory.getLog(FrequencyAnalysisSpooler.class);
    private static final String ADVERTISEMENT_QUERY_NAME = "propertyAdvertisement.mostRecentInRegion";
    //private static final String PROPERTY_QUERY_NAME = "realProperty.inRegion";
//    public FrequencyAnalysisSpooler(EntityManager entityManager, Pager pager, Class clazz, Query query) {
//        super(entityManager, pager, clazz, query);
//    }
//
//    public FrequencyAnalysisSpooler(EntityManager entityManager, Pager pager, Class clazz) {
//        super(entityManager, pager, clazz);
//    }

    public FrequencyAnalysisSpooler(EntityManager entityManager, QueryPager pager, Query query) {
        super(entityManager, pager, query);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the FrequencyAnalysisSpooler with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------


    /**
     * Process the collection of domain objects that have been paged out of persistence
     * The persistence session is open
     */
    protected void process(List queryResults) throws SpoolerException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
