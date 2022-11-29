package group9.trump.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

import group9.trump.model.ChamberMapper;
import group9.trump.model.Chamber;
import group9.trump.model.TrumpMapper;
import group9.trump.model.Trump;
import group9.trump.model.DeckMapper;
import group9.trump.model.Tehuda;
import group9.trump.model.Deck;
import group9.trump.model.TehudaMapper;
import group9.trump.model.Tehuda;
import group9.trump.model.FieldMapper;
import group9.trump.model.Field;
import group9.trump.model.TurnMapper;
import group9.trump.model.Turn;

@Controller
public class DoubtController {

  @Autowired
  ChamberMapper CMapper;

  @Autowired
  TrumpMapper TMapper;

  @Autowired
  DeckMapper DMapper;

  @Autowired
  TehudaMapper TTMapper;

  @Autowired
  FieldMapper FMapper;

  @Autowired
  TurnMapper TUMapper;

  @GetMapping("/doubt")
  public String doubt(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    Chamber chamber = CMapper.selectByName(loginUser);
    model.addAttribute("chamber", chamber);
    return "doubt.html";
  }

  @GetMapping("/doubtMatch")
  public String doubtMatch(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);
    return "doubtMatch.html";
  }

  @GetMapping("/doubtFightStart")
  public String doubtFightStart(Principal prin, ModelMap model) {
    FMapper.deleteField();
    DMapper.deleteDeck();
    TTMapper.deleteTehuda();
    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);
    int i = 0;
    ArrayList<Trump> tehuda = new ArrayList<>();
    ArrayList<Trump> trumps = TMapper.selectAll();
    Collections.shuffle(trumps);
    while (13 > tehuda.size()) {
      tehuda.add(trumps.get(i));
      i++;
    }
    for (int j = i; j < trumps.size(); j++) {
      DMapper.insertDeck(trumps.get(j).getNumber(), trumps.get(j).getMark());
    }
    for (int k = 0; k < tehuda.size(); k++) {
      TTMapper.insertTehuda(tehuda.get(k).getNumber(), tehuda.get(k).getMark());
    }
    tehuda = TTMapper.selectAllOrder();
    model.addAttribute("tehuda", tehuda);
    ArrayList<Deck> trump = DMapper.selectAll();
    model.addAttribute("trump", trump);
    return "doubtFight.html";
  }

  @PostMapping("/doubtFight")
  public String cardSubmit(Principal prin, ModelMap model, @RequestParam int selectedcard) {
    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);
    Tehuda putcard = TTMapper.selectById(selectedcard);
    TTMapper.deleteTehudaById(selectedcard);

    FMapper.insertField(putcard.getNumber(), putcard.getMark());
    Field field = FMapper.selectFieldOne();
    model.addAttribute("field", field);
    ArrayList<Trump> tehuda = TTMapper.selectAllOrder();
    model.addAttribute("tehuda", tehuda);
    return "doubtFight.html";
  }
}
