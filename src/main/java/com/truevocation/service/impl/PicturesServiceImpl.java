package com.truevocation.service.impl;

import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.truevocation.domain.Pictures;
import com.truevocation.repository.PicturesRepository;
import com.truevocation.service.CourseService;
import com.truevocation.service.PicturesService;
import com.truevocation.service.PortfolioService;
import com.truevocation.service.UniversityService;
import com.truevocation.service.dto.CourseDTO;
import com.truevocation.service.dto.PicturesDTO;
import com.truevocation.service.dto.PortfolioDTO;
import com.truevocation.service.dto.UniversityDTO;
import com.truevocation.service.mapper.CourseMapper;
import com.truevocation.service.mapper.PicturesMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.truevocation.service.mapper.PortfolioMapper;
import com.truevocation.service.mapper.UniversityMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Pictures}.
 */
@Service
@Transactional
public class PicturesServiceImpl implements PicturesService {

    private final Logger log = LoggerFactory.getLogger(PicturesServiceImpl.class);

    private final PicturesRepository picturesRepository;

    private final PicturesMapper picturesMapper;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UniversityMapper universityMapper;

    @Autowired
    private PortfolioMapper portfolioMapper;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.picture.viewPath}")
    private String viewPathPicture;

    @Value("${file.picture.uploadPath}")
    private String uploadPathPicutre;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;



    private static final String COURSE_PICTURE = "course";
    private static final String UNIVERSITY_PICTURE = "university";
    private static final String PORTFOLIO_PICTURE = "portfolio";



    public PicturesServiceImpl(PicturesRepository picturesRepository, PicturesMapper picturesMapper) {
        this.picturesRepository = picturesRepository;
        this.picturesMapper = picturesMapper;
    }

    @Override
    public PicturesDTO save(PicturesDTO picturesDTO) {
        log.debug("Request to save Pictures : {}", picturesDTO);
        Pictures pictures = picturesMapper.toEntity(picturesDTO);
        pictures = picturesRepository.save(pictures);
        return picturesMapper.toDto(pictures);
    }

    @Override
    public Optional<PicturesDTO> partialUpdate(PicturesDTO picturesDTO) {
        log.debug("Request to partially update Pictures : {}", picturesDTO);

        return picturesRepository
            .findById(picturesDTO.getId())
            .map(existingPictures -> {
                picturesMapper.partialUpdate(existingPictures, picturesDTO);

                return existingPictures;
            })
            .map(picturesRepository::save)
            .map(picturesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PicturesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pictures");
        return picturesRepository.findAll(pageable).map(picturesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PicturesDTO> findOne(Long id) {
        log.debug("Request to get Pictures : {}", id);
        return picturesRepository.findById(id).map(picturesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pictures : {}", id);
        picturesRepository.deleteById(id);
    }

    @Override
    public PicturesDTO savePicture(MultipartFile file, Long sourceId, String sourceType) {
        if(Objects.equals(file.getContentType(), "image/jpeg") || Objects.equals(file.getContentType(), "image/png")) {
            String extension;
            if ("image/jpeg".equals(file.getContentType())) {
                extension = ".jpg";
            } else {
                extension = ".png";
            }
            try {
                PicturesDTO pictures = new PicturesDTO();
                switch (sourceType){
                    case COURSE_PICTURE:
                        CourseDTO course = courseService.findOne(sourceId).orElse(null);
                        pictures.setCourse(course);
                        break;
                    case UNIVERSITY_PICTURE:
                        UniversityDTO university = universityService.findOne(sourceId).orElse(null);
                        pictures.setUniversity(university);
                        break;
                    case PORTFOLIO_PICTURE:
                        PortfolioDTO portfolio = portfolioService.findOne(sourceId).orElse(null);
                        pictures.setPortfolio(portfolio);
                        break;
                }

                String picName = UUID.randomUUID().toString();

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPathPicutre + picName + extension);
                Files.write(path, bytes);
                pictures.setPicture(path.getFileName().toString());
                pictures.setUrl(picName);

                pictures = this.save(pictures);
                return pictures;
            } catch (Exception e) {
                log.error("Error when save picture");
                throw new InternalServerErrorException("Error when save picture");
            }
        }
        return null;
    }


    @Override
    public byte[] getPictureByUrl(String url) throws IOException {
        Pictures pictures = picturesRepository.findByUrlEquals(url);
        String[] pictureSplit = pictures.getPicture().split("\\.");
        url = pictureSplit[0];
        String extension = pictureSplit[1];
        String pictureURL = viewPath+defaultPicture;
        if(url!=null && !url.equals("null")){
            pictureURL = viewPathPicture+url+"."+extension;
        }



        InputStream in;
        try{
            ClassPathResource resource = new ClassPathResource(pictureURL);
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource = new ClassPathResource(viewPath+defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }

        return IOUtils.toByteArray(in);
    }
}
