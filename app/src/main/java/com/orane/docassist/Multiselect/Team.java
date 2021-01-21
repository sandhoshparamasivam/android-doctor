package com.orane.docassist.Multiselect;

public class Team {
    private String teamName;
    private String id;

    public  Team(String name, String id_val)
    {
        teamName = name;
        id = id_val;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getId() {
        return id;
    }
}
