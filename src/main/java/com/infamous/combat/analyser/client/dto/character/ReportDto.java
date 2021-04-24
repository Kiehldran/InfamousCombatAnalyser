package com.infamous.combat.analyser.client.dto.character;

import lombok.Data;

@Data
public class ReportDto {
    private String code;
    private long startTime;
    private long endTime;
    private ZoneDto zoneDto;
}
