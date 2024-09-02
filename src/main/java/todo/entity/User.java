package todo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "signup_date", nullable = false)
    private LocalDateTime signupDate;

    @Column(nullable = false)
    private String role;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "deactivation_date")
    private LocalDateTime deactivationDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToDoList> toDoLists = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public User(String email, String password, String profileImg, String phoneNumber, LocalDateTime signupDate, String role, boolean isActive, LocalDateTime deactivationDate) {
        this.email = email;
        this.password = password;
        this.profileImg = profileImg;
        this.phoneNumber = phoneNumber;
        this.signupDate = signupDate;
        this.role = role;
        this.isActive = isActive;
        this.deactivationDate = deactivationDate;
    }

    public void addToDoList(ToDoList toDoList) {
        toDoLists.add(toDoList);
        toDoList.setUser(this);
    }

    public void removeToDoList(ToDoList toDoList) {
        toDoLists.remove(toDoList);
        toDoList.setUser(null);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setUser(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setUser(null);
    }

}

