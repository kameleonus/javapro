package com.example.javaProjektKc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;


        @Column(nullable = false, unique = true)
        private String email;


        @Column(nullable = false)
        private String password;


        @Column(nullable = false)
        private String role;

        @Column(nullable = false)
        private int score;
        @Column(name = "finished_test")
        private boolean finishedTest;



    public User() {}

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.score-=0;
        this.finishedTest=false;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public boolean isFinishedTest() {
        return finishedTest;
    }

    public void setFinishedTest(boolean finishedTest) {
        this.finishedTest = finishedTest;
    }
}
