package Rater.Controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

 /*   @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<Map<String, UUID>> registerUser(@Valid @RequestBody User user) throws DataConflictException {
        *//*if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }*//*

      *//*  if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }*//*

        user.encodePassword(encoder);
        user = userService.addUser(user);
        return ResponseEntity.ok(Collections.singletonMap("id", user.getId()));
    }*/
}
