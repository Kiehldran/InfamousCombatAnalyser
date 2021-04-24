package com.infamous.combat.analyser.client.bo.character;

import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class ReportBo {

    private static final String CODE_KEY = "code";
    private static final String START_TIME_KEY = "startTime";
    private static final String END_TIME_KEY = "endTime";
    private static final String ZONE_KEY = "zone";

    private String code;
    private long startTime;
    private long endTime;
    private ZoneBo zoneBo;

    public ReportBo(LinkedHashMap reportLinkedHashMap) {
        this.code = (String) reportLinkedHashMap.get(CODE_KEY);
        this.startTime = (long) reportLinkedHashMap.get(START_TIME_KEY);
        this.endTime = (long) reportLinkedHashMap.get(END_TIME_KEY);
        this.zoneBo = new ZoneBo((LinkedHashMap) reportLinkedHashMap.get(ZONE_KEY));
    }
}
