package com.infamous.combat.analyser.client.bo.character;

import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class ZoneBo {

    private static final String NAME_KEY = "name";
    private static final String ID_KEY = "id";

    private String name;
    private int id;

    public ZoneBo(LinkedHashMap zoneLinkedHashMap) {
        this.name = (String) zoneLinkedHashMap.get(NAME_KEY);
        this.id = (int) zoneLinkedHashMap.get(ID_KEY);
    }
}
