package com.andkjyk.wetube_v0.Model;

public class PlaylistItem {

    private String plVideoName, plPublisher, plVideoId, plThumbnailURL, plRoomCode;

    public PlaylistItem(){};

    public PlaylistItem(String videoName, String publisher, String videoId, String thumbnailURL, String roomCode){
        this.plVideoName = videoName;
        this.plPublisher = publisher;
        this.plVideoId = videoId;
        this.plThumbnailURL = thumbnailURL;
        this.plRoomCode = roomCode;
    }

    public String getPlVideoName(){
        return plVideoName;
    }

    public String getPlPublisher(){
        return plPublisher;
    }

    public String getPlVideoId() { return plVideoId; }

    public String getPlThumbnailURL() { return plThumbnailURL; }

    public String getPlRoomCode() { return plRoomCode; }

    public void setPlVideoName(String plVideoName){
        this.plVideoName = plVideoName;
    }

    public void setPlPublisher(String plPublisher){
        this.plPublisher = plPublisher;
    }

    public void setPlVideoId(String plVideoName) { this.plVideoId = plVideoName; }

    public void setPlThumbnailURL(String plThumbnailURL) { this.plThumbnailURL = plThumbnailURL; }

    public void setPlRoomCode(String plRoomCode) { this.plRoomCode = plRoomCode; }
}
