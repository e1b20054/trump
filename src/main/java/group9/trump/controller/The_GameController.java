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
import org.springframework.web.bind.annotation.RequestMapping;

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
import group9.trump.model.TurnMapper;
import group9.trump.model.Turn;
import group9.trump.service.AsyncThe_Game;
import group9.trump.service.AsyncTurn;

@Controller
@RequestMapping("/trump")
public class The_GameController {

  boolean play = false;
  boolean saisyo = true;
  boolean first = true;
  int a = 6;

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
  TurnMapper TurnMapper;

  @Autowired
  AsyncThe_Game asyncThe_Game;

  @Autowired
  AsyncTurn asyncTurn;

  @GetMapping("/the_game")
  public String trump(Principal prin, ModelMap model) {
    if (play == true) {
      return "memorySkip.html";
    }
    String loginUser = prin.getName();
    Chamber chamber = CMapper.selectByName(loginUser);
    model.addAttribute("chamber", chamber);
    return "the_game.html";
  }

  @GetMapping("/the_gameMatch")
  public String Deck_in(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);
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
    while (x2 > 1) {
      TGFMapper.deleteField(x2);
      x2 = TGFMapper.selectNumber(2);
    }
    while (x3 < 100) {
      TGFMapper.deleteField(x3);
      x3 = TGFMapper.selectNumber(3);
    }
    while (x4 < 100) {
      TGFMapper.deleteField(x4);
      x4 = TGFMapper.selectNumber(4);
    }
    ArrayList<Integer> the_game = TGMapper.selectAll();
    Collections.shuffle(the_game);
    for (int j = 0; j < the_game.size(); j++) {
      TGDMapper.insertDeck(the_game.get(j));
    }
    // TurnMapper.insert(loginUser, TGTMapper.selectCount(loginUser));
    this.asyncTurn.syncturnUser(loginUser);
    return "the_gameMatch.html";
  }

  @GetMapping("/the_gameMatch/di")
  public String di(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);
    // ArrayList<String> turn = TurnMapper.selectAllUser();
    final ArrayList<String> turn = this.asyncTurn.syncUser();
    model.addAttribute("turn", turn);
    return "the_gameMatch.html";
  }

  // 追加--------------------------------------
  @GetMapping("/the_gameFight/step3")
  public SseEmitter step3() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncTurn.asyncShowUser(sseEmitter);
    return sseEmitter;
  }

  // ---追加
  @GetMapping("/the_gameFight")
  public String tehuda(Principal prin, ModelMap model) {
    this.play = true;
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
  public String shouhi(Principal prin, ModelMap model, @RequestParam(defaultValue = "0") Integer id,
      @RequestParam(defaultValue = "0") Integer te) {
    String error;
    String loginUser = prin.getName();
    if (saisyo == true) {
      this.saisyo = false;
      TurnMapper.update1(loginUser, TGTMapper.selectCount(loginUser));
      ArrayList<String> others = TurnMapper.selectOther(loginUser);
      for (int i = 0; i < TurnMapper.selectCountOther(loginUser); i++) {
        TurnMapper.update0(others.get(i), TGTMapper.selectCount(others.get(i)));
      }
      this.asyncTurn.syncTurn();
    }
    String tasi = TurnMapper.selectUser();
    if (tasi == null) {
      tasi = "";
    }
    if (tasi.equals(loginUser)) {
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
        error = "間違え";
        model.addAttribute("error", error);
      }
      TurnMapper.update1(loginUser, TGTMapper.selectCount(loginUser));
    } else {
      error = "あなたのターンではないです";
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
      this.a = TGTMapper.selectCount(loginUser) + 1;
    }
    if (TGTMapper.selectCount(loginUser) < this.a) {
      String end = "end";
      model.addAttribute("end", end);
    }
    return "the_gameFight.html";
  }

  @GetMapping("/the_gameFight/step1")
  public SseEmitter step1() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncThe_Game.asyncShowThe_Game(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("/the_gameFight/deck")
  public String kakunin(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    ArrayList<Integer> tehuda = TGTMapper.selectAll(loginUser);
    int deck = TGDMapper.selectCount();
    final ArrayList<The_Game_Field> the_game_field1 = this.asyncThe_Game.syncShowThe_Game(1);
    final ArrayList<The_Game_Field> the_game_field2 = this.asyncThe_Game.syncShowThe_Game(2);
    final ArrayList<The_Game_Field> the_game_field3 = this.asyncThe_Game.syncShowThe_Game(3);
    final ArrayList<The_Game_Field> the_game_field4 = this.asyncThe_Game.syncShowThe_Game(4);
    model.addAttribute("the_game_field1", the_game_field1);
    model.addAttribute("the_game_field2", the_game_field2);
    model.addAttribute("the_game_field3", the_game_field3);
    model.addAttribute("the_game_field4", the_game_field4);
    model.addAttribute("deck", deck);
    model.addAttribute("tehuda", tehuda);
    return "the_gameFight.html";
  }

  @PostMapping("/the_gameFight/end")
  public String end(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    this.first = true;
    this.a = 6;
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
    // -->ここasnyc TurnMapper.update0(loginUser, TGTMapper.selectCount(loginUser));
    this.asyncTurn.syncChange(loginUser);
    return "the_gameFight.html";
  }

  @PostMapping("/the_gameFight/win")
  public String admin_win(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    TGDMapper.delete();
    final ArrayList<The_Game_Field> the_game_field1 = this.asyncThe_Game.syncShowThe_Game(1);
    final ArrayList<The_Game_Field> the_game_field2 = this.asyncThe_Game.syncShowThe_Game(2);
    final ArrayList<The_Game_Field> the_game_field3 = this.asyncThe_Game.syncShowThe_Game(3);
    final ArrayList<The_Game_Field> the_game_field4 = this.asyncThe_Game.syncShowThe_Game(4);
    model.addAttribute("the_game_field1", the_game_field1);
    model.addAttribute("the_game_field2", the_game_field2);
    model.addAttribute("the_game_field3", the_game_field3);
    model.addAttribute("the_game_field4", the_game_field4);
    ArrayList<Integer> tehuda = TGTMapper.selectAll(loginUser);
    model.addAttribute("tehuda", tehuda);
    return "the_gameFight.html";
  }

  @GetMapping("/the_gameFight/step2")
  public SseEmitter step2() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncTurn.asyncShowTurn(sseEmitter);
    return sseEmitter;
  }

  @PostMapping("/the_gameFight/result")
  public String result(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    this.play = false;
    this.saisyo = true;
    int d = TGDMapper.selectCount();
    int t = TGTMapper.selectCountAll();
    model.addAttribute("d", d);
    model.addAttribute("t", t);
    String win = "win";
    String lose = "lose";
    Chamber chamber = CMapper.selectByName(loginUser);
    asyncTurn.syncEnd();
    if (d + t < 10) {
      if (!(loginUser.equals("admin"))) {
        CMapper.updateWin(chamber.getWin() + 1, loginUser);
        chamber = CMapper.selectByName(loginUser);
      }
      model.addAttribute("chamber", chamber);
      model.addAttribute("win", win);
    } else {
      model.addAttribute("lose", lose);
      model.addAttribute("chamber", chamber);
    }
    TurnMapper.delete();
    return "the_gameResult.html";
  }

}
