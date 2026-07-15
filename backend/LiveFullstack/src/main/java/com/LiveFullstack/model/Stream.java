package com.LiveFullstack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stream {

    private Long id;
    private String title;
    private String description;
    private String streamKey;
    private Long userId;
    private String status;
    private Integer viewerCount;
    private java.time.LocalDateTime startedAt;
    private java.time.LocalDateTime endedAt;
    private java.time.LocalDateTime createdAt;
}
