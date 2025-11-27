package uz.pdp.kiyim_online_dokon.service.interfaces;

import uz.pdp.kiyim_online_dokon.dto.AddressDTO;

import java.util.List;

public interface AddressesService {

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Integer id);

    AddressDTO createAddress(AddressDTO addressDTO);

    AddressDTO updateAddress(Integer id, AddressDTO addressDTO);

    void deleteAddress(Integer id);

    List<AddressDTO> getAddressesByUserId(Integer userId);
}
