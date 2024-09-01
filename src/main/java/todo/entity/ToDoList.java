package todo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "todolist")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_id", nullable = false)
    private Long listId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false)
    private User user;

    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "completion_status", nullable = false)
    private boolean completionStatus;

    @Column(nullable = false)
    private String priority;

    @Column(name = "repeat_interval")
    private String repeatInterval;

    @OneToMany(mappedBy = "toDoList", cascade = CascadeType.ALL
            , orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setToDoList(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setToDoList(null);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
