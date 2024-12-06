package com.ums.Controller;

import com.ums.Repository.PropertyRepository;
import com.ums.Repository.ReviewRepository;
import com.ums.entity.AppUser;
import com.ums.entity.Property;
import com.ums.entity.Review;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/reviews")
public class ReviewController {
    public ReviewController(PropertyRepository propertyRepository, ReviewRepository reviewRepository) {
        this.propertyRepository = propertyRepository;
        this.reviewRepository = reviewRepository;
    }

    private PropertyRepository propertyRepository;

    private ReviewRepository reviewRepository;

    @PostMapping
    public ResponseEntity<String> addReview(
            @AuthenticationPrincipal AppUser user,
            @RequestParam long propertyId,
            @RequestBody Review review
            ){
        Optional<Property> byId = propertyRepository.findById(propertyId);
        Property property = byId.get();
        review.setAppUser(user);
        review.setProperty(property);
        Review r = reviewRepository.fetchUserReview(property,user);
        if(r!=null){
            return  new ResponseEntity<>("Review is already given", HttpStatus.BAD_REQUEST);
        }
        reviewRepository.save(review);
        return new ResponseEntity<>("Review is Added", HttpStatus.CREATED);
    }

}
