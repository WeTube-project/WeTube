package com.andkjyk.wetube_v0.Model;

public class Room {
    private String roomTitle;
    private String hostName;
    private String roomCode;

    public Room(String roomTitle, String hostName, String roomCode) {
        this.roomTitle = roomTitle;
        this.hostName = hostName;
        this.roomCode = roomCode;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }
}
