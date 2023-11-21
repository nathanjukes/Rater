package Rater.Models.API.Rules;

import static Rater.Models.API.Rules.CustomRuleType.basic;

public class BaseRule implements Rule {
    private int useLimit;

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
    public double getPermittedRate() {
        return 0;
    }
}
