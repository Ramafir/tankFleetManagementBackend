package pl.daniel.kolban.tankfleetmanagement.user;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.daniel.kolban.tankfleetmanagement.exception.domain.EmailExistException;
import pl.daniel.kolban.tankfleetmanagement.exception.domain.UserNotFoundException;
import pl.daniel.kolban.tankfleetmanagement.exception.domain.UsernameExistException;
import pl.daniel.kolban.tankfleetmanagement.tank.Tank;

import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static pl.daniel.kolban.tankfleetmanagement.constant.UserImplConstant.*;


@Service
@Qualifier("userDetailsService")
@Slf4j
public class UserService implements UserDetailsService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    public User register(String fullName, String country, String username, String password, String email) throws UserNotFoundException, UsernameExistException, EmailExistException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
        user.setFullName(fullName);
        user.setUsername(username);
        user.setCountry(country);
        user.setEmail(email);
        user.setJoinDate(LocalDateTime.now());
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_PRESIDENT.name());
        user.setAuthorities(Role.ROLE_PRESIDENT.getAuthorities());
        user.setHasAtomicButton(checkIfPresidentHasAtomicButton(country));
        userRepository.save(user);
        return user;
    }


    public User addNewUser(String fullName, String username, String password, String email, String country, boolean isNonLocked, boolean isActive, boolean hasAtomicButton) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
        user.setFullName(fullName);
        user.setCountry(country);
        user.setUsername(username);
        user.setJoinDate(LocalDateTime.now());
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setActive(isActive);
        user.setNotLocked(isNonLocked);
        user.setHasAtomicButton(hasAtomicButton);
        user.setRole(Role.ROLE_PRESIDENT.name());
        user.setAuthorities(Role.ROLE_PRESIDENT.getAuthorities());
        userRepository.save(user);
        return user;
    }

    public User updateUser(String currentUsername, String fullName, String username, String country, String email, String role, boolean isNonLocked, boolean isActive, boolean hasAtomicButton) throws UserNotFoundException, EmailExistException, UsernameExistException {
        User currentUser = validateNewUsernameAndEmail(currentUsername, username, email);
        currentUser.setFullName(fullName);
        currentUser.setUsername(username);
        currentUser.setEmail(email);
        currentUser.setCountry(country);
        currentUser.setHasAtomicButton(checkIfPresidentHasAtomicButton(country));
        currentUser.setActive(isActive);
        currentUser.setNotLocked(isNonLocked);
        currentUser.setHasAtomicButton(hasAtomicButton);
        currentUser.setRole(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(currentUser);
        return currentUser;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);

    }

    List<Tank> getTanks(Long userId) {
        return userRepository.findById(userId)
                .map(User::getTanks)
                .orElseThrow();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);
        if(StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }
    private boolean checkIfPresidentHasAtomicButton(String country) {
        return country.equalsIgnoreCase("USA") || country.equalsIgnoreCase("United States") || country.equalsIgnoreCase("China") || country.equalsIgnoreCase("DPRK") || country.equalsIgnoreCase("Russia")
                || country.equalsIgnoreCase("France") || country.equalsIgnoreCase("Great Britain") || country.equalsIgnoreCase("North Korea");
    }

    public User findUserById(Long id) {
        log.info("Fetching user by id: {}", id);
        return userRepository.findById(id).get();
    }
}
