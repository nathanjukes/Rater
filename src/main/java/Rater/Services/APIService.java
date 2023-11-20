package Rater.Services;

import Rater.Exceptions.UnauthorizedException;
import Rater.Models.API.*;
import Rater.Models.Org.Org;
import Rater.Repositories.APIRepository;
import Rater.Repositories.IdRuleRepository;
import Rater.Repositories.IpRuleRepository;
import Rater.Repositories.RoleRuleRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class APIService {
    private static final Logger log = LogManager.getLogger(APIService.class);

    private final APIRepository apiRepository;
    private final IdRuleRepository idRuleRepository;
    private final IpRuleRepository ipRuleRepository;
    private final RoleRuleRepository roleRuleRepository;
    private final ServiceService serviceService;

    @Autowired
    public APIService(APIRepository apiRepository, IdRuleRepository idRuleRepository, IpRuleRepository ipRuleRepository, RoleRuleRepository roleRuleRepository, ServiceService serviceService) {
        this.apiRepository = apiRepository;
        this.idRuleRepository = idRuleRepository;
        this.ipRuleRepository = ipRuleRepository;
        this.roleRuleRepository = roleRuleRepository;
        this.serviceService = serviceService;
    }

    public Optional<API> getAPI(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return apiRepository.findById(id);
    }

    public Optional<List<API>> getAPIs() {
        return Optional.of(apiRepository.findAll());
    }

    public Optional<List<API>> getAPIs(UUID orgId, UUID appId, UUID serviceId) {
        if (appId != null && serviceId != null) {
            return apiRepository.findByOrgId(orgId, appId, serviceId);
        } else if (appId == null && serviceId != null) {
            return apiRepository.findAPIsByServiceId(orgId, serviceId);
        } else if (appId != null && serviceId == null) {
            return apiRepository.findAPIsByAppId(orgId, appId);
        }

        return apiRepository.findByOrgId(orgId);
    }

    public Optional<API> createAPI(APICreateRequest apiCreateRequest, Org org) throws UnauthorizedException {
        Optional<Rater.Models.Service.Service> service = serviceService.getService(apiCreateRequest.getServiceId());

        if (service.isEmpty() || !service.map(s -> s.getOrgId()).get().equals(org.getId())) {
            log.info("API Create Denied, Invalid Service: " + apiCreateRequest.toString());
            throw new UnauthorizedException();
        }

        API api = new API(apiCreateRequest.getName(), 10, service.orElseThrow(), apiCreateRequest.getHttpMethod(), org);
        return Optional.of(apiRepository.save(api));
    }

    public Optional<Rule> createAPIRule(RuleCreateRequest ruleCreateRequest, Org org) throws UnauthorizedException {
        Optional<API> api = getAPI(ruleCreateRequest.getApiId());

        if (api.isEmpty() || !api.map(API::getOrgId).get().equals(org.getId())) {
            log.info("API Rule Create Denied, Invalid Org: " + ruleCreateRequest);
            throw new UnauthorizedException();
        }

        if (ruleCreateRequest.getRuleType().equals(RuleType.id)) {
            IdRule rule = IdRule.from(ruleCreateRequest, api.orElseThrow());
            return Optional.of(idRuleRepository.save(rule));
        } else if (ruleCreateRequest.getRuleType().equals(RuleType.ip)){
            IpRule rule = IpRule.from(ruleCreateRequest, api.orElseThrow());
            return Optional.of(ipRuleRepository.save(rule));
        } else {
            RoleRule rule = RoleRule.from(ruleCreateRequest, api.orElseThrow());
            return Optional.of(roleRuleRepository.save(rule));
        }
    }

    public Optional<API> searchAPI(String apiName, String fullApi, HttpMethod httpMethod, UUID serviceId, Org org) {
        // api name + service id pair is unique so should be searchable
        Optional<API> api = apiRepository.searchApiByFullName(fullApi, httpMethod.toString(), serviceId, org.getId());

        if (api.isEmpty()) {
            return apiRepository.searchApiByName(apiName, httpMethod.toString(), serviceId, org.getId());
        }

        return api;
    }

    public Optional<? extends Rule> getRule(RuleGetRequest ruleGetRequest, Org org) throws UnauthorizedException {
        Optional<API> api = getAPI(ruleGetRequest.getApiId());

        if (api.isEmpty() || !api.map(API::getOrgId).get().equals(org.getId())) {
            log.info("API Rule Get Denied, Invalid Org: " + ruleGetRequest);
            throw new UnauthorizedException();
        }

        switch (ruleGetRequest.getType()) {
            case id:
                return idRuleRepository.findByUserIdAndApiId(ruleGetRequest.getData(), api.map(API::getId).orElseThrow());
            case ip:
                return ipRuleRepository.findByUserIp(ruleGetRequest.getData());
            case role:
                return roleRuleRepository.findByRole(ruleGetRequest.getData());
        }
        return Optional.empty();
    }

    public Optional<? extends Rule> searchRule(RuleSearchRequest ruleSearchRequest, Org org) throws UnauthorizedException {
        RuleSearchQuery ruleSearchQuery = RuleSearchQuery.from(ruleSearchRequest);

        // Get API first
        Optional<API> api = searchAPI(
                ruleSearchQuery.getApiName(),
                ruleSearchQuery.getFullApi(),
                ruleSearchQuery.getHttpMethod(),
                ruleSearchQuery.getServiceId(),
                org
        );

        if (api.isEmpty() || !api.map(API::getOrgId).get().equals(org.getId())) {
            log.info("API Rule Search Denied: " + ruleSearchRequest.toString());
            throw new UnauthorizedException();
        }

        switch (ruleSearchRequest.getType()) {
            case id:
                return idRuleRepository.findByUserIdAndApiId(ruleSearchRequest.getData(), api.map(API::getId).orElseThrow());
            case ip:
                return ipRuleRepository.findByUserIp(ruleSearchRequest.getData());
            case role:
                return roleRuleRepository.findByRole(ruleSearchRequest.getData());
        }

        return Optional.empty();
    }

    public void deleteAPI(UUID id, Org org) {
        apiRepository.deleteByIdAndOrgId(id, org.getId());
    }

    public APIStatus getAPIStatus(API api) {
        return new APIStatus(api, 10);
    }
}
