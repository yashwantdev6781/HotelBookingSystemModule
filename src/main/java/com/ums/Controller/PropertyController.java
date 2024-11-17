package com.ums.Controller;

import com.ums.Repository.PropertyRepository;
import com.ums.entity.Property;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {
    private PropertyRepository propertyRepository;

    public PropertyController(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @GetMapping
    public ResponseEntity<List<Property>> getPropertyListings(
            @RequestParam String locationName
    ){
        List<Property> propertyListings=propertyRepository.listPropertyByLocationOrCountryName(locationName);
        return new ResponseEntity<>(propertyListings, HttpStatus.OK);
    }
}
