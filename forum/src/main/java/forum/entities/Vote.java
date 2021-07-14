package forum.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(
        name = "votes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "user_id"})}
)
public class Vote {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column
    private long upVotes;

    @Column
    private long downVotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    public long getId() {
        return this.id;
    }

    public long getUpVotes() {
        return this.upVotes;
    }

    public long getDownVotes() {
        return this.downVotes;
    }

    public Post getPost() {
        return this.post;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUpVotes(long upVotes) {
        this.upVotes = upVotes;
    }

    public void setDownVotes(long downVotes) {
        this.downVotes = downVotes;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
