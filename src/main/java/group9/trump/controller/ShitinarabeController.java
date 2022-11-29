package group9.trump.controller;

import java.util.ArrayList;
import java.util.Collections;
//import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

import group9.trump.model.TrumpMapper;
import group9.trump.model.Trump;
import group9.trump.model.DeckMapper;
import group9.trump.model.Deck;
import group9.trump.model.TehudaMapper;
import group9.trump.model.Shitinarabe;
import group9.trump.model.ShitinarabeMapper;
import group9.trump.model.Smatch;
//import group9.trump.model.SmatchMapper;
//import group9.trump.model.Tehuda;

@Controller
public class ShitinarabeController {
  @Autowired
  TrumpMapper TMapper;

  @Autowired
  DeckMapper DMapper;

  @Autowired
  ShitinarabeMapper SMapper;

  // @Autowired
  // SmatchMapper SMMapper;

  @GetMapping("/daihugo")
  public String trump() {
    // ArrayList<Trump> tehuda = new ArrayList<>();
    ArrayList<Trump> trumps = TMapper.selectAll();
    Collections.shuffle(trumps);
    Shitinarabe tmp = new Shitinarabe();
    Trump card = new Trump();
    for (int i = 1; i < 5; i++) {
      for (int j = 0; j < 13; j++) {
        card = trumps.get((i - 1) * 12 + j);
        tmp.setNumber(card.getNumber());
        tmp.setMark(card.getMark());
        tmp.setName(i);
        tmp.setState("tehuda");
        SMapper.insertShitinarabe(tmp.getNumber(), tmp.getMark(), tmp.getName(), tmp.getState());
      }
    }
    /*
     * model.addAttribute("tehuda", tehuda);
     * for (j = i; j < trumps.size(); j++) {
     * DMapper.insertDeck(trumps.get(j).getNumber(), trumps.get(j).getMark());
     * }
     * for (int k = 0; k < tehuda.size(); k++) {
     * TTMapper.insertTehuda(tehuda.get(k).getNumber(), trumps.get(k).getMark());
     * }
     * ArrayList<Deck> trump = DMapper.selectAll();
     * model.addAttribute("trump", trump);
     */
    return "test.html";
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
