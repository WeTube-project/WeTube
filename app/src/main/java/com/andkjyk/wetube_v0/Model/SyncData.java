package com.andkjyk.wetube_v0.Model;

public class SyncData {
    private boolean isHost;
    private boolean isPaused;
    private float hostTimestamp;
    private float firstHostTimestamp;
    private String videoId;
    private String roomCode;
    private float guestTimestamp;
    private String guestFrom;

    public SyncData(boolean isHost, float hostTimestamp, String videoId, String roomCode){
        this.isHost = isHost;
        //this.isPaused = isPaused;
        this.hostTimestamp = hostTimestamp;
        //this.guestTimestamp = guestTimestamp;
        this.videoId = videoId;
        this.roomCode = roomCode;
    }

    public SyncData(boolean isHost, float guestTimestamp, String guestFrom){
        this.isHost = isHost;
        this.guestTimestamp = guestTimestamp;
        this.guestFrom = guestFrom;
    }

    public boolean getIsHost() { return isHost; }

    //public boolean getIsPaused() { return isPaused; }

    public float getHostTimestamp() { return hostTimestamp; }

    public float getFirstHostTimestamp() { return firstHostTimestamp; }

    public float getGuestTimestamp() { return guestTimestamp; }

    public String getVideoId() { return videoId; }

    public String getRoomCode() { return roomCode; }

    public void setIsHost(boolean isHost) { this.isHost = isHost; }

    //public void setIsPaused(boolean isPaused) { this.isPaused = isPaused; }

    public void setHostTimestamp(float hostTimestamp) { this.hostTimestamp = hostTimestamp; }

    public void setGuestTimestamp(float guestTimestamp) { this.guestTimestamp = guestTimestamp; }

    public void setVideoId(String videoId) { this.videoId = videoId; }

    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }
}
