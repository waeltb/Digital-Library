package net.corilus.courseservice.entity;

import jakarta.persistence.*;

@Entity
public final class Slide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int id;
    private final String title;
    @ManyToOne
    private final Lesson lesson;
    @Column(name = "`order`")
    private final int order;
    private final String pathvideo;

    private Slide(Slide.Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.lesson = builder.lesson;
        this.order = builder.order;
        this.pathvideo = builder.pathvideo;
    }

    public Slide() {
        this.id = 0;
        this.title = null;
        this.lesson = null;
        this.order = 0;
        this.pathvideo = null;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public String getPathvideo() {
        return pathvideo;
    }

    public int getOrder() {
        return order;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    // Builder static class
    public static class Builder {
        private int id;
        private String title;
        private Lesson lesson;
        private int order;
        private String pathvideo;

        public Slide.Builder id(int id) {
            this.id = id;
            return this;
        }

        public Slide.Builder title(String title) {
            this.title = title;
            return this;
        }

        public Slide.Builder lesson(Lesson lesson) {
            this.lesson = lesson;
            return this;
        }

        public Slide.Builder order(int order) {
            this.order = order;
            return this;
        }

        public Slide.Builder pathvideo(String pathvideo) {
            this.pathvideo = pathvideo;
            return this;
        }

        public Slide build() {
            return new Slide(this);
        }
    }
}
