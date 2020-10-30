package edu.upenn.cis350.project;

public class UserData {

    private String username;
    private String fullName;

    private double lon;
    private double lat;

    private int zip;

    public String getName() {

        return fullName;

    }

    public void setName(String n) {

        fullName = n;

    }

    public String getUsername() {

        return username;

    }

    public void setUsername(String n) {

        username = n;

    }

    public double getLong() {

        return lon;

    }

    public double getLat() {

        return lat;

    }

    public int getZip() {

        return zip;

    }

    public void setZip(int z) {

        zip = z;

    }

}
