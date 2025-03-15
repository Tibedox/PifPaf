package ru.pifpaf;

public class Player {
    public String name = "Noname";
    public int score;
    public int kills;

    public void clone(Player player){
        name = player.name;
        score = player.score;
        kills = player.kills;
    }

    public void clear(){
        score = 0;
        kills = 0;
    }
}
