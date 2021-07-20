package org.forstudy.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "topics")
public class Topic {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column
    private String title;

    @Column(name = "created_date")
    private LocalDateTime dateOfCreation;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)

    private Set<Post> posts;

    public long getId() {
        return this.id;
    }

    public User getAuthor() {
        return this.author;
    }

    public Group getGroup() {
        return this.group;
    }

    public String getTitle() {
        return this.title;
    }

    public LocalDateTime getDateOfCreation() {
        return this.dateOfCreation;
    }

    public Set<Post> getPosts() {
        return this.posts;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dateCreated=" + dateOfCreation +
                '}';
    }
}
