package com.truevocation.service.impl;

import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.truevocation.domain.Post;
import com.truevocation.domain.University;
import com.truevocation.repository.PostRepository;
import com.truevocation.service.PostService;
import com.truevocation.service.dto.PostDTO;
import com.truevocation.service.dto.UniversityDTO;
import com.truevocation.service.mapper.PostMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        postRepository.deleteById(id);
    }

    @Override
    public PostDTO uploadPicture(MultipartFile file, Long postId) {
        if(Objects.equals(file.getContentType(), "image/jpeg") || Objects.equals(file.getContentType(), "image/png")) {
            String extension;
            if ("image/jpeg".equals(file.getContentType())) {
                extension = ".jpg";
            } else {
                extension = ".png";
            }
            try {
                PostDTO postDTO = this.findOne(postId).orElse(null);
                if(Objects.isNull(postDTO)){
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
        if (post.getPicture() != null && (post.getPicture().contains(".jpg") || post.getPicture().contains(".png"))){
            String[] pictureSplit = post.getPicture().split("\\.");
            url = pictureSplit[0];
            extension = pictureSplit[1];
        }
        String pictureURL = viewPathPicture+defaultPicture;
        if(url!=null && !url.equals("null")){
            pictureURL = viewPathPicture+url+"."+extension;
        }

        InputStream in;
        try{
            in = new FileInputStream(pictureURL);
        }catch (Exception e){
            in = new FileInputStream(viewPathPicture+defaultPicture);
            extension = "png";
            e.printStackTrace();
        }
        String contentType = defineContentType(extension);
        byte[] content = Base64.getEncoder().encode(IOUtils.toByteArray(in));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        return ResponseEntity.ok().headers(headers).body(content);
    }

    public String defineContentType(String extension){
        if(extension.equals("jpg")){
            return "image/jpeg";
        }else {
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
        int number = page.getNumber()+1;
        headers.add(TOTAL_PAGES, String.valueOf(totalPages));
        headers.add(TOTAL_ELEMENTS, String.valueOf(totalElements));
        headers.add(IS_LAST_PAGE, String.valueOf(last));
        headers.add(IS_FIST_PAGE, String.valueOf(first));
        headers.add(PAGE_SIZE, String.valueOf(size));
        headers.add(PAGE_NUMBER, String.valueOf(number));
        return headers;
    }
}
