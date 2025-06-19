package com.poly.report.controller;

import com.poly.report.model.ActivityLogModel;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/activity-logs")
public class ActivityLogController {

    @GetMapping
    public List<ActivityLogModel> getAll() {
        return Arrays.asList(
                ActivityLogModel.builder()
                        .userId("1")
                        .actor("ADMIN")
                        .action("/login")
                        .status("SUCCESS")
                        .message("User logged in")
                        .build(),
                ActivityLogModel.builder()
                        .userId("2")
                        .actor("STAFF")
                        .action("/checkout")
                        .status("FAILED")
                        .message("Checkout failed")
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ActivityLogModel getById(@PathVariable String id) {
        return ActivityLogModel.builder()
                .userId(id)
                .actor("MANAGER")
                .action("/update-profile")
                .status("SUCCESS")
                .message("Profile updated")
                .build();
    }

    @PostMapping
    public ActivityLogModel create(@RequestBody ActivityLogModel model) {
        return ActivityLogModel.builder()
                .userId("3")
                .actor("SYSTEM")
                .action("/auto-backup")
                .status("SUCCESS")
                .message("Backup completed")
                .build();
    }
}
