package Rater.Models.API.Rules;

public interface Rule {
    int getUseLimit();
    CustomRuleType getCustomRuleType();
    double getPermittedRatePerSecond();
}
