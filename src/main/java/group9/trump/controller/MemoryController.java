package group9.trump.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import group9.trump.model.ChamberMapper;
import group9.trump.model.Chamber;
import group9.trump.model.TrumpMapper;
import group9.trump.model.Trump;
import group9.trump.model.DeckMapper;
//import group9.trump.model.Deck;

@Controller
public class MemoryController {

  @Autowired
  ChamberMapper CMapper;

  @Autowired
  TrumpMapper TMapper;

  @Autowired
  DeckMapper DMapper;

  @GetMapping("/memory")
  public String memory(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    Chamber chamber = CMapper.selectByName(loginUser);
    model.addAttribute("chamber", chamber);
    return "memory.html";
  }

  @GetMapping("/memoryMatch")
  public String memoryMatch(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);
    return "memoryMatch.html";
  }

  @GetMapping("/memoryFight")
  public String memoryFight(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    ArrayList<Trump> trump = TMapper.selectAll();
    Collections.shuffle(trump);
    model.addAttribute("trump", trump);
    model.addAttribute("user", loginUser);
    for (int j = 0; j < trump.size(); j++) {
      DMapper.insertDeck(trump.get(j).getNumber(), trump.get(j).getMark());
    }
    return "memoryFight.html";
  }
}
