package com.ficklebytes.controller;

import static com.ficklebytes.utils.JWTUtils.AUTHORIZATION_HEADER_NAME;
import static com.ficklebytes.utils.JWTUtils.IP_ADDRESS_CLAIM_NAME;
import static com.ficklebytes.utils.JWTUtils.TOKEN_PREFIX;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ficklebytes.Status;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.PasswordResetToken;
import com.ficklebytes.enums.ActivityName;
import com.ficklebytes.enums.Role;
import com.ficklebytes.exception.UnAuthenticationException;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.exception.UserNotFoundException;
import com.ficklebytes.reply.FacebookAuthReply;
import com.ficklebytes.request.auth.ChangePasswordRequest;
import com.ficklebytes.request.auth.LoginRequest;
import com.ficklebytes.request.auth.RegistrationRequest;
import com.ficklebytes.request.auth.ResetPasswordRequest;
import com.ficklebytes.service.ActivityService;
import com.ficklebytes.service.MailService;
import com.ficklebytes.service.PasswordResetService;
import com.ficklebytes.service.UserService;
import com.ficklebytes.utils.JWTUtils;
import com.ficklebytes.utils.Utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@Log4j2
public class AuthController {

  @Autowired
  private UserService userService;

  @Autowired
  private MailService mailService;

  @Autowired
  private ActivityService activityService;

  @Autowired
  private PasswordResetService passwordResetService;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity loginFromUsernameAndPassword(@RequestBody LoginRequest loginRequest,
      HttpServletRequest request)
      throws UnAuthorizedException {
    log.info("Login Request " + Utils.ClassToString(loginRequest));
    try {
      AcademyUser academyUser =
          userService.GetUserByEmailOrUsername(loginRequest.getEmail());
      if (!academyUser.getPassword().equalsIgnoreCase(Utils.MD5(loginRequest.getPassword()))) {
        throw new UnAuthorizedException();
      }
      activityService.registerActivity(academyUser.getUserId(), ActivityName.LOGIN);

      String ip = Utils.getRemoteAddress(request);
      String token = JWTUtils.createToken(academyUser.getUsername(), ip);
      academyUser.setToken(token);
      userService.Save(academyUser);
      return ResponseEntity.ok().header(JWTUtils.AUTHORIZATION_HEADER_NAME, token)
          .body(academyUser);
    } catch (UserNotFoundException exception) {
      throw new UnAuthorizedException();
    }
  }

