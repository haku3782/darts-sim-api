package com.example.dartssimapi.controller;

import com.example.dartssimapi.dto.SimulationDto.SimulationRequest;
import com.example.dartssimapi.dto.SimulationDto.SimulationResponse;
import com.example.dartssimapi.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.dartssimapi.entity.SimulationRecord;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SimulationController {

    // Serviceの注入
    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/simulate")
    public ResponseEntity<SimulationResponse> simulate(@RequestBody SimulationRequest request) {
        // 本物の物理演算を実行して結果を返す
        SimulationResponse response = simulationService.runSimulation(request);
        return ResponseEntity.ok(response);
    }

    // ▼ 追加: 履歴取得API
    @GetMapping("/history")
    public List<SimulationRecord> getHistory() {
        return simulationService.getHistory();
    }
}