package com.LiveFullstack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dashboard {

    private Long id;
    private Long streamId;
    private Integer totalViewers;
    private Integer peakViewers;
    private Integer totalComments;
    private Integer totalVotes;
    private java.time.LocalDateTime generatedAt;
}
