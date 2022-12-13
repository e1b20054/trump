package group9.trump.controller;

import java.util.ArrayList;
import java.util.Collections;
//import java.util.List;
import java.security.Principal;
import group9.trump.model.Sgame;

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
import group9.trump.model.ShitinarabeField;
import group9.trump.model.ShitinarabeFieldMapper;
import group9.trump.model.Smatch;
//import group9.trump.model.SmatchMapper;
//import group9.trump.model.Tehuda;
import group9.trump.model.ChamberMapper;
import group9.trump.model.Chamber;

@Controller
public class ShitinarabeController {
  @Autowired
  TrumpMapper TMapper;

  @Autowired
  DeckMapper DMapper;

  @Autowired
  ShitinarabeMapper SMapper;

  @Autowired
  ShitinarabeFieldMapper SFMapper;

  // @Autowired
  // SmatchMapper SMMapper;

  @Autowired
  ChamberMapper CMapper;

  @GetMapping("/shitinarabe")
  public String trump(Principal prin, ModelMap model) {
    ArrayList<Trump> trumps = TMapper.selectAll();
    Shitinarabe tmp = new Shitinarabe();
    ShitinarabeField Field = new ShitinarabeField();
    Trump card = new Trump();

    for (int i = 0; i < 52; i++) {
      card = trumps.get(i);
      Field.setNumber(card.getNumber());
      Field.setMark(card.getMark());
      SFMapper.insertShitinarabeField(Field.getNumber(), Field.getMark(), false);
    }

    Collections.shuffle(trumps); // トランプシャッフル
    for (int i = 1; i < 5; i++) {
      for (int j = 0; j < 13; j++) {
        card = trumps.get((i - 1) * 13 + j);
        tmp.setNumber(card.getNumber());
        tmp.setMark(card.getMark());
        tmp.setName(1);
        tmp.setState("tehuda");
        SMapper.insertShitinarabe(tmp.getNumber(), tmp.getMark(), tmp.getName(), tmp.getState());
      }
    }

    tmp = SMapper.selectCard("♦", 7);
    SFMapper.updateField("♦", 7);
    SMapper.updateTehuda(tmp.getId());
    tmp = SMapper.selectCard("♠", 7);
    SFMapper.updateField("♠", 7);
    SMapper.updateTehuda(tmp.getId());
    tmp = SMapper.selectCard("♣", 7);
    SFMapper.updateField("♣", 7);
    SMapper.updateTehuda(tmp.getId());
    tmp = SMapper.selectCard("♥", 7);
    SFMapper.updateField("♥", 7);
    SMapper.updateTehuda(tmp.getId());

    String loginUser = prin.getName();
    int loginId = CMapper.selectIdByName(loginUser);
    ArrayList<Shitinarabe> tehuda = SMapper.selectTehuda(loginId);
    ArrayList<ShitinarabeField> Fieldlist = SFMapper.selectAll();
    model.addAttribute("Field", Fieldlist);
    model.addAttribute("tehuda", tehuda);
    return "shitinarabe.html";
  }

  @GetMapping("/card")
  public String card(@RequestParam String mark, int number, Principal prin, ModelMap model) {
    Shitinarabe right = new Shitinarabe();
    Shitinarabe left = new Shitinarabe();
    Shitinarabe tmp = new Shitinarabe();

    if (number == 13) {
      right = SMapper.selectCard(mark, number - 12);
    } else {
      right = SMapper.selectCard(mark, number + 1);
    }
    if (number == 1) {
      left = SMapper.selectCard(mark, number + 12);
    } else {
      left = SMapper.selectCard(mark, number - 1);
    }

    if ("Field".equals(right.getState()) || "Field".equals(left.getState())) {
      tmp = SMapper.selectCard(mark, number);
      SFMapper.updateField(mark, number);
      SMapper.updateTehuda(tmp.getId());
      model.addAttribute("flag", "true");
    } else {
      model.addAttribute("flag", "false");
    }

    String loginUser = prin.getName();
    int loginId = CMapper.selectIdByName(loginUser);
    ArrayList<Shitinarabe> tehuda = SMapper.selectTehuda(loginId);
    ArrayList<ShitinarabeField> Fieldlist = SFMapper.selectAll();
    model.addAttribute("Field", Fieldlist);
    model.addAttribute("tehuda", tehuda);
    return "shitinarabe.html";
  }

}
