package org.forstudy.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(length = 2000)
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "post",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)

    private Set<Vote> votes;

    @Column(name = "created_date")
    private LocalDateTime dateCreated;

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public long getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public User getAuthor() {
        return this.author;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public LocalDateTime getDateCreated() {
        return this.dateCreated;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}