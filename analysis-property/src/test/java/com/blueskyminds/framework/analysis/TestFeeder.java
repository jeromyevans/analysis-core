package com.blueskyminds.framework.analysis;

import com.blueskyminds.framework.test.BaseTestCase;
//import com.blueskyminds.framework.persistence.hibernate.query.HibernateCriteriaImpl;
//import org.hibernate.criterion.DetachedCriteria;
//import org.hibernate.criterion.Expression;

/**
 * The feeders load series from the database for analysis
 *
 * Date Started: 25/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Deprecated
public class TestFeeder extends BaseTestCase {

    public TestFeeder(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestFeeder with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Creates a QueryBuilder that looks up listing prices from advertisments in a  */
//    public PersistenceQuery searchByName(Region region, Interval timespan, MonthOfYear timePeriod) {
//        DetachedCriteria criteria = DetachedCriteria.forClass(PropertyAdvertisement.class);
//        criteria.add(Expression.eq("region", region));
//        return new HibernateCriteriaImpl(criteria);
//    }


//    private void testQuery() {
//
//        PropertyAnalysisTestTools.generateRandomAdvertisements(1000);
//
//        try {
//            PersistenceService gateway = getPersistenceService();
//            PersistenceSession ps = gateway.openSession();
//
//            gateway.find(PropertyAdvertisement.class, searchByName())
//            Session session = (Session) ps.getSessionImpl();
//            Query c
//            while (!lastPage) {
//                page = new HibernatePageImpl(session.createQuery("from PropertyAdvertisement"), pageNo, 20);
//                System.out.println(pageNo);
//                lastPage = !page.hasNextPage();
//                pageNo++;
//            }
//            ps.close();
//        } catch (PersistenceServiceException e) {
//            e.printStackTrace();
//            fail();
//        }
//    }

}
