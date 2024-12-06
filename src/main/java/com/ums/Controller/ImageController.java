package com.ums.Controller;

import com.ums.Repository.ImagesRepository;
import com.ums.Repository.PropertyRepository;
import com.ums.Service.BucketService;
import com.ums.entity.Image;
import com.ums.entity.Property;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private ImagesRepository imagesRepository;
    private PropertyRepository propertyRepository;

    private BucketService bucketService;

    public ImageController(ImagesRepository imagesRepository, PropertyRepository propertyRepository, BucketService bucketService) {
        this.imagesRepository = imagesRepository;
        this.propertyRepository = propertyRepository;
        this.bucketService = bucketService;
    }

    @PostMapping("/addImage")
    public ResponseEntity<Image> addImages(
            @RequestParam long propertyId,
            @RequestParam String bucketName,
            MultipartFile file
    ){
        String imageUrl = bucketService.uploadFile(file,bucketName);
        Optional<Property> byId = propertyRepository.findById(propertyId);
        Property property = byId.get();
        Image image = new Image();
        image.setImageUrl(imageUrl);
        image.setProperty(property);
        Image savedImage = imagesRepository.save(image);
        return new ResponseEntity<>(savedImage, HttpStatus.CREATED);

    }

    @GetMapping("/propertyImages")
    public ResponseEntity<List<Image>> fetchPropertyImages(
            @RequestParam long propertyId
    ){
        List<Image> images = imagesRepository.findByPropertyId(propertyId);
        return new ResponseEntity<>(images,HttpStatus.OK);
    }
}
