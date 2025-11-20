package uz.pdp.kiyim_online_dokon.dto.rivewdto;

import java.time.LocalDateTime;

public record ReviewDto(Long id, Integer rating, String comment, String userName, LocalDateTime createdAt) {}