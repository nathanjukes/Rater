package Rater.Models.Org;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrgHealth {
    private String name;

    public OrgHealth(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static OrgHealth from(Org org) {
        return new OrgHealth(org.getName());
    }
}
