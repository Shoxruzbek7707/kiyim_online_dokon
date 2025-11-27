package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.dto.AddressDTO;
import uz.pdp.kiyim_online_dokon.entity.Addresses;
import uz.pdp.kiyim_online_dokon.entity.Users;
import uz.pdp.kiyim_online_dokon.repository.AddressesRepository;
import uz.pdp.kiyim_online_dokon.repository.UsersRepository;
import uz.pdp.kiyim_online_dokon.service.interfaces.AddressesService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressesServiceIml implements AddressesService {
    private final AddressesRepository addressesRepository;
    private final UsersRepository usersRepository;

    private AddressDTO toDTO(Addresses address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setUserId(address.getUser() != null ? address.getUser().getId() : null);
        dto.setRegion(address.getRegion());
        dto.setDistrict(address.getDistrict());
        dto.setStreet(address.getStreet());
        dto.setHouse(address.getHouse());
        dto.setApartment(address.getApartment());
        dto.setIsDefault(address.getIsDefault());
        return dto;
    }

    private Addresses toEntity(AddressDTO dto) {
        Addresses address = new Addresses();
        address.setId(dto.getId());
        address.setRegion(dto.getRegion());
        address.setDistrict(dto.getDistrict());
        address.setStreet(dto.getStreet());
        address.setHouse(dto.getHouse());
        address.setApartment(dto.getApartment());
        address.setIsDefault(dto.getIsDefault());
        return address;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        return addressesRepository
                .findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

    }

    @Override
    public AddressDTO getAddressById(Integer id) {
        Addresses a = addressesRepository.findById(id).orElse(null);
        return toDTO(a);
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Addresses addresses = toEntity(addressDTO);
        Users user = usersRepository.findById(addressDTO.getUserId()).orElse(null);
        addresses.setUser(user);

        Addresses saved = addressesRepository.save(addresses);
        return toDTO(saved);
    }

    @Override
    public AddressDTO updateAddress(Integer id, AddressDTO addressDTO) {
        Addresses existing = addressesRepository.findById(id).orElse(null);

        existing.setRegion(addressDTO.getRegion());
        existing.setDistrict(addressDTO.getDistrict());
        existing.setStreet(addressDTO.getStreet());
        existing.setHouse(addressDTO.getHouse());
        existing.setApartment(addressDTO.getApartment());
        existing.setIsDefault(addressDTO.getIsDefault());

        if (addressDTO.getId() != null) {
            Users user = usersRepository.findById(addressDTO.getUserId()).orElse(null);
            existing.setUser(user);
        }

        Addresses updated = addressesRepository.save(existing);
        return toDTO(updated);
    }

    @Override
    public void deleteAddress(Integer id) {
        Addresses existing = addressesRepository.findById(id).orElse(null);
        assert existing != null;
        addressesRepository.delete(existing);

    }

    @Override
    public List<AddressDTO> getAddressesByUserId(Integer userId) {
        Users user = usersRepository.findById(userId).orElse(null);

        return addressesRepository.findAllByUser(user)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
