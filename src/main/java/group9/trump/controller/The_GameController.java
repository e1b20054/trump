package group9.trump.controller;

import java.util.ArrayList;
import java.util.Collections;
//import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

import group9.trump.model.The_GameMapper;
import group9.trump.model.The_Game;
import group9.trump.model.The_Game_DeckMapper;
import group9.trump.model.The_Game_Deck;
import group9.trump.model.The_Game_TehudaMapper;
import group9.trump.model.The_Game_Tehuda;

@Controller
public class The_GameController {
  @Autowired
  The_GameMapper TGMapper;

  @Autowired
  The_Game_DeckMapper TGDMapper;

  @Autowired
  The_Game_TehudaMapper TGTMapper;

  @GetMapping("/the_game")
  public String trump(ModelMap model) {
    int i = 0;
    ArrayList<The_Game> tehuda = new ArrayList<>();
    ArrayList<The_Game> the_game = TGMapper.selectAll();
    Collections.shuffle(the_game);
    model.addAttribute("the_game", the_game);
    while (8 > tehuda.size()) {
      tehuda.add(the_game.get(i));
      i++;
    }
    model.addAttribute("tehuda", tehuda);
    for (int j = i; j < the_game.size(); j++) {
      TGDMapper.insertDeck(the_game.get(j).getNumber());
    }
    for (int k = 0; k < tehuda.size(); k++) {
      TGTMapper.insertTehuda(tehuda.get(k).getNumber());
    }
    return "the_game.html";
  }

  @PostMapping("/the_game/shouhi")
  public String trump2(ModelMap model, @RequestParam Integer id, @RequestParam Integer te) {
    TGTMapper.deleteTehuda(te);
    ArrayList<The_Game_Deck> the_game = TGDMapper.selectAll();
    TGTMapper.insertTehuda(the_game.get(0).getNumber());
    TGDMapper.deleteDeck(the_game.get(0).getNumber());
    the_game.remove(0);
    ArrayList<The_Game_Tehuda> tehuda = TGTMapper.selectAll();
    model.addAttribute("the_game", the_game);
    model.addAttribute("tehuda", tehuda);
    return "the_game.html";
  }

}
