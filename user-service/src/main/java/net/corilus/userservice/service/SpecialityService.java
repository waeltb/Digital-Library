package net.corilus.userservice.service;

import net.corilus.userservice.dto.SpecialityDto;
import net.corilus.userservice.entity.Speciality;

import java.util.List;

public interface SpecialityService {
    List<SpecialityDto> getSpecialities();

    SpecialityDto getSpeciality(String specialityName);

    void createSpeciality( SpecialityDto specialityDto);

    void updateSpeciality(SpecialityDto specialityDto ,String specialityName);

    void deleteSpeciality(String specialityName);
     Speciality convertToEntity(SpecialityDto specialityDto) ;

}
