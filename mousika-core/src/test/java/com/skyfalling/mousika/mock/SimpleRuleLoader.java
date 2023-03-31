package com.skyfalling.mousika.mock;

import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.suite.RuleLoader;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SimpleRuleLoader implements RuleLoader {
    private List<RuleDefinition> rules;
    private List<UdfDefinition> udfs;

    @Override
    public List<RuleDefinition> loadRules() {
        return rules;
    }

    @Override
    public List<UdfDefinition> loadUdfs() {
        return udfs;
    }

}
