package com.truevocation.service.impl;

import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.truevocation.domain.Achievement;
import com.truevocation.domain.Post;
import com.truevocation.repository.AchievementRepository;
import com.truevocation.service.AchievementService;
import com.truevocation.service.dto.AchievementDTO;
import com.truevocation.service.dto.PostDTO;
import com.truevocation.service.mapper.AchievementMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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
 * Service Implementation for managing {@link Achievement}.
 */
@Service
@Transactional
public class AchievementServiceImpl implements AchievementService {

    private final Logger log = LoggerFactory.getLogger(AchievementServiceImpl.class);

    private final AchievementRepository achievementRepository;

    private final AchievementMapper achievementMapper;

    @Value("${file.achievements.viewPath}")
    private String viewPathPicture;

    @Value("${file.achievements.uploadPath}")
    private String uploadPathPicture;

    @Value("${file.achievements.defaultPicture}")
    private String defaultPicture;

    public AchievementServiceImpl(AchievementRepository achievementRepository, AchievementMapper achievementMapper) {
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
    }

    @Override
    public AchievementDTO save(AchievementDTO achievementDTO) {
        log.debug("Request to save Achievement : {}", achievementDTO);
        Achievement achievement = achievementMapper.toEntity(achievementDTO);
        achievement = achievementRepository.save(achievement);
        return achievementMapper.toDto(achievement);
    }

    @Override
    public Optional<AchievementDTO> partialUpdate(AchievementDTO achievementDTO) {
        log.debug("Request to partially update Achievement : {}", achievementDTO);

        return achievementRepository
            .findById(achievementDTO.getId())
            .map(existingAchievement -> {
                achievementMapper.partialUpdate(existingAchievement, achievementDTO);

                return existingAchievement;
            })
            .map(achievementRepository::save)
            .map(achievementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AchievementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Achievements");
        return achievementRepository.findAll(pageable).map(achievementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AchievementDTO> findOne(Long id) {
        log.debug("Request to get Achievement : {}", id);
        return achievementRepository.findById(id).map(achievementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Achievement : {}", id);
        achievementRepository.deleteById(id);
    }


    @Override
    public AchievementDTO uploadPicture(MultipartFile file, Long achievementId) {
        if (Objects.equals(file.getContentType(), "image/jpeg") || Objects.equals(file.getContentType(), "image/png")) {
            String extension;
            if ("image/jpeg".equals(file.getContentType())) {
                extension = ".jpg";
            } else {
                extension = ".png";
            }
            try {
                AchievementDTO achievementDTO = this.findOne(achievementId).orElse(null);
                if (Objects.isNull(achievementDTO)) {
                    achievementDTO = new AchievementDTO();
                }

                String picName = UUID.randomUUID().toString();

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPathPicture + picName + extension);
                Files.write(path, bytes);
                achievementDTO.setPicture(path.getFileName().toString());

                achievementDTO = this.save(achievementDTO);
                return achievementDTO;
            } catch (Exception e) {
                log.error("Error when save picture");
                throw new InternalServerErrorException("Error when save picture");
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getPicture(String url) throws IOException {
        Achievement achievement = achievementRepository.findByPictureEquals(url);
        String extension = "";
        if (achievement.getPicture() != null && (achievement.getPicture().contains(".jpg") || achievement.getPicture().contains(".png"))) {
            String[] pictureSplit = achievement.getPicture().split("\\.");
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
    public List<AchievementDTO> findByPortfolio(Long id) {
        return achievementRepository.findAllByPortfolioId(id).stream()
            .map(achievementMapper::toDto)
            .collect(Collectors.toList());
    }
}


