package com.ficklebytes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ficklebytes.config.JacksonConfig;
import com.ficklebytes.enums.ContentType;
import com.ficklebytes.enums.Visibility;
import com.ficklebytes.utils.Utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "content")
public class Content implements Serializable {

    @Column(name = "content_id", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contentId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "length")
    private int length;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "is_free")
    private boolean free;

    @Column(name = "creation_time_date")
    @JsonFormat(pattern = JacksonConfig.dateTimeFormat, timezone = JacksonConfig.timeZone, shape = Shape.STRING)
    private LocalDateTime creationTimeDate;

    @Column(name = "visibility")
    @Enumerated(EnumType.ORDINAL)
    private Visibility visibility;

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private ContentType type;

    @Column(name = "order_index")
    private int order;

    @Column(name = "course_id")
    private int courseId;

    @OneToMany(mappedBy = "contentId", fetch = FetchType.LAZY)
//    @BatchSize(size = 20)
    private Set<Document> documents;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", insertable = false, updatable = false)
    private Course course;

    @Transient
    private long unseenCommentsCount;

    @Override
    public String toString() {
        return Utils.ClassToString(this);
    }
}
