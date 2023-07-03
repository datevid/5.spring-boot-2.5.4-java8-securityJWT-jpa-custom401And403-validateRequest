package net.demo.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import net.demo.dto.LoginRequest;
import net.demo.dto.UserNewDTO;
import net.demo.dto.UserResponseDTO;
import net.demo.entity.ResponseApi;
import net.demo.model.AppUser;
import net.demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;


    @PostMapping("/signin")
    @ApiOperation(value = "${UserController.signin}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 401, message = "Invalid username/password supplied")})
    public ResponseEntity<?> signin(
            @ApiParam("LoginRequest request") @RequestBody LoginRequest request, BindingResult valid) {
        ResponseApi response;
        if (valid.hasErrors()) {
            Object detalleValid = this.formatMapMessageList(valid);
            response = new ResponseApi(false, HttpStatus.BAD_REQUEST.toString(), detalleValid);
            return ResponseEntity.badRequest().body(response);
        }

        String signin = userService.signin(request.getUsername(), request.getPassword());
        response = new ResponseApi(true, null, signin);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/signinForm")
    @ApiOperation(value = "${UserController.signin}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 401, message = "Invalid username/password supplied")})
    public String loginForm(
            @ApiParam("Username") @RequestParam String username,
            @ApiParam("Password") @RequestParam String password) {
        String signin;
        signin = userService.signin(username, password);
        return signin;
    }

    @PostMapping("/signup")
    @ApiOperation(value = "${UserController.signup}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 401, message = "Username is already in use")})
    public ResponseEntity<?> signup(@ApiParam("Signup User") @RequestBody UserNewDTO user, BindingResult valid) {
        ResponseApi response;
        if (valid.hasErrors()) {
            Object detalleValid = this.formatMapMessageList(valid);
            response = new ResponseApi(false, HttpStatus.BAD_REQUEST.toString(), detalleValid);
            return ResponseEntity.badRequest().body(response);
        }
        AppUser appUser = modelMapper.map(user, AppUser.class);
        String signup = userService.signup(appUser);
        ResponseApi responseApi = new ResponseApi(true, null, signup);
        return ResponseEntity.ok(responseApi);
    }

    @DeleteMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "${UserController.delete}", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "The user doesn't exist"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public String delete(@ApiParam("Username") @PathVariable String username) {
        userService.delete(username);
        return username;
    }

    @GetMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "${UserController.search}", response = UserResponseDTO.class, authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "The user doesn't exist"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseApi search(@ApiParam("Username") @PathVariable String username) {
        AppUser appUser = userService.search(username);
        UserResponseDTO userResponseDTO = modelMapper.map(appUser, UserResponseDTO.class);
        return new ResponseApi(userResponseDTO);
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseApi whoami(HttpServletRequest req) {
        AppUser appUser = userService.whoami(req);
        UserResponseDTO userResponseDTO = modelMapper.map(appUser, UserResponseDTO.class);
        return new ResponseApi(userResponseDTO);
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public String refresh(HttpServletRequest req) {
        String refresh = userService.refresh(req.getRemoteUser());
        return refresh;
    }

    protected List<Map<String, String>> formatMapMessageList(BindingResult result) {

        List<Map<String, String>> errors = result.getFieldErrors().stream().map(err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }

        ).collect(Collectors.toList());
        return errors;

    }
}
