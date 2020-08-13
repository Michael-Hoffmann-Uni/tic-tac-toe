package jpp.games.networking.server.database;

import jpp.games.networking.server.database.types.User;

import java.util.*;

public class DatabaseAccess_Impl implements DatabaseAccess {

    Map<String, User> userMap;

    public DatabaseAccess_Impl() {
        userMap = new HashMap<String, User>();
    }

    @Override
    public Optional<String> createUser(String name) {
        String key = name + Objects.hash(name);
        if (userMap.containsKey(key)) {
            return Optional.empty();
        } else {
            userMap.put(key, new User(name));
            return Optional.of(key);
        }
    }

    @Override
    public Optional<String> getName(String key) {
        if (userMap.containsKey(key)) {
            return Optional.of(userMap.get(key).getName());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> increaseScore(String name) {
        //recreate hashkey
        String key = name + Objects.hash(name);
        if (userMap.containsKey(key)) {
            User user = userMap.get(key);
            return Optional.of(user.increaseScore());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getScore(String name) {
        //recreate hashkey
        String key = name + Objects.hash(name);
        if (userMap.containsKey(key)) {
            User user = userMap.get(key);
            return Optional.of(user.getScore());
        }
        return Optional.empty();
    }

    @Override
    public String getHighscoreList() {
        String outputString = "";
        int counter = 0;
        HashMap<String, Integer> highscoreMap = new HashMap<>();
        //create highscore map with name and score
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            highscoreMap.put(entry.getValue().getName(), entry.getValue().getScore());
        }
        //transform highscoreMap to highscoreList
        List<Map.Entry<String, Integer>> highscoreList = new LinkedList<Map.Entry<String, Integer>>(highscoreMap.entrySet());
        //sort the highscoreList with custom comparator
        Collections.sort(highscoreList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        //iterate highscoreList (which has Map.Entry<String, Integer> elements)
        for (Map.Entry<String, Integer> entry : highscoreList) {
            if (counter >= 5) {
                break;
            } else {
                if (counter >= 1)
                    outputString = outputString + "\n";
                outputString = outputString + entry.getValue() + "\t" + entry.getKey();
                counter++;
            }
        }
        return outputString;
    }
}
