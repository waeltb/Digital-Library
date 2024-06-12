package net.corilus.courseservice.entity;

import jakarta.persistence.*;


@Entity
public final class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int id;
    private final String title;
    @ManyToOne
    private final  Course course;

    private Lesson(Lesson.Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.course = builder.course;
    }

    public Lesson() {
        this.id = 0;
        this.title = null;
        this.course = null;

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



    // Builder static class
    public static class Builder {
        private int id;
        private String title;
        private Course course;

        public Lesson.Builder id(int id) {
            this.id = id;
            return this;
        }

        public Lesson.Builder title(String title) {
            this.title = title;
            return this;
        }
        public Builder course(Course course) {
            this.course = course;
            return this;
        }


        public Lesson build() {
            return new Lesson(this);
        }
    }
}

