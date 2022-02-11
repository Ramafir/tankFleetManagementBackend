package pl.daniel.kolban.tankfleetmanagement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import pl.daniel.kolban.tankfleetmanagement.domain.HttpResponse;
import pl.daniel.kolban.tankfleetmanagement.exception.ExceptionHandling;
import pl.daniel.kolban.tankfleetmanagement.exception.domain.EmailExistException;
import pl.daniel.kolban.tankfleetmanagement.exception.domain.UserNotFoundException;
import pl.daniel.kolban.tankfleetmanagement.exception.domain.UsernameExistException;
import pl.daniel.kolban.tankfleetmanagement.tank.Tank;
import pl.daniel.kolban.tankfleetmanagement.utility.JWTTokenProvider;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static pl.daniel.kolban.tankfleetmanagement.constant.SecurityConstant.JWT_TOKEN_HEADER;


@RestController
@RequestMapping("api/users")
public class UserResource extends ExceptionHandling {
    public static final String USER_DELETED_SUCCESSFULLY = "User Deleted Successfully";
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserResource(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User newUser = userService.register(user.getFullName(), user.getCountry(), user.getUsername(), user.getPassword(),  user.getEmail());
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("")
    public ResponseEntity<User> addNewUser(@RequestParam("fullName") String fullName,
                                           @RequestParam("username") String username,
                                           @RequestParam("country") String country,
                                           @RequestParam("email") String email,
                                           @RequestParam("password") String password,
                                           @RequestParam("isActive") String isActive,
                                           @RequestParam("isNonLocked") String isNonLocked,
                                           @RequestParam("hasAtomicButton") String hasAtomicButton) throws UserNotFoundException, UsernameExistException {

        User newUser = userService.addNewUser(fullName, username, email, password, country, Boolean.parseBoolean(isActive), Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(hasAtomicButton));
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestParam("currentUsername") String currentUsername,
                                           @RequestParam("fullName") String fullName,
                                           @RequestParam("username") String username,
                                           @RequestParam("country") String country,
                                           @RequestParam("email") String email,
                                           @RequestParam("role") String role,
                                           @RequestParam("isActive") String isActive,
                                           @RequestParam("isNonLocked") String isNonLocked,
                                           @RequestParam("hasAtomicButton") String hasAtomicButton) throws UserNotFoundException, UsernameExistException {

        User updatedUser = userService.updateUser(currentUsername, fullName, username, country, email, role, Boolean.parseBoolean(isActive), Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(hasAtomicButton));
        return new ResponseEntity<>(updatedUser, OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, OK);
    }

    @GetMapping("/{id}/tanks")
    public ResponseEntity<List<Tank>> getUserTanks(@PathVariable("id") Long id) {
        List<Tank> tanks = userService.getTanks(id);
        return new ResponseEntity<>(tanks, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return response(OK, USER_DELETED_SUCCESSFULLY);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message.toUpperCase()), httpStatus);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
