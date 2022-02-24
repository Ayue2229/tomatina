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
        System.out.println(arenaUpdate);
        arenaUpdate.arena.state.entrySet().forEach(state -> {
            String user = state.getKey();
            PlayerState playerState = state.getValue();
            System.out.println(user + " : " + playerState);
        });
        String[] commands = new String[]{"F", "R", "L", "T"};
        int i = new Random().nextInt(4);
        return commands[i];
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
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }


}
