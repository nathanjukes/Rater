package Rater.Models.API.Rules;

import static Rater.Models.API.Rules.CustomRuleType.basic;

public class BaseRule implements Rule {
    private int useLimit;
    private final int timePeriod = 60;

    public BaseRule(int useLimit) {
        this.useLimit = useLimit;
    }

    @Override
    public int getUseLimit() {
        return useLimit;
    }

    @Override
    public CustomRuleType getCustomRuleType() {
        return basic;
    }

    @Override
    public double getPermittedRatePerSecond() {
        return (double) useLimit / timePeriod;
    }
}
