package com.blueskyminds.framework.analysis;

import com.blueskyminds.framework.test.BaseTestCase;
import com.thoughtworks.xstream.XStream;

/**
 * Methods to get familiar with XStream (de)serialization
 *
 * Date Started: 1/09/2006
 *
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestXStream extends BaseTestCase {

    public TestXStream(String string) {
        super(string);
    }
    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestXStream with default attributes
     */
    private void init() {
    }

    public class Bean {

        private String name;
        private String value;

        public Bean(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final Bean bean = (Bean) o;

            if (name != null ? !name.equals(bean.name) : bean.name != null) return false;
            if (value != null ? !value.equals(bean.value) : bean.value != null) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = (name != null ? name.hashCode() : 0);
            result = 29 * result + (value != null ? value.hashCode() : 0);
            return result;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    public void testXStream() {
        XStream xStream = new XStream();
        Bean testBean = new Bean("blue", "sky");
        String xml = xStream.toXML(testBean);
        assertNotNull(xml);
        System.out.println(xml);
        Bean newBean = (Bean) xStream.fromXML(xml);
        assertNotNull(newBean);
        assertTrue(newBean.equals(testBean));
    }
}
