package net.corilus.newsservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.corilus.newsservice.enums.ExperienceLevel;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as =ImmutableJobOfferDto.class)

public interface JobOfferDto {
    Integer getOfferId();
    Integer getUserId();
    String getTitle();
    String getContext();
    String getMissions();
    String getProfile();
    String getSkills();
    String getCompany();
    String getLocation();
    String getSalaryRange();
    String getLogo();
    ExperienceLevel getExperienceLevel();
}
