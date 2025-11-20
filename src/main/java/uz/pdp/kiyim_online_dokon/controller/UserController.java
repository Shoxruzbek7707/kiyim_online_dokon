package uz.pdp.kiyim_online_dokon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "2. User Profile",description = "Shaxsiy kabinet va buyrutmalar")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")

public class UserController {
    private final UserService userService;

    @Operation(summary = "Joriy foydalanuvchi ma'lumotlari")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getMe(Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(userService.getMe(principal)));
    }

    @Operation(summary = "Profilni yangilash")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateMe(@Valid @RequestBody UpdateProfileRequest dto, Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateProfile(dto, principal)));
    }

    @Operation(summary = "Mening buyurtmalarim")
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getMyOrders(Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(userService.getMyOrders(principal)));
    }
}
