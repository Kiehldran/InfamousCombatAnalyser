package com.infamous.combat.analyser.client.dto.graphql.character;

import lombok.Data;

import java.util.List;

@Data
public class RecentReports {
    List<Report> reports;
}
