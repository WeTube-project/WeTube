package com.andkjyk.wetube_v0.Model;

public class RoomData {
    private String username;
    private String roomCode;

    public RoomData(String username, String roomCode) {
        this.username = username;
        this.roomCode = roomCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }
}
