package com.andkjyk.wetube_v0.Model;

public class SearchedVideoItem {    // AddPlaylistActivity에서 검색된 영상 목록을 띄우기 위해 YouTube api 검색결과로 나온 각 영상의 정보를 담는 클래스
    private String title;
    private String publisher;
    private String thumbnailURL;
    private String id;
    private String roomCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getRoomCode() { return roomCode; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnail) {
        this.thumbnailURL = thumbnail;
    }

    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }
}
