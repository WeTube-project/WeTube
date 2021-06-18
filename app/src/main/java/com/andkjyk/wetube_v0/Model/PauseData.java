package com.andkjyk.wetube_v0.Model;

public class PauseData {
    private boolean isPaused;
    private String roomCode;

    public PauseData(boolean isPaused, String roomCode){
        this.isPaused = isPaused;
        this.roomCode = roomCode;
    }

    public boolean getIsPaused() { return isPaused; }

    public void setIsPaused(boolean isPaused) { this.isPaused = isPaused; }

    public String getRoomCode() { return roomCode; }

    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }
}
