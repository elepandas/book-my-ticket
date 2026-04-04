package com.ticketbooking.dto;

import javax.validation.constraints.NotBlank;

public class TheaterDto {

    private Long id;

    @NotBlank(message = "Theater name is required")
    private String name;

    private String location;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
