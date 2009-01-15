package com.blueskyminds.analysis.core.sets.dao;

import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.core.sets.AggregateSetGroup;
import com.blueskyminds.analysis.core.sets.PropertyValueSet;
import com.blueskyminds.analysis.core.sets.UnionSet;
import com.blueskyminds.analysis.core.test.JPATestCase;

/**
 * Date Started: 15/11/2007
 * <p/>
 * History:
 */
public class AggregateSetDAOTest extends JPATestCase {

    private static final String PERSISTENCE_UNIT_NAME = "TestAnalysisPersistenceUnit";

    private AggregateSetDAO aggregateSetDAO;
    private static final String TEST_GROUP_KEY = "TestGroup";

    public AggregateSetDAOTest() {
        super(PERSISTENCE_UNIT_NAME);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        AggregateSet dog = new PropertyValueSet("dog", "type", "dog");
        AggregateSet cat = new PropertyValueSet("cat", "type", "cat");
        AggregateSet animal = new UnionSet("animals", dog, cat);
        AggregateSetGroup testGroup = new AggregateSetGroup(TEST_GROUP_KEY);
        testGroup.includeSet(animal);

        em.persist(testGroup);
        em.flush();

        aggregateSetDAO = new AggregateSetDAO(em);
    }

    public void testFindAggregateSetGroupByName() {
        AggregateSetGroup testGroup = aggregateSetDAO.lookupAggregateSetGroup(TEST_GROUP_KEY);
        assertNotNull(testGroup);        
    }

    public void testFindAggregateSetByGroupAndKey() {
        AggregateSet animal = aggregateSetDAO.lookupAggregateSet(TEST_GROUP_KEY, "animals");
        assertNotNull(animal);
    }
}
