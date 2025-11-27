package uz.pdp.kiyim_online_dokon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.kiyim_online_dokon.dto.dashboarddto.DailySalesDto;
import uz.pdp.kiyim_online_dokon.dto.dashboarddto.DashboardStatsDto;

import java.util.List;

@Tag(name = "12. Admin - Dashboard", description = "Statistika va umumiy ko'rsatkichlar")
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Umumiy statistika")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getStats()));
    }

    @Operation(summary = "So'nggi 7 kunlik savdo")
    @GetMapping("/sales/last7days")
    public ResponseEntity<ApiResponse<List<DailySalesDto>>> getLast7DaysSales() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getLast7DaysSales()));
    }
}