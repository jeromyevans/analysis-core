package com.blueskyminds.analysis.basic;

import com.blueskyminds.analysis.basic.statistics.LongAdapter;
import com.blueskyminds.analysis.basic.statistics.BasicStats;
import com.blueskyminds.analysis.basic.statistics.ComputeAdapter;
import com.blueskyminds.analysis.basic.AnalysisTools;

import java.math.BigDecimal;

import junit.framework.TestCase;

/**
 * Unit tests for the AnalysisTools class
 *
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestAnalysisTools extends TestCase {

    public void testAnalysis() {
        Long[] values = {10L, 20L, 30L, 40L, 50L};

        BasicStats result = AnalysisTools.computeStats(values, new LongAdapter());

        assertNotNull(result);
        assertEquals(new BigDecimal(30), result.getMean());
    }

    private class TestNode {
        Long value;
        String text;

        public TestNode(Long value, String text) {
            this.value = value;
            this.text = text;
        }


        public Long getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    /** Demonstrates that an adapter can  be used to perform the analysis on any object */
    public void testAnalysisWithObjects() {
        TestNode[] values = {
                new TestNode(10L, "A"),
                new TestNode(20L, "B"),
                new TestNode(30L, "C"),
                new TestNode(40L, "D"),
                new TestNode(50L, "E")};

        BasicStats result = AnalysisTools.computeStats(values, new ComputeAdapter() {

            public BigDecimal valueOf(Object object) {
                return new BigDecimal(((TestNode) object).getValue());
            }
        });

        assertNotNull(result);
        assertEquals(new BigDecimal(30), result.getMean());
    }
}
