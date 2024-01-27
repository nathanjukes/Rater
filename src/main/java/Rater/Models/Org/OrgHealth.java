package Rater.Models.Org;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class OrgHealth {
    private String name;
    private List<Object[]> metadata;

    public OrgHealth(String name, List<Object[]> metadataMetrics) {
        this.name = name;
        this.metadata = metadataMetrics;
    }

    public String getName() {
        return name;
    }

    public List<Object[]> getMetadata() {
        return metadata;
    }

    public static OrgHealth from(Org org, List<Object[]> metadataMetrics) {
        return new OrgHealth(org.getName(), metadataMetrics);
    }
}
