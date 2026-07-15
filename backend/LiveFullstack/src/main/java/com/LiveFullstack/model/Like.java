package com.LiveFullstack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    private Long id;
    private Long commentId;
    private Long userId;
    private LocalDateTime createdAt;
}
