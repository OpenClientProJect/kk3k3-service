package com.ligg.dto;

import com.ligg.entity.Episodes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDataDTO {
    private Long id;
    private String title;
    private String description;//视频描述
    private String coverUrl;//视频封面
    private LocalDateTime createTime;
    private List<Episodes> episodes;
}
