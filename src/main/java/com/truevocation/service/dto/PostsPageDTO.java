package com.truevocation.service.dto;


import org.springframework.data.domain.Page;

import java.util.List;

public class PostsPageDTO {
    private Page<PostDTO> latestPosts;
    private List<PostDTO> oldPosts;

    public PostsPageDTO() {
    }

    public Page<PostDTO> getLatestPosts() {
        return latestPosts;
    }

    public void setLatestPosts(Page<PostDTO> latestPosts) {
        this.latestPosts = latestPosts;
    }

    public List<PostDTO> getOldPosts() {
        return oldPosts;
    }

    public void setOldPosts(List<PostDTO> oldPosts) {
        this.oldPosts = oldPosts;
    }
}
