package com.threadjava.post.model;

import com.threadjava.image.ImageDto;
import com.threadjava.users.model.UserShortDto;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto {
    public UUID id;
    public String body;
    public long likeCount;
    public long dislikeCount;
    public long commentCount;
    public Date createdAt;
    public ImageDto image;
    public UserShortDto user;
}