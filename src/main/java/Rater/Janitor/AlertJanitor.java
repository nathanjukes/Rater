package Rater.Janitor;

import Rater.Repositories.AlertsRepository;
import Rater.Services.AlertsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
@Component
@Configuration
@EnableScheduling
public class AlertJanitor {
    private static final Logger log = LogManager.getLogger(AlertJanitor.class);

    private AlertsRepository alertsRepository;
    private AlertsService alertsService;

    @Autowired
    public AlertJanitor(AlertsRepository alertsRepository, AlertsService alertsService) {
        this.alertsRepository = alertsRepository;
        this.alertsService = alertsService;
    }

    @Scheduled(fixedRate = 20000) // every 20 seconds
    public void alertMonitoring() {
        final Date timestampMinuteAgo = Date.from(Instant.now().minusSeconds(60));
        final Date timestamp = Date.from(Instant.now());

        log.info("Getting alerts between {} - {}", timestampMinuteAgo, timestamp);

        List<Object[]> apiAlerts = alertsRepository.getApiAlertData(timestampMinuteAgo, timestamp);
        List<Object[]> userAlerts = alertsRepository.getUserAlertData(timestampMinuteAgo, timestamp);

        alertsService.saveApiAlerts(apiAlerts, timestampMinuteAgo, timestamp);
        alertsService.saveUserAlerts(userAlerts, timestampMinuteAgo, timestamp);
    }
}
