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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.transaction.annotation.Transactional;

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
import group9.trump.model.DoubtResultMapper;
import group9.trump.model.DoubtResult;
import group9.trump.service.AsyncDoubtField;
import group9.trump.service.AsyncDoubtResult;

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
  DoubtResultMapper DRMapper;

  @Autowired
  AsyncDoubtField asyncDoubtField;

  @Autowired
  AsyncDoubtResult asyncDoubtResult;

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
    DRMapper.deleteResult();
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

    int turn = 13;
    String nextname = "admin";
    asyncDoubtField.syncDoubtField(0, "♠", turn, loginUser, nextname);
    model.addAttribute("nextname", nextname);
    Tehuda = TTMapper.selectAllOrder();
    model.addAttribute("tehuda", Tehuda);
    ArrayList<Deck> trump = DMapper.selectAll();
    model.addAttribute("trump", trump);
    return "doubtFight.html";
  }

  @GetMapping("/doubtFight")
  public String doubtFight1(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);

    final Field field = this.asyncDoubtField.syncShowDoubt();
    model.addAttribute("field", field);

    final DoubtResult result = this.asyncDoubtResult.syncShowResult();
    model.addAttribute("result", result);

    ArrayList<Tehuda> Tehuda = TTMapper.selectAllOrder();
    model.addAttribute("tehuda", Tehuda);

    return "doubtFight.html";
  }

  @PostMapping("/doubtFight")
  public String doubtFight2(Principal prin, ModelMap model, @RequestParam int selectedcard) {
    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);
    String nextname = FMapper.selectNextOne();

    if (nextname.equals(loginUser)) {
      int turn = FMapper.selectTurnOne();
      turn++;
      if (turn == 14) {
        turn = 1;
      }
      Tehuda putcard = TTMapper.selectById(selectedcard);
      TTMapper.deleteTehudaById(selectedcard);

      if (nextname.equals("user3")) {
        nextname = "admin";
      } else if (nextname.equals("admin")) {
        nextname = "user1";
      } else if (nextname.equals("user1")) {
        nextname = "user2";
      } else if (nextname.equals("user2")) {
        nextname = "user3";
      }
      if (FMapper.selectNumberOne() == 0) {
        FMapper.deleteField();
      }
      model.addAttribute("nextname", nextname);
      asyncDoubtField.syncDoubtField(putcard.getNumber(), putcard.getMark(), turn, loginUser, nextname);
      final Field field = this.asyncDoubtField.syncShowDoubt();
      model.addAttribute("field", field);
      ArrayList<Tehuda> Tehuda = TTMapper.selectAllOrder();
      model.addAttribute("tehuda", Tehuda);
      if (TTMapper.selectTehudaCount(loginUser) == 0) {
        Chamber chamber = CMapper.selectByName(loginUser);
        CMapper.updateWin(chamber.getWin() + 1, loginUser);
        chamber = CMapper.selectByName(loginUser);
        model.addAttribute("chamber", chamber);
        return "doubtEnd.html";
      }
      return "doubtFight.html";
    }
    final DoubtResult result = this.asyncDoubtResult.syncShowResult();
    model.addAttribute("result", result);
    final Field field = this.asyncDoubtField.syncShowDoubt();
    model.addAttribute("field", field);
    ArrayList<Tehuda> Tehuda = TTMapper.selectAllOrder();
    model.addAttribute("tehuda", Tehuda);
    return "doubtFight.html";
  }

  @GetMapping("/doubtFight/step1")
  public SseEmitter step1() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncDoubtField.asyncShowDoubtField(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("/doubtFight/step2")
  public SseEmitter step2() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncDoubtResult.asyncShowDoubtResult(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("/doubtCall")
  public String doubtCall(Principal prin, ModelMap model) {
    String judge = "";
    String user = "";
    int i = 1;

    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);

    Field field = FMapper.selectFieldOne();
    Field first = FMapper.selectFieldFirst();
    Field insert;
    if (field.getTurn() == field.getNumber()) {
      judge = "成功";
      user = field.getUser();
      for (i = first.getId(); i <= field.getId(); i++) {
        insert = FMapper.selectById(i);
        if (insert.getNumber() != 0) {
          TTMapper.insertTehuda(insert.getNumber(), insert.getMark(), user);
        }
      }
    } else {
      judge = "失敗";
      user = loginUser;
      for (i = first.getId(); i <= field.getId(); i++) {
        insert = FMapper.selectById(i);
        if (insert.getNumber() != 0) {
          TTMapper.insertTehuda(insert.getNumber(), insert.getMark(), user);
        }
      }
    }

    int turn = FMapper.selectTurnOne();
    String nextname = FMapper.selectNextOne();
    FMapper.deleteField();
    asyncDoubtField.syncDoubtField(0, "♠", turn, loginUser, nextname);
    field = this.asyncDoubtField.syncShowDoubt();
    // DRMapper.insertResult(result,user);
    asyncDoubtResult.syncDoubtResult(judge, user);
    final DoubtResult result = this.asyncDoubtResult.syncShowResult();
    model.addAttribute("result", result);
    model.addAttribute("field", field);
    ArrayList<Tehuda> Tehuda = TTMapper.selectAllOrder();
    model.addAttribute("tehuda", Tehuda);
    return "doubtFight.html";
  }
}
