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

import group9.trump.model.TrumpMapper;
import group9.trump.model.Trump;
import group9.trump.model.DeckMapper;
import group9.trump.model.Deck;
import group9.trump.model.TehudaMapper;
import group9.trump.model.Tehuda;

@Controller
public class DoubtController {
  @Autowired
  TrumpMapper TMapper;

  @Autowired
  DeckMapper DMapper;

  @Autowired
  TehudaMapper TTMapper;

  @GetMapping("/doubt")
  public String trump(ModelMap model) {
    int i = 0;
    ArrayList<Trump> tehuda = new ArrayList<>();
    ArrayList<Trump> trumps = TMapper.selectAll();
    Collections.shuffle(trumps);
    while (8 > tehuda.size()) {
      tehuda.add(trumps.get(i));
      i++;
    }
    model.addAttribute("tehuda", tehuda);
    for (int j = i; j < trumps.size(); j++) {
      DMapper.insertDeck(trumps.get(j).getNumber(), trumps.get(j).getMark());
    }
    for (int k = 0; k < tehuda.size(); k++) {
      TTMapper.insertTehuda(tehuda.get(k).getNumber(), trumps.get(k).getMark());
    }
    ArrayList<Deck> trump = DMapper.selectAll();
    model.addAttribute("trump", trump);
    return "doubt.html";
  }

  // @PostMapping("/ /shouhi")
  // public String trump2(ModelMap model, @RequestParam Integer id, @RequestParam
  // Integer te) {
  // TTMapper.deleteTehuda(te);
  // ArrayList< Deck> = TGDMapper.selectAll();
  // TGTMapper.insertTehuda( .get(0).getNumber());
  // TGDMapper.deleteDeck( .get(0).getNumber());
  // .remove(0);
  // ArrayList< Tehuda> tehuda = TGTMapper.selectAll();
  // model.addAttribute(" ", );
  // model.addAttribute("tehuda", tehuda);
  // return " .html";
  // }

}
