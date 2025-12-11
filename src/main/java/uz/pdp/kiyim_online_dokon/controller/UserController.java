package uz.pdp.kiyim_online_dokon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.kiyim_online_dokon.dto.UserDTO;
import uz.pdp.kiyim_online_dokon.service.impl.UsersServiceImpl;


import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "Operations related to users")
public class UserController {

    @Autowired
    UsersServiceImpl userServiceImpl;

    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping
    public List<UserDTO> getAll() {
        return userServiceImpl.getAllUsers();
    }
}
