package net.corilus.newsservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.corilus.newsservice.enums.ApplicationStatus;
import org.immutables.value.Value;

import java.util.Date;

@Value.Immutable
@JsonDeserialize(as =ImmutableJobApplicationDto.class)

public interface JobApplicationDto {
    Integer getApplicationId();
    Integer getUserId();
    Date getApplicationDate();
    ApplicationStatus getApplicationStatus();
}
