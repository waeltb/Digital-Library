package net.corilus.courseservice.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public final class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int id;
    private final String title;
    private final String description;
    private final String imagepath;
    private final Date creationdate;
    private final Date modificationdate;
    private final String reasonforrefusal;
    @Enumerated(EnumType.STRING)
    private final  Status status ;
    @Enumerated(EnumType.STRING)
    private final Level level;
    @OneToMany(mappedBy = "course")
    private final List<Lesson>lessons ;
    private Course(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.imagepath = builder.imagepath;
        this.creationdate = builder.creationdate;
        this.modificationdate = builder.modificationdate;
        this.reasonforrefusal = builder.reasonforrefusal;
         this.status=builder.status;
         this.level=builder.level;
        this.lessons = builder.lessons;

    }

    public Course() {
        this.id = 0;
        this.title = null;
        this.description = null;
        this.imagepath = null;
        this.creationdate = null;
        this.modificationdate = null;
        this.reasonforrefusal = null;
        this.status = null;
        this.level = null;
        this.lessons = null;

    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

    public Level getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public String getImagepath() {
        return imagepath;
    }

    public Date getCreationdate() {
        return new Date(creationdate.getTime());
    }

    public Date getModificationdate() {
        return new Date(modificationdate.getTime());
    }

    public String getReasonforrefusal() {
        return reasonforrefusal;
    }

    // Builder static class
    public static class Builder {
        private int id;
        private String title;
        private String description;
        private String imagepath;
        private Date creationdate;
        private Date modificationdate;
        private String reasonforrefusal;
        private  Status status ;
        private  Level level;
        private List<Lesson> lessons;
        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder imagepath(String imagepath) {
            this.imagepath = imagepath;
            return this;
        }

        public Builder creationdate(Date creationdate) {
            this.creationdate = new Date(creationdate.getTime());
            return this;
        }

        public Builder modificationdate(Date modificationdate) {
            this.modificationdate = new Date(modificationdate.getTime());
            return this;
        }

        public Builder reasonforrefusal(String reasonforrefusal) {
            this.reasonforrefusal = reasonforrefusal;
            return this;
        }
        public Builder status(Status status) {
            this.status=status;
            return this;
        }
        public Builder level(Level level) {
            this.level = level;
            return this;
        }

        public Builder lessons(List<Lesson> lessons) {
            this.lessons = lessons;
            return this;
        }

        public Course build() {
            return new Course(this);
        }
    }
}
