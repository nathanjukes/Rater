package Rater.Services;

import Rater.Exceptions.UnauthorizedException;
import Rater.Models.API.API;
import Rater.Models.API.Rules.*;
import Rater.Models.Org.Org;
import Rater.Repositories.IdRuleRepository;
import Rater.Repositories.IpRuleRepository;
import Rater.Repositories.RoleRuleRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class APIRuleService {
    private static final Logger log = LogManager.getLogger(APIRuleService.class);

    private final IdRuleRepository idRuleRepository;
    private final IpRuleRepository ipRuleRepository;
    private final RoleRuleRepository roleRuleRepository;

    @Autowired
    public APIRuleService(IdRuleRepository idRuleRepository, IpRuleRepository ipRuleRepository, RoleRuleRepository roleRuleRepository) {
        this.idRuleRepository = idRuleRepository;
        this.ipRuleRepository = ipRuleRepository;
        this.roleRuleRepository = roleRuleRepository;
    }

    public Optional<? extends Rule> getRule(RuleGetRequest ruleGetRequest, API api, Org org) throws UnauthorizedException {
        if (!api.getOrgId().equals(org.getId())) {
            log.info("API Rule Get Denied, Invalid Org: " + ruleGetRequest);
            throw new UnauthorizedException();
        }

        return findRule(ruleGetRequest.getType(), ruleGetRequest.getData(), api);
    }

    private Optional<? extends Rule> findRule(RuleType ruleType, String data, API api) {
        Optional<? extends Rule> r = Optional.empty();

        switch (ruleType) {
            case id:
                r = idRuleRepository.findByUserIdAndApiId(data, api.getId());
                break;
            case ip:
                r = ipRuleRepository.findByUserIpAndApiId(data, api.getId());
                break;
            case role:
                r = roleRuleRepository.findByRoleAndApiId(data, api.getId());
                break;
        }

        // Resort to base limit if a custom rule does not exist
        if (r == null || r.isEmpty()) {
            r = Optional.ofNullable(api.getBaseRule());
        }

        return r;
    }

    public Optional<Rule> createAPIRule(RuleCreateRequest ruleCreateRequest, API api, Org org) throws UnauthorizedException {
        if (!api.getOrgId().equals(org.getId())) {
            log.info("API Rule Create Denied, Invalid Org: " + ruleCreateRequest);
            throw new UnauthorizedException();
        }

        if (ruleCreateRequest.getRuleType().equals(RuleType.id)) {
            IdRule rule = IdRule.from(ruleCreateRequest, api);
            return Optional.of(idRuleRepository.save(rule));
        } else if (ruleCreateRequest.getRuleType().equals(RuleType.ip)) {
            IpRule rule = IpRule.from(ruleCreateRequest, api);
            return Optional.of(ipRuleRepository.save(rule));
        } else {
            RoleRule rule = RoleRule.from(ruleCreateRequest, api);
            return Optional.of(roleRuleRepository.save(rule));
        }
    }

    public Optional<SearchRuleResponse> searchRule(RuleSearchRequest ruleSearchRequest, RuleSearchQuery ruleSearchQuery, API api, Org org) throws UnauthorizedException {
        if (!api.getOrgId().equals(org.getId())) {
            log.info("API Rule Search Denied: " + ruleSearchRequest.toString());
            throw new UnauthorizedException();
        }

        return Optional.of(new SearchRuleResponse(findRule(ruleSearchRequest.getType(), ruleSearchQuery.getData(), api), Optional.ofNullable(api)));
    }

    public void deleteRule(IdRule r) {
        idRuleRepository.delete(r);
    }

    public void deleteRule(IpRule r) {
        ipRuleRepository.delete(r);
    }

    public void deleteRule(RoleRule r) {
        roleRuleRepository.delete(r);
    }

    public void tryDeleteRule(UUID id) {
        idRuleRepository.deleteById(id);
        ipRuleRepository.deleteById(id);
        roleRuleRepository.deleteById(id);
    }
}
