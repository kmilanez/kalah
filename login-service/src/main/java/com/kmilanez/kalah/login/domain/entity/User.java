package com.kmilanez.kalah.login.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    @JsonIgnore
    private ObjectId id;
    private String username;
    private String password;

    public User(final String username) {
        this.username = username;
    }

    public User(final String username, final String password) {
        this.id = ObjectId.get();
        this.username = username;
        this.password = password;
    }
}
