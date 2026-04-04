package com.ticketbooking.dto;

import javax.validation.constraints.NotBlank;

public class MovieDto {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String genre;
    private Integer duration;
    private String language;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
