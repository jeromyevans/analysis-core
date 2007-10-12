package com.blueskyminds.analysis.sets;

import com.blueskyminds.framework.DomainObject;
import org.hibernate.Criteria;

/**
 * SetCriteria is an API for retrieving entities by composing a SetCriterion objects.
 *
 * The distinguishing factors of this API compared to QueryBuilder are that:
 *    - is that associations between entities do not need to be navigatable
 *    - operations to retrieve the set may be performed on the server and/or within the database
 *    - the criteria may result in multiple concurrent database operations
 *
 * Contains criteria for defining a set of Domain Objects
 *
 * Date Started: 5/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class SetCriteria<T extends DomainObject> {

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the SetCriteria with default attributes
     */
    private void init() {
        Criteria c;
    }

    // ------------------------------------------------------------------------------------------------------
}
