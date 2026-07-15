package com.LiveFullstack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveStatus {

    private Long streamId;
    private String status;
    private Integer viewerCount;
    private Integer commentCount;
    private Integer voteCount;
    private java.time.LocalDateTime updatedAt;
}
