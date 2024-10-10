package com.example.socialnetworkmap.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Tuple<Long, Long>> {
    LocalDateTime date;

    public Friendship(Long id1, Long id2, LocalDateTime date) {
        this.setId(new Tuple<>(id1, id2));
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }


    public boolean containsUser(Long userId) {
        Tuple<Long, Long> id = getId();

        return id.getLeft().equals(userId) || id.getRight().equals(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship that)) return false;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate());
    }
}
