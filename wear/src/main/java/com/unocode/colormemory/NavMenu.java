package com.unocode.colormemory;

public class NavMenu {
    private String name;
    private String navigationIcon;

    public NavMenu(
            String name,
            String navigationIcon) {

        this.name = name;
        this.navigationIcon = navigationIcon;
    }

    public String getName() {
        return name;
    }

    public String getNavigationIcon() {
        return navigationIcon;
    }
}
