package com.LiveFullstack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;
    private Long streamId;
    private Long userId;
    private String nickname;
    private String content;
    private java.time.LocalDateTime createdAt;
}
