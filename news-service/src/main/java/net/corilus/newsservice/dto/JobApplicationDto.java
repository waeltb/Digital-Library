package net.corilus.newsservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.corilus.newsservice.enums.ApplicationStatus;
import org.immutables.value.Value;

import java.util.Date;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as =ImmutableJobApplicationDto.class)

public interface JobApplicationDto {
    Long getJobOfferId();
    Integer getApplicationId();
    Integer getUserId();
    Date getApplicationDate();
    ApplicationStatus getApplicationStatus();
    String getCoverLetter();
    String getCv();
    String getFirstName();
    String getLastName();
    String getContainername();
    String getEmail();
    Optional<String> getCompanyName();
    Optional<String> getTitleOffer();
}
