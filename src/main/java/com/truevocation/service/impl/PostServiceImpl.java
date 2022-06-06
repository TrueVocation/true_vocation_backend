package com.truevocation.service.impl;

import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.truevocation.domain.Comments;
import com.truevocation.domain.Post;
import com.truevocation.domain.University;
import com.truevocation.domain.User;
import com.truevocation.repository.PostRepository;
import com.truevocation.service.*;
import com.truevocation.service.dto.*;
import com.truevocation.service.mapper.AppUserMapper;
import com.truevocation.service.mapper.CommentsMapper;
import com.truevocation.service.mapper.PostMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.truevocation.service.mapper.UserMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

/**
 * Service Implementation for managing {@link Post}.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Value("${file.posts.viewPath}")
    private String viewPathPicture;

    @Value("${file.posts.uploadPath}")
    private String uploadPathPicture;

    @Value("${file.posts.defaultPicture}")
    private String defaultPicture;


    private static final String TOTAL_PAGES = "totalPages";
    private static final String TOTAL_ELEMENTS = "totalElements";
    private static final String IS_LAST_PAGE = "isLastPage";
    private static final String IS_FIST_PAGE = "isFirstPage";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGE_NUMBER = "pageNumber";

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostDTO save(PostDTO postDTO) {
        log.debug("Request to save Post : {}", postDTO);
        Post post = postMapper.toEntity(postDTO);
        post = postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public Optional<PostDTO> partialUpdate(PostDTO postDTO) {
        log.debug("Request to partially update Post : {}", postDTO);

        return postRepository
            .findById(postDTO.getId())
            .map(existingPost -> {
                postMapper.partialUpdate(existingPost, postDTO);

                return existingPost;
            })
            .map(postRepository::save)
            .map(postMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Posts");
        return postRepository.findAll(pageable).map(postMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostDTO> findOne(Long id) {
        log.debug("Request to get Post : {}", id);
        return postRepository.findById(id).map(postMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostDTO> findOneWithEagerRelations(Long id) {
        log.debug("Request to get Post : {}", id);
        User user = userService.getUserWithAuthorities().orElse(null);
        return postRepository.findById(id)
            .map(postMapper::toDto)
            .map(postDTO -> {
            postDTO.setLikeCount(likesService.getPostLikesCount(postDTO.getId()));
            postDTO.setCommentCount(commentsService.getPostCommentsCount(postDTO.getId()));
            if(!Objects.isNull(user)){
                postDTO.setLiked(likesService.isLiked(postDTO.getId(), user.getId()));
                postDTO.setFavorite(favoriteService.isFavorite(postDTO.getId(), user.getId()));
            }
            postDTO.setCommentsList(getPostComments(id));
            return postDTO;
        });
    }

    private List<CommentsDTO> getPostComments(Long postId){
        List<Comments> comments = commentsService.getPostComments(postId);
        return comments.stream()
            .map(comment -> {
                CommentsDTO commentsDTO = new CommentsDTO();
                commentsDTO.setId(comment.getId());
                commentsDTO.setText(comment.getText());
                commentsDTO.setAddedDate(comment.getAddedDate());
                commentsDTO.setUser(appUserMapper.toDto(comment.getUser()));
                commentsDTO.setUserDTO(userMapper.appUserToUserDTO(comment.getUser()));
                commentsDTO.setUser(null);
                return commentsDTO;
            }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        postRepository.deleteById(id);
    }

    @Override
    public PostDTO uploadPicture(MultipartFile file, Long postId) {
        if (Objects.equals(file.getContentType(), "image/jpeg") || Objects.equals(file.getContentType(), "image/png")) {
            String extension;
            if ("image/jpeg".equals(file.getContentType())) {
                extension = ".jpg";
            } else {
                extension = ".png";
            }
            try {
                PostDTO postDTO = this.findOne(postId).orElse(null);
                if (Objects.isNull(postDTO)) {
                    throw new EntityNotFoundException("Post entity not found");
                }

                String picName = UUID.randomUUID().toString();

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPathPicture + picName + extension);
                Files.write(path, bytes);
                postDTO.setPicture(path.getFileName().toString());

                postDTO = this.save(postDTO);
                return postDTO;
            } catch (Exception e) {
                log.error("Error when save picture");
                throw new InternalServerErrorException("Error when save picture");
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getPicture(String url) throws IOException {
        Post post = postRepository.findByPictureEquals(url);
        String extension = "";
        if (post.getPicture() != null && (post.getPicture().contains(".jpg") || post.getPicture().contains(".png"))) {
            String[] pictureSplit = post.getPicture().split("\\.");
            url = pictureSplit[0];
            extension = pictureSplit[1];
        }
        String pictureURL = viewPathPicture + defaultPicture;
        if (url != null && !url.equals("null")) {
            pictureURL = viewPathPicture + url + "." + extension;
        }

        InputStream in;
        try {
            in = new FileInputStream(pictureURL);
        } catch (Exception e) {
            in = new FileInputStream(viewPathPicture + defaultPicture);
            extension = "png";
        }
        String contentType = defineContentType(extension);
        byte[] content = Base64.getEncoder().encode(IOUtils.toByteArray(in));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        return ResponseEntity.ok().headers(headers).body(content);
    }

    public String defineContentType(String extension) {
        if (extension.equals("jpg")) {
            return "image/jpeg";
        } else {
            return "image/png";
        }
    }

    @Override
    public HttpHeaders addCustomPaginationHeaders(HttpHeaders headers, Page<PostDTO> page) {
        int totalPages = page.getTotalPages();
        long totalElements = page.getTotalElements();
        boolean last = page.isLast();
        boolean first = page.isFirst();
        int size = page.getSize();
        int number = page.getNumber() + 1;
        headers.add(TOTAL_PAGES, String.valueOf(totalPages));
        headers.add(TOTAL_ELEMENTS, String.valueOf(totalElements));
        headers.add(IS_LAST_PAGE, String.valueOf(last));
        headers.add(IS_FIST_PAGE, String.valueOf(first));
        headers.add(PAGE_SIZE, String.valueOf(size));
        headers.add(PAGE_NUMBER, String.valueOf(number));
        return headers;
    }


    @Override
    public PostsPageDTO findAllForPostsPage(String searchText, Pageable pageable) {
        PostsPageDTO postsPageDTO = new PostsPageDTO();
        User user = userService.getUserWithAuthorities().orElse(null);
        Page<PostDTO> latestPostsPage = postRepository.findAllByTitleContaining(searchText, pageable)
            .map(postMapper::toDto)
            .map(postDTO -> {
                postDTO.setLikeCount(likesService.getPostLikesCount(postDTO.getId()));
                postDTO.setCommentCount(commentsService.getPostCommentsCount(postDTO.getId()));
                if(!Objects.isNull(user)){
                    postDTO.setLiked(likesService.isLiked(postDTO.getId(), user.getId()));
                    postDTO.setFavorite(favoriteService.isFavorite(postDTO.getId(), user.getId()));
                }
                return postDTO;
            })
            ;
        List<PostDTO> postDTOList = postRepository.findAllOldPosts().stream().map(postMapper::toDto).collect(Collectors.toList());
        postsPageDTO.setOldPosts(postDTOList);
        postsPageDTO.setLatestPosts(latestPostsPage);
        return postsPageDTO;
    }
}
