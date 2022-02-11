package com.skyfalling.mousika;


import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.suite.RuleScenario;
import com.skyfalling.mousika.suite.RuleLoader;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SimpleRuleLoader implements RuleLoader {
    private List<RuleDefinition> rules;
    private List<UdfDefinition> udfs;
    private List<RuleScenario> ruleSets;

    @Override
    public List<RuleDefinition> loadRules() {
        return rules;
    }

    @Override
    public List<RuleScenario> loadScenarios() {
        return ruleSets;
    }

    @Override
    public List<UdfDefinition> loadUdfs() {
        return udfs;
    }
}
