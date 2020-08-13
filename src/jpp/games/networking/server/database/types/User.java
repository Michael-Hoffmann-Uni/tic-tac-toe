package jpp.games.networking.server.database.types;

import java.util.Objects;

public class User {

    private String name;
    private int score;

    public User(String name){
        if(name == null){
            throw new NullPointerException("Argument name is null.");
        }
        this.name = name;
        this.score = 0;
    }

    public String getName(){
        return name;
    }

    public int getScore(){
        return score;
    }

    public int increaseScore(){
        this.score = score+1;
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
