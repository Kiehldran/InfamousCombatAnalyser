package com.infamous.combat.analyser.client.dto.graphql.character;

import lombok.Data;

@Data
public class Report {
    private String code;
    private long startTime;
    private long endTime;
    private Zone zone;
}
