package uz.pdp.kiyim_online_dokon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.kiyim_online_dokon.dto.authdto.AuthResponse;

@Tag(name ="1. Authentication", description = "Register, login, refresh token")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Ro'yxatdan o'tish", description = "Yangi foydalanuvchi yaratish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Muvaffaqiyatli"),
            @ApiResponse(responseCode = "400", description = "Email yoki telefon band")
    })
    @PostMapping("/regiter")
    public ResponseEntity<ApiResponse<AuthResponse>> regiter(@Valid @ResponseBody RegisterRequest dto) {
        return ResponseEntity.ok(ApiResponse.success(authService.register(dto)));
    }

    @Operation(summary = "Kiritish", description = "Eamil va parol orqali login")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<ApiResponse>> login(@Valid@ResponseBody LoginRequest dto){
        return ResponseEntity.ok(ApiResponse.success(authService.login(dto)))
    }

    @Operation(summary = "Token yangilash")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshTokenRequest dto) {
        return ResponseEntity.ok(ApiResponse.success(authService.refreshToken(dto.getRefreshToken())));
    }

}
