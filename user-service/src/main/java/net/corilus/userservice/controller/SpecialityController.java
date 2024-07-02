package net.corilus.userservice.controller;

import net.corilus.userservice.dto.SpecialityDto;
import net.corilus.userservice.service.SpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/speciality")
public class SpecialityController {
    @Autowired
    private SpecialityService specialityService;

    @PostMapping("/addSpeciality")
    public String addSpeciality(@RequestBody SpecialityDto speciality) {
        specialityService.createRole(speciality);
        return "success";
    }

    @DeleteMapping("/deleteSpeciality/{specialityName}")
    public String deleteSpeciality(@PathVariable("specialityName") String specialityName) {
        specialityService.deleteSpeciality(specialityName);
        return "success";
    }

    @PutMapping("/updateSpeciality/{specialityName}")
    public String updateSpeciality(@RequestBody SpecialityDto specialityDto, @PathVariable String specialityName) {
        specialityService.updateSpeciality(specialityDto, specialityName);
        return "success";
    }

    @GetMapping("/getSpecialities")
    public List<SpecialityDto> getSpecialities() {
        return specialityService.getSpecialities();
    }

    @GetMapping("/getSpeciality/{specialityName}")
    public SpecialityDto getSpeciality(@PathVariable String specialityName) {
        return specialityService.getSpeciality(specialityName);
    }
}
