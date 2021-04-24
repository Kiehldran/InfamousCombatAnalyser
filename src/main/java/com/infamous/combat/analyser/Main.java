package com.infamous.combat.analyser;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.infamous.combat.analyser.client.WCLogsClient;
import com.infamous.combat.analyser.client.bo.character.ReportBo;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final int NAXXRAMAS_ZONE_ID = 1006;
    private static final String SERVER_GEHENNAS = "Gehennas";
    private static final String REGION_EUROPE = "eu";

    public static void main(String[] args) throws IOException {
        Injector injector = Guice.createInjector(new BasicModule());
        WCLogsClient wcLogsClient = injector.getInstance(WCLogsClient.class);

        List<ReportBo> recentReportDtos = wcLogsClient.getRecentReports("Kiehl", SERVER_GEHENNAS, REGION_EUROPE).stream()
                .map(ReportBo::new)
                .filter(reportBo -> reportBo.getZoneBo().getId() == NAXXRAMAS_ZONE_ID)
                .collect(Collectors.toList());

//        List<ReportDto> recentNaxxramasReportDtos = wcLogsClient.getRecentReports("Kiehl", SERVER_GEHENNAS, REGION_EUROPE).stream()
//                .filter(reportDto -> reportDto.getZoneDto().getId() == NAXXRAMAS_ZONE_ID)
//                .collect(Collectors.toList());

        System.out.println(recentReportDtos);
    }

}
