package group9.trump.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import group9.trump.model.The_GameMapper;
import group9.trump.model.The_Game;
import group9.trump.model.TrumpMapper;
import group9.trump.model.Trump;
import group9.trump.model.ChamberMapper;
import group9.trump.model.Chamber;


@Controller
public class TrumpController {

  @Autowired
  ChamberMapper CMapper;

  @Autowired
  TrumpMapper TMapper;

  @Autowired
  The_GameMapper TGMapper;

  @GetMapping("/trump")
  public String trump(ModelMap model) {
    ArrayList<The_Game> the_game = TGMapper.selectAll();
    model.addAttribute("the_game", the_game);
    ArrayList<Trump> trump = TMapper.selectAll();
    model.addAttribute("trump", trump);
    ArrayList<Chamber> chamber = CMapper.selectAll();
    model.addAttribute("chamber", chamber);
    return "trump.html";
  }

}
