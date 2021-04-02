package com.infamous.combat.analyser;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.infamous.combat.analyser.client.WCLogsClient;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Injector injector = Guice.createInjector(new BasicModule());
        WCLogsClient wcLogsClient = injector.getInstance(WCLogsClient.class);

        wcLogsClient.getRecentReports();

    }

}
