package com.foxminded.SQL.domain;

public class Group {
    private static final String TAB = " ";

    private final int groupID;
    private final String groupName;

    public Group(int groupID, String groupName) {
        this.groupID = groupID;
        this.groupName = groupName;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String toString() {
        return groupID + TAB + groupName;
    }
}
