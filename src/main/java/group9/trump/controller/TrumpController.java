package group9.trump.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import group9.trump.model.The_GameMapper;
import group9.trump.model.The_Game;


@Controller
public class TrumpController {

  @Autowired
  The_GameMapper game;

  @GetMapping("/trump")
  public String trump(ModelMap model) {
    ArrayList<The_Game> the_game = game.selectAll();
    model.addAttribute("the_game", the_game);
    return "trump.html";
  }

}
