package net.corilus.newsservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.corilus.newsservice.enums.Categories;
import net.corilus.newsservice.enums.ExperienceLevel;
import org.immutables.value.Value;

import java.util.Date;

@Value.Immutable
@JsonDeserialize(as =ImmutableJobOfferDto.class)

public interface JobOfferDto {
    Integer getOfferId();
    Integer getUserId();
    String getTitle();

    String getMissions();

    String getSkills();
    String getCompany();
    String getLocation();
    String getSalaryRange();
    String getLogo();
    ExperienceLevel getExperienceLevel();
    Categories getCategories();
    Date getEndDateForSending();
    String getContainername();
}
