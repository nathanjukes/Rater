package Rater.Services;

import Rater.Controllers.OrgController;
import Rater.Exceptions.BadRequestException;
import Rater.Models.Alerts.OrgAlertConfig;
import Rater.Models.Alerts.OrgAlertUpdateRequest;
import Rater.Models.Alerts.UserAlert;
import Rater.Models.Metrics.UserUsageMetric;
import Rater.Models.Org.Org;
import Rater.Repositories.OrgAlertConfigRepository;
import Rater.Repositories.UserAlertsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AlertsService {
    private static final Logger log = LogManager.getLogger(AlertsService.class);

    private final UserAlertsRepository userAlertsRepository;
    private final OrgAlertConfigRepository orgAlertConfigRepository;
    private final MetricsService metricsService;

    @Autowired
    public AlertsService(UserAlertsRepository userAlertsRepository, OrgAlertConfigRepository orgAlertConfigRepository, MetricsService metricsService) {
        this.userAlertsRepository = userAlertsRepository;
        this.orgAlertConfigRepository = orgAlertConfigRepository;
        this.metricsService = metricsService;
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

    public void saveOrgAlertSettings(Org org, OrgAlertUpdateRequest orgAlertUpdateRequest) {
        Optional<OrgAlertConfig> orgAlertConfig = orgAlertConfigRepository.findByOrgId(org.getId());

        if (orgAlertConfig.isPresent()) {
            orgAlertConfig.get().updateSettings(orgAlertUpdateRequest);
        }
        orgAlertConfigRepository.save(orgAlertConfig.orElse(OrgAlertConfig.from(orgAlertUpdateRequest, org)));
    }
}
