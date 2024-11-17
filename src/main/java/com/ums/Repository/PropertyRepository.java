package com.ums.Repository;

import com.ums.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("select p from Property p join Location l on p.location=l.id where l.locationName=:locationName")
    List<Property> listPropertyByLocationOrCountryName(@Param("locationName") String locationName);
}