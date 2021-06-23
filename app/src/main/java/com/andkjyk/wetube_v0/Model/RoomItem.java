package com.andkjyk.wetube_v0.Model;

public class RoomItem {
    private String roomTitle, headcount, videoName, thumbnail, roomCode, hostName, videoId;

    public RoomItem(){};

    public RoomItem(String roomTitle, String hostName, String roomCode) {
        this.roomTitle = roomTitle;
        this.hostName = hostName;
        this.roomCode = roomCode;
    }

    public RoomItem(String roomTitle, String headcount, String videoName, String thumbnail, String hostName, String roomCode) {
        this.roomTitle = roomTitle;
        this.headcount = headcount;
        this.videoName = videoName;
        this.thumbnail = thumbnail;
        this.hostName = hostName;
        this.roomCode = roomCode;
    }

    public String getRoomTitle(){ return roomTitle; }

    public String getHeadcount(){
        return headcount;
    }

    public String getVideoName(){
        return videoName;
    }

    public String getThumbnail() { return thumbnail; }

    public String getRoomCode() { return roomCode; }

    public String getHostName() { return hostName; }

    public String getVideoId() { return videoId; }

    public void setRoomTitle(String roomTitle){
        this.roomTitle = roomTitle;
    }

    public void setHeadcount(String headcount){
        this.headcount = headcount;
    }

    public void setVideoName(String videoName){
        this.videoName = videoName;
    }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public void setHostName(String hostName) { this.hostName = hostName; }

    public void setVideoId(String videoId) { this.videoId = videoId; }
}
