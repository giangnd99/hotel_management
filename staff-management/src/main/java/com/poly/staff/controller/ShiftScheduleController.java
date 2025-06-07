package com.poly.staff.controller;

import com.poly.staff.model.ShiftScheduleModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/shift-schedules")
public class ShiftScheduleController {

    @GetMapping
    public ResponseEntity<List<ShiftScheduleModel>> getAll() {
        List<ShiftScheduleModel> list = Arrays.asList(
            ShiftScheduleModel.builder()
                .scheduleId(1)
                .staffId("S001")
                .workDate(LocalDate.now())
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(16, 0))
                .build(),
            ShiftScheduleModel.builder()
                .scheduleId(2)
                .staffId("S002")
                .workDate(LocalDate.now())
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .build()
        );
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftScheduleModel> getById(@PathVariable Integer id) {
        ShiftScheduleModel model = ShiftScheduleModel.builder()
            .scheduleId(id)
            .staffId("S00" + id)
            .workDate(LocalDate.now())
            .startTime(LocalTime.of(8, 0))
            .endTime(LocalTime.of(16, 0))
            .build();
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<ShiftScheduleModel> create(@RequestBody ShiftScheduleModel model) {
        ShiftScheduleModel created = ShiftScheduleModel.builder()
            .scheduleId(100)
            .staffId(model.getStaffId())
            .workDate(model.getWorkDate())
            .startTime(model.getStartTime())
            .endTime(model.getEndTime())
            .build();
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShiftScheduleModel> update(@PathVariable Integer id, @RequestBody ShiftScheduleModel model) {
        ShiftScheduleModel updated = ShiftScheduleModel.builder()
            .scheduleId(id)
            .staffId(model.getStaffId())
            .workDate(model.getWorkDate())
            .startTime(model.getStartTime())
            .endTime(model.getEndTime())
            .build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return ResponseEntity.noContent().build();
    }
}
