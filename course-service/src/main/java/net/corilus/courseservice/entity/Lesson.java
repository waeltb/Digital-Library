package net.corilus.courseservice.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public final class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int id;
    private final String title;
    @ManyToOne
    private final Course course;
    @OneToMany(mappedBy = "lesson")
    private final List<Slide> slides;

    private Lesson(Lesson.Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.course = builder.course;
        this.slides = builder.slides != null ? builder.slides : new ArrayList<>();
    }

    public Lesson() {
        this.id = 0;
        this.title = null;
        this.course = null;
        this.slides = new ArrayList<>();
    }

    public Course getCourse() {
        return course;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Slide> getSlides() {
        return slides;
    }

    // Builder static class
    public static class Builder {
        private int id;
        private String title;
        private Course course;
        private List<Slide> slides;

        public Lesson.Builder id(int id) {
            this.id = id;
            return this;
        }

        public Lesson.Builder title(String title) {
            this.title = title;
            return this;
        }

        public Lesson.Builder course(Course course) {
            this.course = course;
            return this;
        }

        public Lesson.Builder slides(List<Slide> slides) {
            this.slides = slides;
            return this;
        }

        public Lesson build() {
            return new Lesson(this);
        }
    }
}
