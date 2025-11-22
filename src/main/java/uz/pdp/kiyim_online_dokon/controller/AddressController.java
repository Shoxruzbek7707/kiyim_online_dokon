package uz.pdp.kiyim_online_dokon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.kiyim_online_dokon.dto.addressdto.AddressCreateRequest;
import uz.pdp.kiyim_online_dokon.dto.addressdto.AddressDto;

import java.security.Principal;
import java.util.List;


@Tag(name = "3. Addresses", description = "Yetkazib berish manzillari")
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class AddressController{

    private final AddressService addressService;

    @Operation(summary = "Yangi manzil qo'shish")
    @PostMapping
    public ResponseEntity<ApiResponse<AddressDto>> create(@Valid @RequestBody AddressCreateRequest dto, Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(addressService.create(dto, principal)));
    }

    @Operation(summary = "Mening barcha manzillarim")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressDto>>> getMyAddresses(Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(addressService.getMyAddresses(principal)));
    }

    @Operation(summary = "Manzilni yangilash")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> update(@PathVariable Long id, @Valid @RequestBody AddressUpdateRequest dto, Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(addressService.update(id, dto, principal)));
    }

    @Operation(summary = "Manzilni o'chirish")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id, Principal principal) {
        addressService.delete(id, principal);
        return ResponseEntity.ok(ApiResponse.success("Address deleted"));
    }
}