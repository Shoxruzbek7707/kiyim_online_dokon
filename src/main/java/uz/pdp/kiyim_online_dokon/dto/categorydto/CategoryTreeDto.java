package uz.pdp.kiyim_online_dokon.dto.categorydto;

import java.util.List;

public record CategoryTreeDto(Long id, String name, List<CategoryTreeDto> children) {}