package group9.trump.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.List;

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
import group9.trump.model.The_GameMapper;
import group9.trump.model.The_Game;
import group9.trump.model.The_Game_DeckMapper;
import group9.trump.model.The_Game_Deck;
import group9.trump.model.The_Game_TehudaMapper;
import group9.trump.model.The_Game_Tehuda;
import group9.trump.model.The_Game_FieldMapper;
import group9.trump.model.The_Game_Field;
import group9.trump.service.AsyncThe_Game;

@Controller
public class The_GameController {
  @Autowired
  The_GameMapper TGMapper;

  @Autowired
  The_Game_DeckMapper TGDMapper;

  @Autowired
  The_Game_TehudaMapper TGTMapper;

  @Autowired
  The_Game_FieldMapper TGFMapper;

  @Autowired
  ChamberMapper CMapper;

  @Autowired
  AsyncThe_Game asyncThe_Game;

  // ---追加
  @GetMapping("/the_game")
  public String trump(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    Chamber chamber = CMapper.selectByName(loginUser);
    model.addAttribute("chamber", chamber);
    return "the_game.html";
  }

  @GetMapping("/the_gameMatch")
  public String deck(Principal prin, ModelMap model) {
    // int i = 0;
    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);
    int x1 = TGFMapper.selectNumber(1);
    int x2 = TGFMapper.selectNumber(2);
    int x3 = TGFMapper.selectNumber(3);
    int x4 = TGFMapper.selectNumber(4);
    TGDMapper.delete();
    TGTMapper.delete();
    while (x1 > 1) {
      TGFMapper.deleteField(x1);
      x1 = TGFMapper.selectNumber(1);
    }
    ArrayList<The_Game_Field> the_game_field1 = TGFMapper.selectAll(1);
    // model.addAttribute("the_game_field1", the_game_field1);
    while (x2 > 1) {
      TGFMapper.deleteField(x2);
      x2 = TGFMapper.selectNumber(2);
    }
    ArrayList<The_Game_Field> the_game_field2 = TGFMapper.selectAll(2);
    // model.addAttribute("the_game_field2", the_game_field2);
    while (x3 < 100) {
      TGFMapper.deleteField(x3);
      x3 = TGFMapper.selectNumber(3);
    }
    ArrayList<The_Game_Field> the_game_field3 = TGFMapper.selectAll(3);
    // model.addAttribute("the_game_field3", the_game_field3);
    while (x4 < 100) {
      TGFMapper.deleteField(x4);
      x4 = TGFMapper.selectNumber(4);
    }
    ArrayList<The_Game_Field> the_game_field4 = TGFMapper.selectAll(4);
    // model.addAttribute("the_game_field4", the_game_field4);
    // ArrayList<The_Game> tehuda = new ArrayList<>();
    ArrayList<Integer> the_game = TGMapper.selectAll();
    Collections.shuffle(the_game);
    // model.addAttribute("the_game", the_game);
    // while (8 > tehuda.size()) {
    // tehuda.add(the_game.get(i));
    // i++;
    // }
    // model.addAttribute("tehuda", tehuda);
    for (int j = 0; j < the_game.size(); j++) {
      TGDMapper.insertDeck(the_game.get(j));
    }
    // for (int k = 0; k < tehuda.size(); k++) {
    // TGTMapper.insertTehuda(tehuda.get(k).getNumber());
    // }
    return "the_gameMatch.html";
  }

  // ---追加
  @GetMapping("/the_gameFight")
  public String tehuda(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    ArrayList<Integer> the_game = TGDMapper.selectAll();
    while (7 > TGTMapper.selectCount(loginUser)) {
      TGTMapper.insertTehuda(loginUser, the_game.get(0));
      TGDMapper.deleteDeck(the_game.get(0));
      the_game.remove(0);
    }
    ArrayList<Integer> tehuda = TGTMapper.selectAll(loginUser);
    final ArrayList<The_Game_Field> the_game_field1 = this.asyncThe_Game.syncShowThe_Game(1);
    final ArrayList<The_Game_Field> the_game_field2 = this.asyncThe_Game.syncShowThe_Game(2);
    final ArrayList<The_Game_Field> the_game_field3 = this.asyncThe_Game.syncShowThe_Game(3);
    final ArrayList<The_Game_Field> the_game_field4 = this.asyncThe_Game.syncShowThe_Game(4);
    model.addAttribute("the_game_field1", the_game_field1);
    model.addAttribute("the_game_field2", the_game_field2);
    model.addAttribute("the_game_field3", the_game_field3);
    model.addAttribute("the_game_field4", the_game_field4);
    // model.addAttribute("the_game", the_game);
    model.addAttribute("tehuda", tehuda);
    return "the_gameFight.html";
  }

  @PostMapping("/the_gameFight/shouhi")
  @Transactional
  public String trump2(Principal prin, ModelMap model, @RequestParam(defaultValue = "0") Integer id,
      @RequestParam(defaultValue = "0") Integer te) {
    String loginUser = prin.getName();
    int a = 5;
    int n1 = TGFMapper.selectNumber(1);
    int n2 = TGFMapper.selectNumber(2);
    int n3 = TGFMapper.selectNumber(3);
    int n4 = TGFMapper.selectNumber(4);
    int p10 = te + 10;
    int m10 = te - 10;
    if (id == 1 && (te > n1 || p10 == n1)) {
      TGTMapper.deleteTehuda(te);
      this.asyncThe_Game.syncThe_Game(1, te);
    } else if (id == 2 && (te > n2 || p10 == n2)) {
      TGTMapper.deleteTehuda(te);
      this.asyncThe_Game.syncThe_Game(2, te);
    } else if (id == 3 && (te < n3 || m10 == n3)) {
      TGTMapper.deleteTehuda(te);
      this.asyncThe_Game.syncThe_Game(3, te);
    } else if (id == 4 && (te < n4 || m10 == n4)) {
      TGTMapper.deleteTehuda(te);
      this.asyncThe_Game.syncThe_Game(4, te);
    } else {
      String error = "間違え";
      model.addAttribute("error", error);
    }
    ArrayList<Integer> tehuda = TGTMapper.selectAll(loginUser);
    final ArrayList<The_Game_Field> the_game_field1 = this.asyncThe_Game.syncShowThe_Game(1);
    final ArrayList<The_Game_Field> the_game_field2 = this.asyncThe_Game.syncShowThe_Game(2);
    final ArrayList<The_Game_Field> the_game_field3 = this.asyncThe_Game.syncShowThe_Game(3);
    final ArrayList<The_Game_Field> the_game_field4 = this.asyncThe_Game.syncShowThe_Game(4);
    model.addAttribute("the_game_field1", the_game_field1);
    model.addAttribute("the_game_field2", the_game_field2);
    model.addAttribute("the_game_field3", the_game_field3);
    model.addAttribute("the_game_field4", the_game_field4);
    // model.addAttribute("the_game", the_game);
    model.addAttribute("tehuda", tehuda);
    int d = TGDMapper.selectCount();
    if (d == 0) {
      a = 6;
    }
    if (TGTMapper.selectCount(loginUser) <= a) {
      String end = "end";
      model.addAttribute("end", end);
    }
    return "the_gameFight.html";
  }

  // 追加--------------------------------------
  @GetMapping("/the_gameFight/step1")
  public SseEmitter step1() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncThe_Game.asyncShowThe_Game(sseEmitter);
    return sseEmitter;
  }

  @PostMapping("/the_gameFight/end")
  public String end(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    final ArrayList<The_Game_Field> the_game_field1 = this.asyncThe_Game.syncShowThe_Game(1);
    final ArrayList<The_Game_Field> the_game_field2 = this.asyncThe_Game.syncShowThe_Game(2);
    final ArrayList<The_Game_Field> the_game_field3 = this.asyncThe_Game.syncShowThe_Game(3);
    final ArrayList<The_Game_Field> the_game_field4 = this.asyncThe_Game.syncShowThe_Game(4);
    model.addAttribute("the_game_field1", the_game_field1);
    model.addAttribute("the_game_field2", the_game_field2);
    model.addAttribute("the_game_field3", the_game_field3);
    model.addAttribute("the_game_field4", the_game_field4);
    ArrayList<Integer> the_game = TGDMapper.selectAll();
    for (int i = TGTMapper.selectCount(loginUser); i < 7; i++) {
      if (TGDMapper.selectCount() == 0) {
        i = 100;
      } else {
        TGTMapper.insertTehuda(loginUser, the_game.get(0));
        TGDMapper.deleteDeck(the_game.get(0));
        the_game.remove(0);
      }
    }
    ArrayList<Integer> tehuda = TGTMapper.selectAll(loginUser);
    model.addAttribute("tehuda", tehuda);
    return "the_gameFight.html";
  }

  // 追加--------------------------------------
  @PostMapping("/the_gameFight/result")
  public String result(Principal prin, ModelMap model) {
    int d = TGDMapper.selectCount();
    int t = TGTMapper.selectCountAll();
    String win = "win";
    String lose = "lose";
    if (d + t < 10) {
      model.addAttribute("win", win);
    } else {
      model.addAttribute("lose", lose);
    }
    return "the_gameResult.html";
  }

}
