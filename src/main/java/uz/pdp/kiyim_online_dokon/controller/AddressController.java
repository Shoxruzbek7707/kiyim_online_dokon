package uz.pdp.kiyim_online_dokon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.kiyim_online_dokon.dto.AddressDTO;
import uz.pdp.kiyim_online_dokon.service.interfaces.AddressesService;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressesService addressesService;

    @GetMapping
    public ResponseEntity<List<AddressDTO>>  getAllAddresses(){
        List<AddressDTO> addresses = addressesService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Integer id){
        AddressDTO addressDTO = addressesService.getAddressById(id);
        return ResponseEntity.ok(addressDTO);
    }

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO){
        AddressDTO created = addressesService.createAddress(addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Integer id, @RequestBody AddressDTO addressDTO){
        AddressDTO updated = addressesService.updateAddress(id,addressDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id){
        addressesService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressDTO>> getAddressesByUser(@PathVariable Integer userId){
        List<AddressDTO> addressDTOS = addressesService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addressDTOS);
    }

}
