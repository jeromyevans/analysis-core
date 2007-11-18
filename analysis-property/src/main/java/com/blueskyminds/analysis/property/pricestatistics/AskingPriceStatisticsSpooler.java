package com.blueskyminds.analysis.property.pricestatistics;

import com.blueskyminds.analysis.core.statistics.StatisticsEngine;
import com.blueskyminds.analysis.property.PriceAnalysisSampleDescriptor;
import com.blueskyminds.analysis.property.dao.AdvertisementAnalysisDAO;
import com.blueskyminds.framework.persistence.spooler.PageSpooler;
import com.blueskyminds.landmine.core.property.AskingPrice;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * A specialization of a PageSpooler that pages through asking price data based on settings in a PriceAnalysisSampleDescriptor and passes them
 *  to the statistics task for analysis.
 *
 * Date Started: 16/11/2007
 * <p/>
 * History:
 */
public class AskingPriceStatisticsSpooler extends PageSpooler<AskingPrice> {

    /**
     * Setup a specialised PageSpooler that loads AskingPrice data
     *
     * @param descriptor
     * @param advertisementAnalysisDAO
     * @param statisticsEngine
     * @param em
     */
    public AskingPriceStatisticsSpooler(PriceAnalysisSampleDescriptor descriptor, AdvertisementAnalysisDAO advertisementAnalysisDAO, StatisticsEngine statisticsEngine, EntityManager em) {

        AskingPriceStatisticsTask spoolerTask = new AskingPriceStatisticsTask(em, descriptor, statisticsEngine);

        Date startDate = descriptor.getMonthOfYear().subtract(descriptor.getInterval()).toDate();
        Date endDate = descriptor.getMonthOfYear().add(descriptor.getInterval()).toDate();

        AskingPriceDataSource dataSource = (AskingPriceDataSource) descriptor.getDataSource();

        setPager(advertisementAnalysisDAO.pageMostRecentPrice(dataSource.getPropertyAdvertisementType(), descriptor.getRegion(), descriptor.getAggregateSet(), startDate, endDate));
        setTask(spoolerTask);

        addListener(spoolerTask);
    }
}
