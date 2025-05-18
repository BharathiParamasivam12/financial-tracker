package  com.finance.user.controller;

import com.finance.user.model.User;
import com.finance.user.model.UserInfo;
import com.finance.user.response.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.finance.user.service.UserService;
import org.springframework.http.HttpStatus;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Slf4j
@RestController
@RequestMapping("v1/users")
public class UserController {
    @Autowired(required = true)
    UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @ApiOperation(value = "Register a new user", notes = "Creates a new user account with the provided credentials")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User successfully registered"),
            @ApiResponse(code = 400, message = "Invalid input data"),
            @ApiResponse(code = 409, message = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseModel> registerUser(@RequestBody User registrationDTO) {
        ResponseModel<Object> responseModel = new ResponseModel<>();
        try {
            userService.registerUser(registrationDTO);
            responseModel.setStatus("success");
            responseModel.setMessage("User registered successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
        } catch (Exception e) {
            responseModel.setStatus("failed");
            responseModel.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }

    }

    @ApiOperation(value = "Get user details", notes = "Retrieves user information based on the provided username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User details retrieved successfully"),
            @ApiResponse(code = 400, message = "Invalid input data"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @GetMapping("/user/{username}")
    public ResponseEntity<ResponseModel> getUser(@RequestHeader("X-Gateway-Auth") String gatewayAuth,
                                                 @PathVariable String username) {
        ResponseModel<Object> responseModel = new ResponseModel<>();
        try {
            logger.info("Fetching user details for username: {}", username);
            responseModel.setStatus("success");
            responseModel.setMessage("User details fetched successfully");
            responseModel.setData(userService.getUser(username));
            logger.info("User details fetched successfully for data: {}", responseModel.getData());
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (Exception e) {
            responseModel.setStatus("failed");
            responseModel.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
        }

    }

    @ApiOperation(value = "Add user information", notes = "Creates additional user information for an existing user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User information added successfully"),
            @ApiResponse(code = 400, message = "Invalid input data"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @PostMapping("/addUserInfo")
    public ResponseEntity<ResponseModel> addUserInfo(@RequestBody UserInfo userInfo) {
        ResponseModel<Object> responseModel = new ResponseModel<>();
        try {
            userService.createUserInfo(userInfo);
            responseModel.setStatus("success");
            responseModel.setMessage("User info created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
        } catch (Exception e) {
            responseModel.setStatus("failed");
            responseModel.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
    }
}