  @RequestMapping(value = "/login-from-facebook", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity loginFromFacebook(@RequestBody String accessToken,
      HttpServletRequest request) throws UnAuthorizedException, UserNotFoundException {
    Status<FacebookAuthReply> facebookAuthReplyStatus = Utils.GetFromAPIURL(
        "https://graph.facebook.com/me?access_token=" + accessToken + "&fields=id,email,name",
        new TypeReference<FacebookAuthReply>() {
        });
    if (facebookAuthReplyStatus.getValue() == null) {
      throw new UnAuthorizedException();
    }
    if (facebookAuthReplyStatus.hasError()) {
      throw new UnAuthorizedException();
    }
    FacebookAuthReply facebookAuthReply =
        facebookAuthReplyStatus.getValue();
    log.info("Facebook Request " + Utils.ClassToString(facebookAuthReply));
    String uniqueEmail = facebookAuthReply.id + "@mawhoubacademy.com";
    if (facebookAuthReply.email == null) {
      facebookAuthReply.setEmail(uniqueEmail);
      log.info("No email found changing to " + facebookAuthReply.getEmail());
    }
    if (userService.IsEmailExists(uniqueEmail)) {
      facebookAuthReply.setEmail(uniqueEmail);
    }
    String ip = Utils.getRemoteAddress(request);
    String token = JWTUtils.createToken(facebookAuthReply.getEmail(), ip);
    AcademyUser academyUser = null;
    if (!userService.IsEmailExists(facebookAuthReply.email)) {
      academyUser = AcademyUser.builder()
          .email(facebookAuthReply.email)
          .password(Utils.MD5(Utils.GenerateLowercaseString(32)))
          .phoneNumber("")
          .fullName(facebookAuthReply.name)
          .biography("")
          .pictureUrl("")
          .role(Role.STUDENT)
          .token(token)
          .creationTimeDate(LocalDateTime.now(ZoneOffset.UTC))
          .validated(true)
          .build();
    } else {
      academyUser =
          userService.GetUserByEmailOrUsername(facebookAuthReply.email);
      academyUser.setToken(token);
    }
    academyUser = userService.Save(academyUser);
    activityService.registerActivity(academyUser.getUserId(), ActivityName.LOGIN);

    return ResponseEntity.ok()
        .header(JWTUtils.AUTHORIZATION_HEADER_NAME, token)
        .body(academyUser);

  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity register(@RequestBody RegistrationRequest registrationRequest,
      HttpServletRequest request) {
    if (userService.IsEmailExists(registrationRequest.getEmail())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    String ip = Utils.getRemoteAddress(request);
    String token = JWTUtils.createToken(registrationRequest.getEmail(), ip);
    AcademyUser academyUser = AcademyUser.builder()
        .email(registrationRequest.getEmail())
        .password(Utils.MD5(registrationRequest.getPassword()))
        .phoneNumber(registrationRequest.getPhoneNumber())
        .fullName(registrationRequest.getFullName())
        .biography("")
        .pictureUrl("")
        .role(Role.STUDENT)
        .creationTimeDate(LocalDateTime.now(ZoneOffset.UTC))
        .token(token)
        .validated(true)
        .build();
    academyUser = userService.Save(academyUser);
    return ResponseEntity.ok()
        .header(JWTUtils.AUTHORIZATION_HEADER_NAME,
            token)
        .body(academyUser);
  }

  @PostMapping(value = "/change-password")
  @ResponseBody
  public ResponseEntity changePassword(@RequestBody ChangePasswordRequest request)
          throws UnAuthenticationException, UserNotFoundException, UnAuthorizedException {
    AcademyUser academyUser = userService.GetUserByEmailOrUsername(request.getEmail());
    if (!Utils.GenerateToken(academyUser.getPassword()).equalsIgnoreCase(request.getToken())) {
      throw new UnAuthorizedException();
    }
    academyUser.setPassword(Utils.MD5(request.getNewPassword()).toLowerCase());
    userService.Save(academyUser);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/send-reset-token")
  public ResponseEntity<String> sentPasswordResetToken(@RequestParam("email") String email) throws UserNotFoundException, IOException {

    Optional<AcademyUser> user = userService.getUserByEmail(email);
    String token;
    Optional<PasswordResetToken> resetTokenOpt;

    do {
      token = generateResetToken();
      resetTokenOpt = passwordResetService.findByToken(token);
    } while(resetTokenOpt.isPresent());

    passwordResetService.savePasswordResetToken(user.get(), token);
    mailService.sendPasswordReset(user.get(), token);

    return ResponseEntity.ok("Reset token has been sent to your email.");
  }

  @PostMapping(value = "/reset-password")
  public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest req) throws UserNotFoundException {
    Optional<PasswordResetToken> resetTokenOpt = passwordResetService.findByToken(req.getToken());

    if (!resetTokenOpt.isPresent()) {
      return ResponseEntity.badRequest().body("Invalid token.");
    }

    PasswordResetToken resetToken = resetTokenOpt.get();

    if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
      return ResponseEntity.badRequest().body("Token has expired.");
    }

    AcademyUser user = userService.GetUserById(resetToken.getUserId());
    String newPasswordHashing = Utils.MD5(req.getNewPassword());
    user.setPassword(newPasswordHashing);

    userService.Save(user);
    passwordResetService.invalidateToken(resetToken);
    return ResponseEntity.ok("Password has been successfully reset.");
  }

  @GetMapping(value = "/is-email-used")
  @ResponseBody
  public ResponseEntity<Boolean> isEmailUsed(@RequestParam(name = "email") String email) {
    return ResponseEntity.ok(userService.IsEmailExists(email));
  }

  @RequestMapping(value = "/verify-user-token", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity verifyUserToken(@AuthenticationPrincipal AcademyUser academyUser,
      HttpServletRequest request)
      throws UnAuthorizedException {
    String requestToken = request.getHeader(AUTHORIZATION_HEADER_NAME);
    requestToken = requestToken.replace(TOKEN_PREFIX, "");
    String requestIpAddress = Utils.getRemoteAddress(request);
    String tokenIpAddress = JWTUtils.extractClaim(requestToken, IP_ADDRESS_CLAIM_NAME);
    String userToken = academyUser.getToken();
    if (!userToken.equals(requestToken) || !requestIpAddress.equals(tokenIpAddress)) {
      throw new UnAuthorizedException();
    }
    return ResponseEntity.ok().build();
  }

  private String generateResetToken() {
    return UUID.randomUUID().toString().substring(0, 8);
  }


}
