package com.ums.Controller;

import com.ums.Service.UserService;
import com.ums.entity.AppUser;
import com.ums.payload.JwtResponse;
import com.ums.payload.LoginDto;
import com.ums.payload.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class Authcontroller {
    private UserService userService;

    public Authcontroller(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{addUser}")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDto userDto,
                                     BindingResult bindingResult

    ){
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        UserDto dto = userService.addUser(userDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){

       String token = userService.verifyLogin(loginDto);
       if(token!=null){
           JwtResponse response = new JwtResponse();
           response.setToken(token);
           return new ResponseEntity<>(token,HttpStatus.OK);

       }
       return new ResponseEntity<>("Invalid credentials",HttpStatus.UNAUTHORIZED);

    }
    @GetMapping("/profile")
    public AppUser getCurrentProfile(@AuthenticationPrincipal AppUser user){
        return user;
    }


}
