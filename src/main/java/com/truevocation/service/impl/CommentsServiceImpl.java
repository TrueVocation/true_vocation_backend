package com.truevocation.service.impl;

import com.truevocation.domain.AppUser;
import com.truevocation.domain.Comments;
import com.truevocation.domain.Post;
import com.truevocation.domain.User;
import com.truevocation.repository.CommentsRepository;
import com.truevocation.service.AppUserService;
import com.truevocation.service.CommentsService;
import com.truevocation.service.PostService;
import com.truevocation.service.UserService;
import com.truevocation.service.dto.AppUserDTO;
import com.truevocation.service.dto.CommentsDTO;
import com.truevocation.service.dto.PostDTO;
import com.truevocation.service.dto.UserDTO;
import com.truevocation.service.mapper.CommentsMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * Service Implementation for managing {@link Comments}.
 */
@Service
@Transactional
public class CommentsServiceImpl implements CommentsService {

    private final Logger log = LoggerFactory.getLogger(CommentsServiceImpl.class);

    private final CommentsRepository commentsRepository;

    private final CommentsMapper commentsMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private AppUserService appUserService;

    public CommentsServiceImpl(CommentsRepository commentsRepository, CommentsMapper commentsMapper) {
        this.commentsRepository = commentsRepository;
        this.commentsMapper = commentsMapper;
    }

    @Override
    public CommentsDTO save(CommentsDTO commentsDTO) {
        log.debug("Request to save Comments : {}", commentsDTO);
        Comments comments = commentsMapper.toEntity(commentsDTO);
        comments = commentsRepository.save(comments);
        return commentsMapper.toDto(comments);
    }

    @Override
    public Optional<CommentsDTO> partialUpdate(CommentsDTO commentsDTO) {
        log.debug("Request to partially update Comments : {}", commentsDTO);

        return commentsRepository
            .findById(commentsDTO.getId())
            .map(existingComments -> {
                commentsMapper.partialUpdate(existingComments, commentsDTO);

                return existingComments;
            })
            .map(commentsRepository::save)
            .map(commentsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Comments");
        return commentsRepository.findAll(pageable).map(commentsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentsDTO> findOne(Long id) {
        log.debug("Request to get Comments : {}", id);
        return commentsRepository.findById(id).map(commentsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Comments : {}", id);
        commentsRepository.deleteById(id);
    }

    @Override
    public int getPostCommentsCount(Long postId) {
        return commentsRepository.getPostCommentsCount(postId);
    }

    @Override
    public List<Comments> getPostComments(Long postID) {
        return commentsRepository.getPostComments(postID);
    }

    @Override
    public CommentsDTO addUserComment(CommentsDTO commentsDTO) {
        if(!Objects.isNull(commentsDTO.getId())){
            Comments comments = commentsRepository.findById(commentsDTO.getId()).orElse(null);
            if(!Objects.isNull(comments)){
                comments.setText(commentsDTO.getText());
                commentsRepository.save(comments);
                return commentsDTO;
            }
        }
        AppUserDTO appUserDTO = appUserService.findByUserId(commentsDTO.getUserDTO().getId()).orElse(null);
        if(!Objects.isNull(appUserDTO)){
            PostDTO postDTO = postService.findOne(commentsDTO.getPost().getId()).orElse(null);
            if(Objects.isNull(postDTO)){
                throw new EntityNotFoundException("post not found");
            }
            Comments comments = new Comments();
            Post post = new Post();
            post.setId(postDTO.getId());
            AppUser appUser = new AppUser();
            appUser.setId(appUserDTO.getId());
            comments.setAddedDate(LocalDate.now());
            comments.setPost(post);
            comments.setUser(appUser);
            comments.setText(commentsDTO.getText());
            Comments comments1 =  commentsRepository.save(comments);
            return commentsMapper.toDto(comments1);
        }
        return null;
    }
}
