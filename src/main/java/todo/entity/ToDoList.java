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

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "completion_status", nullable = false)
    private boolean completionStatus;

    @Column(nullable = false)
    private String priority;

    @Column(name = "repeat_end_date")
    private LocalDateTime repeatEndDate;

    @OneToMany(mappedBy = "toDoList", cascade = CascadeType.ALL
            , orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public ToDoList(User user, String title, String content, LocalDateTime startDate, String priority, LocalDateTime repeatEndDate) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.creationDate = LocalDateTime.now();
        this.completionStatus = false;
        this.priority = priority;
        this.repeatEndDate = repeatEndDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setRepeatEndDate(LocalDateTime repeatEndDate) {
        this.repeatEndDate = repeatEndDate;
    }

    public void setCompletionStatus(boolean completionStatus) {
        this.completionStatus = completionStatus;
    }

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
