package com.alexecollins.docker.orchestration.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ConfTest {
    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());
    private Conf conf;

    @Before
    public void setUp() throws Exception {
        conf = MAPPER.readValue(getClass().getResource("/conf.yml"), Conf.class);
    }

    @Test
    public void test() throws Exception {

        assertNotNull(conf.getTag());
        assertTrue(conf.hasTag());
        assertNotNull(conf.getLinks());
        assertNotNull(conf.getPackaging());
        assertNotNull(conf.getPorts());
        assertNotNull(conf.getVolumesFrom());

        assertEquals(new Link("foo:bar"), conf.getLinks().get(0));

        assertThat(conf.isLogOnFailure(), is(true));
        assertThat(conf.getMaxLogLines(), is(123));
    }

    @Test
    public void containerConf() throws Exception {
        ContainerConf container = conf.getContainer();
        assertTrue(container.hasName());
        assertEquals("theName", container.getName());
    }

    @Test
    public void enabled() throws Exception {
        assertEquals(true, conf.isEnabled());
    }

    @Test
    public void imageName() throws Exception {
        assertEquals("theImage", conf.getImage());
    }
}
