package com.andkjyk.wetube_v0.Model;

public class MainItem {
    private String title, headcount, videoName, thumbnail;

    public String getTitle(){
        return title;
    }

    public String getHeadcount(){
        return headcount;
    }

    public String getVideoName(){
        return videoName;
    }

    public String getThumbnail() { return thumbnail; }

    public void setTitle(String title){
        this.title = title;
    }

    public void setHeadcount(String headcount){
        this.headcount = headcount;
    }

    public void setVideoName(String videoName){
        this.videoName = videoName;
    }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
}
