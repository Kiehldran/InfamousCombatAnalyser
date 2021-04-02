package com.infamous.combat.analyser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.infamous.combat.analyser.client.WCLogsClient;

public class BasicModule extends AbstractModule {
    protected void configure() {
        bind(WCLogsClient.class);
        bind(ObjectMapper.class);
    }
}
