package com.andkjyk.wetube_v0.Model;

public class SyncData {     // 동기화 기능을 위해 서버로 보내질 동기화에 필요한 데이터를 담는 클래스
    private boolean isHost;
    private float hostTimestamp;
    private String videoId;
    private String roomCode;
    private float guestTimestamp;

    public SyncData(boolean isHost, float hostTimestamp, String videoId, String roomCode){
        this.isHost = isHost;
        this.hostTimestamp = hostTimestamp;
        this.videoId = videoId;
        this.roomCode = roomCode;
    }

    public boolean getIsHost() { return isHost; }

    public float getHostTimestamp() { return hostTimestamp; }

    public float getGuestTimestamp() { return guestTimestamp; }

    public String getVideoId() { return videoId; }

    public String getRoomCode() { return roomCode; }

    public void setIsHost(boolean isHost) { this.isHost = isHost; }

    public void setHostTimestamp(float hostTimestamp) { this.hostTimestamp = hostTimestamp; }

    public void setGuestTimestamp(float guestTimestamp) { this.guestTimestamp = guestTimestamp; }

    public void setVideoId(String videoId) { this.videoId = videoId; }

    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }
}
