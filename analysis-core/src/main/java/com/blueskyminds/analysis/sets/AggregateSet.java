package com.blueskyminds.analysis.sets;

import com.blueskyminds.framework.DomainObject;
import com.blueskyminds.framework.AbstractDomainObject;

import javax.persistence.*;

/**
 * Interface to a rule that determines whether a DomainObject belongs in a specific set
  *
 * Date Started: 19/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AggregateSet<T extends DomainObject> extends AbstractDomainObject {

    /** Get the calculated name of this set */
    @Transient
    public abstract String getName();

    // ------------------------------------------------------------------------------------------------------

    /** Evaluate whether the domain object is in this Set */
    @Transient
    public abstract boolean isInSet(T domainObject);

    public String toString() {
        return getIdentityName();
    }

}
