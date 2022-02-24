package com.wy.tomatina;

import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@RestController
public class TomatinaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TomatinaApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "Let the battle begin!";
    }

    @PostMapping("/**")
    public String index(@RequestBody ArenaUpdate arenaUpdate) {
        String href = arenaUpdate._links.self.href;
        PlayerState myState = new PlayerState();
        PlayerState minState = new PlayerState();
        Map<String, PlayerState> states = arenaUpdate.arena.state;
        int myX = states.get(href).x;
        int myY = states.get(href).y;
        String myD = states.get(href).direction;

        String[] commands = new String[]{"F", "R", "L", "T"};
        int i = new Random().nextInt(4);
        String action =  commands[i];

        double minDistance = 10000;
        String minUser = "";
        for (Map.Entry<String, PlayerState> playerState : states.entrySet()) {
            if (playerState.getKey().equals(href)) {
                myState = playerState.getValue();
                if (myState.wasHit) {
                    return "F";
                }
                continue;
            }
            //System.out.println(arenaUpdate.arena.dims + "==>" + playerState.getKey() + " : " + playerState);
            int x = playerState.getValue().x;
            int y = playerState.getValue().y;

            double distance = Math.pow(Math.abs(myX - x), 2) + Math.pow(Math.abs(myY - y), 2);
            if (distance < minDistance) {
                minDistance = distance;
                minUser = playerState.getKey();
                minState = playerState.getValue();
            }
        }
        System.out.println("self======= " + href + " : " + myState);
        System.out.println("minPlayer== " + minUser + " : " + minState + " distance=" + minDistance);
        PlayerState minPlayerState = states.get(minUser);
        int x = minPlayerState.x;
        int y = minPlayerState.y;

        if ((myX - x == -1 && myY == y && "E".equals(myD))
                || (myX - x == 1 && myY == y && "W".equals(myD))
                || (myY - y == -1 && myX == x && "N".equals(myD))
                || (myY - y == 1 && myX == x && "S".equals(myD))
        ) {
            return "T";
        }

        if ((myX - x < -1 && "E".equals(myD))
                || (myX - x > 1 && "W".equals(myD))
                || (myY - y < -1 && "N".equals(myD))
                || (myY - y > 1 && "S".equals(myD))
        ) {
            return "F";
        }

        if (myX - x < -1) {
            action = "R";
        } else if (myX - x > 1) {
            action = "L";
        } else if (myY - y < -1) {
            action = "F";
        }

        return action;
    }

    @Data
    static class ArenaUpdate {
        public Links _links;
        public Arena arena;
    }

    @Data
    static class Links {
        public Self self;
    }

    @Data
    static class Self {
        public String href;
    }

    @Data
    static class Arena {
        public List<Integer> dims;
        public Map<String, PlayerState> state;
    }

    @Data
    static class PlayerState {
        public Integer x;
        public Integer y;
        public String direction;
        public Boolean wasHit;
        public Integer score;

        @Override
        public String toString() {
            return String.format("{ x: %s, y: %s, direction: %s, wasHist: %s, score: %s }", x, y, direction, wasHit, score);
        }
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }


}
