package com.truevocation.web.rest;

import com.truevocation.domain.Authority;
import com.truevocation.domain.User;
import com.truevocation.repository.UserRepository;
import com.truevocation.security.SecurityUtils;
import com.truevocation.service.AppUserService;
import com.truevocation.service.MailService;
import com.truevocation.service.UserService;
import com.truevocation.service.dto.*;
import com.truevocation.web.rest.errors.*;
import com.truevocation.web.rest.vm.KeyAndPasswordVM;
import com.truevocation.web.rest.vm.ManagedUserVM;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.truevocation.web.rest.vm.UserAccountDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/account")
public class AccountResource {

    @Value("${truevocation.is-production}")
    private boolean isProduction;

    private static final String CLIENT_URL_LOCALHOST = "http://localhost:3000/sign-in";

    private static final String CLIENT_URL_PRODUCTION = "http://localhost:9000";

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    @Autowired
    private AppUserService appUserService;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        mailService.sendActivationEmail(user);
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public RedirectView activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
        String url = isProduction ? CLIENT_URL_PRODUCTION : CLIENT_URL_LOCALHOST;
        return new RedirectView(url);
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping
    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }


    @GetMapping("/user")
    public UserAccountDto getUserAccount() {
        return userService
            .getUserWithAuthorities()
            .map(user ->{
                UserAccountDto dto = new UserAccountDto();
                AppUserDTO appUserDTO = appUserService.findByUserId(user.getId()).orElse(null);
                if(!Objects.isNull(appUserDTO)){
                    dto.setBirthdate(appUserDTO.getBirthdate());
                    dto.setPhoneNumber(appUserDTO.getPhoneNumber());
                    dto.setAppUserId(appUserDTO.getId());
                }
                Set<Authority> authorities = user.getAuthorities();
                Set<String> stringAuthorities = authorities.stream().map(Authority::getName).collect(Collectors.toSet());
                dto.setAuthorities(stringAuthorities);
                dto.setActivated(user.isActivated());
                dto.setEmail(user.getEmail());
                dto.setFirstName(user.getFirstName());
                dto.setLastName(user.getLastName());
                dto.setId(user.getId());
                dto.setImageUrl(user.getImageUrl());
                dto.setLogin(user.getLogin());
                return dto;
            })
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }



    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException          {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException         {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
                password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
                password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }


    @PostMapping(value = "/uploadAvatar/{id}")
//    @PreAuthorize("hasRole(ROLE_ADMIN)")
    public ResponseEntity<User> uploadPicture(@RequestBody MultipartFile file,
                                              @PathVariable(name = "id") Long id) {
        User user = userService.saveAvatar(file, id);
        if (!Objects.isNull(user)) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }


    @GetMapping(value = "/viewAvatar/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public ResponseEntity<byte[]> viewAvatar(@PathVariable("id") Long id,
                                                  @RequestParam(name = "url") String url) throws IOException {
        return userService.getAvatar(id, url);
    }


    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUserAccount(@Valid @RequestBody UserAccountDto userAccountDto) {
        if (isPasswordLengthInvalid(userAccountDto.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUserAccount(userAccountDto, userAccountDto.getPassword());
        mailService.sendActivationEmail(user);
    }

    @PostMapping("/check-email")
    public boolean checkEmail(@Valid @RequestParam String email ) {
        return userService.checkEmail(email);
    }

    @PostMapping("/check-login")
    public boolean checkLogin(@Valid @RequestParam String login ) {
        return userService.checkLogin(login);
    }

    @PostMapping("/update-profile")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserAccountDto> updateProfile(@RequestBody UserAccountDto userAccountDto) {
        UserAccountDto user = userService.updateProfile(userAccountDto);
        return ResponseEntity.ok(user);
    }
}
