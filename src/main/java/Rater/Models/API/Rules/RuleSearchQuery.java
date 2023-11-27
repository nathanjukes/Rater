package Rater.Models.API.Rules;

import Rater.Models.API.HttpMethod;

import java.util.UUID;

public class RuleSearchQuery {
    private String data;
    private HttpMethod httpMethod;
    private String apiName;
    private UUID serviceId;
    private String fullApi;

    public RuleSearchQuery(String data, HttpMethod httpMethod, String apiName, UUID serviceId, String fullApi) {
        this.data = data;
        this.httpMethod = httpMethod;
        this.apiName = apiName;
        this.serviceId = serviceId;
        this.fullApi = fullApi;
    }

    public String getData() {
        return data;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getApiName() {
        return apiName;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public String getFullApi() {
        return fullApi;
    }

    public static RuleSearchQuery from(RuleSearchRequest ruleSearchRequest) {
        // apiPath = e.g. "GET:/users/{id}"
        String[] splitData = ruleSearchRequest.getApiPath().split(":/");
        HttpMethod httpMethod = HttpMethod.valueOf(splitData[0]);

        // Does not account for /users?id=10 but this shouldn't be permissable anyways
        String[] subSplitData = splitData[1].split("/");
        String apiName = subSplitData[0];

        return new RuleSearchQuery(ruleSearchRequest.getData(), httpMethod, apiName, ruleSearchRequest.getServiceId(), splitData[1]);
    }
}
