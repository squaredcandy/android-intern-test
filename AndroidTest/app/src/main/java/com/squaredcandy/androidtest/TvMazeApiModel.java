package com.squaredcandy.androidtest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TvMazeApiModel {
    private int id;
    private String url;
    private String name;
    private int season;
    private int episode;
    private String airDate;
    private String airTime;
    private String airStamp;
    private int runTime;
    private String mediumImage;
    private String originalImage;
    private String summary;
    private String links;

    public void print() {
        System.out.println(
                "id:" + id +
                "\nurl:" + url +
                "\nname:" + name +
                "\nseason:" + season +
                "\nepisode:" + episode +
                "\nairDate:" + airDate +
                "\nairTime:" + airTime +
                "\nairStamp:" + airStamp +
                "\nrunTime:" + runTime +
                "\nmediumImage:" + mediumImage +
                "\noriginalImage:" + originalImage +
                "\nsummary:" + summary +
                "\nlinks:" + links
                );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public String getAirTime() {
        return airTime;
    }

    public void setAirTime(String airTime) {
        this.airTime = airTime;
    }

    public String getAirStamp() {
        return airStamp;
    }

    public void setAirStamp(String airStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm");
        try {
            Date date = format.parse(this.airDate + " " + this.airTime);
            this.airStamp = date.toLocaleString();
        } catch (ParseException e) {
            e.printStackTrace();
            this.airStamp = airStamp;
        }
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public String getMediumImage() {
        return mediumImage;
    }

    public void setMediumImage(String mediumImage) {
        this.mediumImage = mediumImage;
    }

    public String getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(String originalImage) {
        this.originalImage = originalImage;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        summary = summary.replaceAll("<[^>]*>", "");
        this.summary = summary;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }
}
