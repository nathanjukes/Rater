package Rater.Services;

import Rater.Controllers.OrgController;
import Rater.Exceptions.BadRequestException;
import Rater.Models.Alerts.OrgAlert;
import Rater.Models.Alerts.OrgAlertConfig;
import Rater.Models.Alerts.OrgAlertUpdateRequest;
import Rater.Models.Alerts.UserAlert;
import Rater.Models.Metrics.UserUsageMetric;
import Rater.Models.Org.Org;
import Rater.Repositories.AlertsRepository;
import Rater.Repositories.OrgAlertConfigRepository;
import Rater.Repositories.UserAlertsRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class AlertsService {
    private static final Logger log = LogManager.getLogger(AlertsService.class);

    private final UserAlertsRepository userAlertsRepository;
    private final OrgAlertConfigRepository orgAlertConfigRepository;
    private final AlertsRepository alertsRepository;
    private final MetricsService metricsService;

    @Autowired
    public AlertsService(UserAlertsRepository userAlertsRepository, OrgAlertConfigRepository orgAlertConfigRepository, AlertsRepository alertsRepository, MetricsService metricsService) {
        this.userAlertsRepository = userAlertsRepository;
        this.orgAlertConfigRepository = orgAlertConfigRepository;
        this.alertsRepository = alertsRepository;
        this.metricsService = metricsService;
    }

    public Optional<List<OrgAlert>> getAlerts(UUID orgId) {
        return alertsRepository.getOrgAlerts(orgId);
    }

    public void deleteAlert(UUID orgId, UUID alertId) {
        alertsRepository.deleteByIdAndOrgId(alertId, orgId);
    }

    public void saveApiAlerts(List<Object[]> alerts, Date lb, Date ub) {
        // [0] = orgId
        // [1] = apiId
        // [2] = denied
        // [3] = total
        for (var i : alerts) {
            OrgAlert orgAlert = new OrgAlert(
                    UUID.fromString(String.valueOf(i[0])),
                    String.valueOf(i[1]),
                    (Long) i[2],
                    (Long) i[3],
                    lb,
                    ub,
                    false
            );

            alertsRepository.save(orgAlert);
        }
    }

    public void saveUserAlerts(List<Object[]> alerts, Date lb, Date ub) {
        for (var i : alerts) {
            OrgAlert orgAlert = new OrgAlert(
                    UUID.fromString(String.valueOf(i[0])),
                    String.valueOf(i[1]),
                    (Long) i[2],
                    (Long) i[3],
                    lb,
                    ub,
                    true
            );

            alertsRepository.save(orgAlert);
        }
    }

    public void configureUserAlert(Org org, String userData) {
        userAlertsRepository.save(new UserAlert(org, userData));
    }

    public void deleteUserAlert(Org org, String userData) {
        userAlertsRepository.deleteByUserData(org.getId(), userData);
    }

    public List<UserUsageMetric> getUserAlerts(UUID orgId) throws BadRequestException {
        Optional<List<String>> trackedUsers = getTrackedUsers(orgId);
        return metricsService.getTrackedUsersMetrics(orgId, trackedUsers.orElse(Collections.emptyList()));
    }

    private Optional<List<String>> getTrackedUsers(UUID orgId) {
        return userAlertsRepository.findByOrgId(orgId);
    }

    public Optional<OrgAlertConfig> getOrgAlertSettings(Org org) {
        Optional<OrgAlertConfig> orgAlertConfig = orgAlertConfigRepository.findByOrgId(org.getId());

        if (orgAlertConfig.isPresent()) {
            return orgAlertConfig;
        }

        return Optional.of(OrgAlertConfig.from(org));
    }

    public void saveOrgAlertSettings(Org org, OrgAlertUpdateRequest orgAlertUpdateRequest) {
        Optional<OrgAlertConfig> orgAlertConfig = orgAlertConfigRepository.findByOrgId(org.getId());

        if (orgAlertConfig.isPresent()) {
            orgAlertConfig.get().updateSettings(orgAlertUpdateRequest);
        }
        orgAlertConfigRepository.save(orgAlertConfig.orElse(OrgAlertConfig.from(orgAlertUpdateRequest, org)));
    }
}
