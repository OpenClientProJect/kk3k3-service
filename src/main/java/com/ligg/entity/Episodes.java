package com.ligg.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Episodes {
    private Long episodesId;
    private String episodesTitle;
    private String episodesImage;
    private String episodesVideo;
    private LocalDateTime createTime;
    private Long videoId;
}
