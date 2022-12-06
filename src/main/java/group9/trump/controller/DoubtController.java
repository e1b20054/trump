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

  int turn = 13;

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
    ArrayList<Tehuda> Tehuda = new ArrayList<>();
    ArrayList<Tehuda> trumps = TMapper.selectAllTehuda();
    Collections.shuffle(trumps);

    while (52 > Tehuda.size()) {
      Tehuda.add(trumps.get(i));
      i++;
    }

    for (int k = 0; k < Tehuda.size(); k = k + 4) {
      TTMapper.insertTehuda(Tehuda.get(k).getNumber(), Tehuda.get(k).getMark(), "user1");
      TTMapper.insertTehuda(Tehuda.get(k + 1).getNumber(), Tehuda.get(k + 1).getMark(), "user2");
      TTMapper.insertTehuda(Tehuda.get(k + 2).getNumber(), Tehuda.get(k + 2).getMark(), "user3");
      TTMapper.insertTehuda(Tehuda.get(k + 3).getNumber(), Tehuda.get(k + 3).getMark(), "admin");
    }

    Tehuda = TTMapper.selectAllOrder();
    model.addAttribute("tehuda", Tehuda);
    ArrayList<Deck> trump = DMapper.selectAll();
    model.addAttribute("trump", trump);
    return "doubtFight.html";
  }

  @PostMapping("/doubtFight")
  public String doubtFight(Principal prin, ModelMap model, @RequestParam int selectedcard) {
    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);
    turn++;
    if (turn == 14) {
      turn = 1;
    }
    Tehuda putcard = TTMapper.selectById(selectedcard);
    TTMapper.deleteTehudaById(selectedcard);

    FMapper.insertField(putcard.getNumber(), putcard.getMark(), turn, loginUser);
    Field field = FMapper.selectFieldOne();
    model.addAttribute("field", field);
    ArrayList<Tehuda> Tehuda = TTMapper.selectAllOrder();
    model.addAttribute("tehuda", Tehuda);
    if (Tehuda.size() == 0) {
      Chamber chamber = CMapper.selectByName(loginUser);
      CMapper.updateWin(chamber.getWin() + 1);
      chamber = CMapper.selectByName(loginUser);
      model.addAttribute("chamber", chamber);
      return "doubtEnd.html";
    }
    return "doubtFight.html";
  }

  @GetMapping("/doubtCall")
  public String doubtCall(Principal prin, ModelMap model) {
    String result = "";
    String user = "";
    int i = 1;

    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);

    Field field = FMapper.selectFieldOne();
    Field first = FMapper.selectFieldFirst();
    Field insert;
    if (field.getTurn() == field.getNumber()) {
      result = "成功";
      user = field.getUser();
      for (i = first.getId(); i <= field.getId(); i++) {
        insert = FMapper.selectById(i);
        TTMapper.insertTehuda(insert.getNumber(), insert.getMark(), user);
      }
    } else {
      result = "失敗";
      user = loginUser;
      for (i = first.getId(); i <= field.getId(); i++) {
        insert = FMapper.selectById(i);
        TTMapper.insertTehuda(insert.getNumber(), insert.getMark(), user);
      }
    }
    FMapper.deleteField();
    model.addAttribute("user", user);
    model.addAttribute("result", result);
    model.addAttribute("field", field);
    ArrayList<Tehuda> Tehuda = TTMapper.selectAllOrder();
    model.addAttribute("tehuda", Tehuda);
    return "doubtFight.html";
  }
}
