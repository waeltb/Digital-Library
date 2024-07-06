package net.corilus.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.corilus.userservice.dto.ImmutableSpecialityDto;
import net.corilus.userservice.dto.SpecialityDto;
import net.corilus.userservice.entity.Speciality;
import net.corilus.userservice.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpecialityServiceImpl implements SpecialityService {
    @Autowired
    private SpecialityRepository specialityRepository;

    @Override
    public List<SpecialityDto> getSpecialities() {
        return specialityRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SpecialityDto getSpeciality(String specialityName) {
        Speciality speciality = specialityRepository.findByName(specialityName);
        if (speciality != null) {
            return convertToDto(speciality);
        } else {
            return null; // Vous pouvez lancer une exception si la spécialité n'existe pas
        }
    }

    @Override
    public void createSpeciality(SpecialityDto specialityDto) {
        Speciality speciality = convertToEntity(specialityDto);
        specialityRepository.save(speciality);
    }

    @Override
    public void updateSpeciality(SpecialityDto specialityDto, String specialityName) {
        Speciality existingSpeciality = specialityRepository.findByName(specialityName);
        if (existingSpeciality != null) {
            existingSpeciality.setName(specialityDto.getName());
            specialityRepository.save(existingSpeciality);
        } else {
            // Vous pouvez lancer une exception si la spécialité n'existe pas
        }
    }

    @Override
    @Transactional
    public void deleteSpeciality(String specialityName) {
        specialityRepository.deleteSpecialityByName(specialityName);
    }

    @Override
    public Speciality convertToEntity(SpecialityDto specialityDto) {
        return Speciality.builder()
                .name(specialityDto.getName())
                .build();
    }

    private SpecialityDto convertToDto(Speciality speciality) {
        return ImmutableSpecialityDto.builder()
                .name(speciality.getName())
                .build();
    }
}
