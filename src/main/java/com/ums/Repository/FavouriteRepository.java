package com.ums.Repository;

import com.ums.entity.AppUser;
import com.ums.entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    @Query("select f from Favourite f where f.appUser=:user")
   public List<Favourite> getFavourites(@Param("user") AppUser user);
}