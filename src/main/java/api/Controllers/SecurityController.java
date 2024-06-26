package api.Controllers;

import api.Main;
import api.Security.JwtCore;
import api.Configs.HashPassword;
import api.Service.UserService;
import api.dto.SigninRequest;
import api.dto.SignupRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class SecurityController {
    private final UserService userService;
    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    private JwtCore jwtCore;

    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:3000")
    ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        String serviceResult = userService.newUser(signupRequest);
        if (Objects.equals(serviceResult, "Выберите другое имя")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceResult);
        }
        if (Objects.equals(serviceResult, "Выберите другую почту")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceResult);
        }
        return ResponseEntity.ok("Вы успешно зарегистрированы. Теперь можете войти в свой аккаунт.");
    }
    @PostMapping("/signin")
    @CrossOrigin(origins = "http://localhost:3000")
    ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest) {

        HashPassword hashPassword = new HashPassword();

        signinRequest.setPassword(hashPassword.hashUserPassword(signinRequest.getPassword()));

        UserDetails user = userService.loadUserByUsername(signinRequest.getUserName());

        if (Objects.equals(user, null) || !Objects.equals(user.getPassword(), signinRequest.getPassword())) {
            log.info("Ошибка авторизации пользователя " + signinRequest.getUserName());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String jwt = jwtCore.generateToken(user);

        Main.currentUser = userService.loadUserEntityByUsername(signinRequest.getUserName());
        log.info("Вход прошёл успешно");
        return ResponseEntity.ok(jwt);
    }
}