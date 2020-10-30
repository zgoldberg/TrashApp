package edu.upenn.cis350.project;

public class GlobalInfo {

    private static String createAccountUsername; // for auto-filling username if one was typed when create account was presses
    private static User loggedInUser;
    private static  User viewAccountUser;
    private static boolean editAccountMode;
    static Stats stats;
    private GlobalInfo() {

        editAccountMode = false;

    }

    private static GlobalInfo instance = new GlobalInfo();

    public static GlobalInfo getInstance() {
        return instance;
    }

    public void clearInfo() {

        createAccountUsername = "";
        editAccountMode = false;

    }

    // getters and setters after here

    public String getCreateAccountUsername() {
        return createAccountUsername;
    }

    public void setCreateAccountUsername(String s) {
        createAccountUsername = s;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User u) {
        loggedInUser = u;
    }

    public User getViewAccountUser() {
        return viewAccountUser;
    }

    public void setViewAccountUser(User u) { viewAccountUser = u;
    }

    public void setEditAccountMode(boolean b) {
        editAccountMode = b;
    }

    public boolean getEditAccountMode() {
        return editAccountMode;
    }

}
