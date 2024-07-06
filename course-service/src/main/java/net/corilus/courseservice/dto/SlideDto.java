package net.corilus.courseservice.dto;


import org.immutables.value.Value;

@Value.Immutable

public interface SlideDto {
    String title();
    String order();
    String pathvideo() ;

}
