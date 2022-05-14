package com.truevocation.service.impl;

import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.truevocation.domain.Pictures;
import com.truevocation.domain.University;
import com.truevocation.repository.UniversityRepository;
import com.truevocation.service.UniversityService;
import com.truevocation.service.dto.CourseDTO;
import com.truevocation.service.dto.PicturesDTO;
import com.truevocation.service.dto.PortfolioDTO;
import com.truevocation.service.dto.UniversityDTO;
import com.truevocation.service.mapper.UniversityMapper;

import java.io.FileInputStream;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

/**
 * Service Implementation for managing {@link University}.
 */
@Service
@Transactional
public class UniversityServiceImpl implements UniversityService {

    private final Logger log = LoggerFactory.getLogger(UniversityServiceImpl.class);

    private final UniversityRepository universityRepository;

    private final UniversityMapper universityMapper;

    @Value("${file.university.viewPath}")
    private String viewPathPicture;

    @Value("${file.university.uploadPath}")
    private String uploadPathPicture;

    @Value("${file.university.defaultPicture}")
    private String defaultPicture;

    public UniversityServiceImpl(UniversityRepository universityRepository, UniversityMapper universityMapper) {
        this.universityRepository = universityRepository;
        this.universityMapper = universityMapper;
    }

    @Override
    public UniversityDTO save(UniversityDTO universityDTO) {
        log.debug("Request to save University : {}", universityDTO);
        University university = universityMapper.toEntity(universityDTO);
        university = universityRepository.save(university);
        return universityMapper.toDto(university);
    }

    @Override
    public Optional<UniversityDTO> partialUpdate(UniversityDTO universityDTO) {
        log.debug("Request to partially update University : {}", universityDTO);

        return universityRepository
            .findById(universityDTO.getId())
            .map(existingUniversity -> {
                universityMapper.partialUpdate(existingUniversity, universityDTO);

                return existingUniversity;
            })
            .map(universityRepository::save)
            .map(universityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniversityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Universities");
        return universityRepository.findAll(pageable).map(universityMapper::toDto);
    }

    public Page<UniversityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return universityRepository.findAllWithEagerRelationships(pageable).map(universityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UniversityDTO> findOne(Long id) {
        log.debug("Request to get University : {}", id);
        return universityRepository.findOneWithEagerRelationships(id).map(universityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete University : {}", id);
        universityRepository.deleteById(id);
    }

    @Override
    public UniversityDTO saveLogo(MultipartFile file, Long universityId) {
        if(Objects.equals(file.getContentType(), "image/jpeg") || Objects.equals(file.getContentType(), "image/png")) {
            String extension;
            if ("image/jpeg".equals(file.getContentType())) {
                extension = ".jpg";
            } else {
                extension = ".png";
            }
            try {
                UniversityDTO universityDTO = this.findOne(universityId).orElse(null);
               if(Objects.isNull(universityDTO)){
                   throw new EntityNotFoundException("University entity not found");
               }

                String picName = UUID.randomUUID().toString();

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPathPicture + picName + extension);
                Files.write(path, bytes);
                universityDTO.setLogo(path.getFileName().toString());

                universityDTO = this.save(universityDTO);
                return universityDTO;
            } catch (Exception e) {
                log.error("Error when save picture");
                throw new InternalServerErrorException("Error when save picture");
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getLogoByUrl(String url) throws IOException {
        University university = universityRepository.findByLogoEquals(url);
        String extension = "";
        if (university.getLogo() != null && (university.getLogo().contains(".jpg") || university.getLogo().contains(".png"))){
            String[] pictureSplit = university.getLogo().split("\\.");
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
}
