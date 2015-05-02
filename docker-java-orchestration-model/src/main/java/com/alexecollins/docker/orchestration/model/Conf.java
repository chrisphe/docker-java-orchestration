package com.alexecollins.docker.orchestration.model;


import lombok.Data;

import java.util.*;

@Data
public class Conf {

    private List<String> tags = new ArrayList<>();
    /**
     * Information about the container. This is intended to provide a place for container only properties.
     */
    private ContainerConf container = new ContainerConf();
    private List<Link> links = new ArrayList<>();
    private Packaging packaging = new Packaging();
    /**
     * E.g. "8080" or "8080 8080" where the former is the exposed port and the latter the container port.
     */
    private List<String> ports = new ArrayList<>();
    private int sleep = 0;
    private boolean logOnFailure = true;
    private int maxLogLines = 10; // same as unix tail command
    private List<Id> volumesFrom = new ArrayList<>();
    private HealthChecks healthChecks = new HealthChecks();
    private Map<String, String> env = new HashMap<>();
    private Map<String, String> volumes = new HashMap<>();
    private boolean enabled = true;
    private boolean exposeContainerIp = true;
    private String image;

    public boolean hasTag() {
        return tags != null && !tags.isEmpty();
    }

    public boolean hasImage() {
        return image != null;
    }

    /**
     * @return Returns the first tag of a list of tags
     */
    public String getTag() {
        return tags.get(0);
    }

    public void setTag(String tag) {
        setTags(Collections.singletonList(tag));
    }
}
