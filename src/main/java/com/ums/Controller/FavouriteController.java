package com.ums.Controller;


import com.ums.Repository.FavouriteRepository;
import com.ums.Repository.PropertyRepository;
import com.ums.entity.AppUser;
import com.ums.entity.Favourite;
import com.ums.entity.Property;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/favourite")
public class FavouriteController {

    private PropertyRepository propertyRepository;
    private FavouriteRepository favouriteRepository;

    public FavouriteController(PropertyRepository propertyRepository, FavouriteRepository favouriteRepository) {
        this.propertyRepository = propertyRepository;
        this.favouriteRepository = favouriteRepository;
    }

    @PostMapping("/addFav")
    public ResponseEntity<Favourite> addFavourite(
            @AuthenticationPrincipal AppUser user,
            @RequestParam long propertyId
            ){
        Optional<Property> byId= propertyRepository.findById(propertyId);
        Property property = byId.get();
        Favourite favourite = new Favourite();
        favourite.setAppUser(user);
        favourite.setProperty(property);
        favourite.setFavourite(true);
        Favourite savedFavourite = favouriteRepository.save(favourite);
        return  new ResponseEntity<>(savedFavourite, HttpStatus.CREATED);
    }

    @GetMapping("/userFavouriteList")
    public ResponseEntity<List<Favourite>> getAllFavouritesofUser(
        @AuthenticationPrincipal AppUser user
        ){
        List<Favourite> favourites = favouriteRepository.getFavourites(user);
        return new ResponseEntity<>(favourites, HttpStatus.OK);

    }
}
