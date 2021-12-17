package com.andkjyk.wetube_v0.Model;

public class PauseData {    // 호스트가 영상을 정지했을 때 서버로 보내질 데이터를 담는 클래스
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
