package com.cs122b.model;

import java.util.ArrayList;

public class DatabaseTable {

    public String name;
    public ArrayList<String> attributes = new ArrayList<String>();

    public DatabaseTable() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addAttribute(String attribute) {
        attributes.add(attribute);
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<String> attributes) {
        this.attributes = attributes;
    }
}
