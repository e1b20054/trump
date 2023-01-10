package group9.trump.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import group9.trump.model.Shitinarabe;
import group9.trump.model.ShitinarabeMapper;
import group9.trump.model.ShitinarabeField;
import group9.trump.model.ShitinarabeFieldMapper;
import group9.trump.model.ShitinarabeMatch;
import group9.trump.model.ShitinarabeMatchMapper;

import group9.trump.model.TrumpMapper;
import group9.trump.model.Trump;
import group9.trump.model.ChamberMapper;
//import group9.trump.model.Chamber;
import group9.trump.service.AsyncShitinarabe;

@Controller
public class ShitinarabeController {
  @Autowired
  ShitinarabeMapper SMapper;

  @Autowired
  ShitinarabeFieldMapper SFMapper;

  @Autowired
  ShitinarabeMatchMapper SMMapper;

  @Autowired
  TrumpMapper TMapper;

  @Autowired
  ChamberMapper CMapper;

  @Autowired
  AsyncShitinarabe asyncShitinarabe;

  @GetMapping("/shitinarabe")
  public String shitinarabe(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    ArrayList<ShitinarabeMatch> SChambers = SMMapper.selectAll();
    /*
     * if (MChambers.size() >= 4) {
     * return "memorySkip.html";
     * }
     */
    ShitinarabeMatch SChamber = SMMapper.selectByName(loginUser);
    if (SChamber == null) {
      asyncShitinarabe.syncInsertShitinarabeChamber(loginUser);
      SChamber = SMMapper.selectByName(loginUser);
    }
    SChambers = SMMapper.selectAll();
    model.addAttribute("user", loginUser);
    model.addAttribute("chamber", SChamber);
    model.addAttribute("SChambers", SChambers);
    return "shitinarabe.html";
  }

  @GetMapping("/shitinarabeFight")
  public String trump(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    ArrayList<ShitinarabeField> sfmapper = SFMapper.selectAll();
    Shitinarabe tmp = new Shitinarabe();
    if (sfmapper.size() == 0) {
      ArrayList<Trump> trumps = TMapper.selectAll();
      ShitinarabeField field = new ShitinarabeField();
      Trump card = new Trump();

      asyncShitinarabe.syncInsertStartflag();

      for (int i = 0; i < 52; i++) {
        card = trumps.get(i);
        field.setNumber(card.getNumber());
        field.setMark(card.getMark());
        SFMapper.insertShitinarabeField(field.getNumber(), field.getMark(), false);
      }

      Collections.shuffle(trumps); // トランプシャッフル
      int count = 0;
      int n = 0;

      while (count != 4) {
        if ((trumps.get(n)).getNumber() == 7) {
          trumps.remove(n);
          count++;
        } else {
          n++;
        }
      }

      for (int i = 1; i < 5; i++) {
        for (int j = 0; j < 12; j++) {
          card = trumps.get((i - 1) * 12 + j);
          tmp.setNumber(card.getNumber());
          tmp.setMark(card.getMark());
          if (i == 1) {
            tmp.setName("user1");
          }
          if (i == 2) {
            tmp.setName("user2");
          }
          if (i == 3) {
            tmp.setName("user3");
          }
          if (i == 4) {
            tmp.setName("user4");
          }

          tmp.setState("tehuda");
          SMapper.insertShitinarabe(tmp.getNumber(), tmp.getMark(), tmp.getName(), tmp.getState());
        }
      }
    } else {
      String count;
      count = SMMapper.selectFlag(-1);
      int c = Integer.valueOf(count) + 1;
      if (c == 2) {
        SFMapper.updateField("♦", 7);
        SFMapper.updateField("♠", 7);
        SFMapper.updateField("♣", 7);
        SFMapper.updateField("♥", 7);
      }
      asyncShitinarabe.syncUpdateStartflag(Integer.toString(c));
    }

    String str = "";
    model.addAttribute("message", str);
    ArrayList<Shitinarabe> tehuda = SMapper.selectTehuda(loginUser);
    model.addAttribute("tehuda", tehuda);
    model.addAttribute("user", loginUser);
    return "shitinarabeFight.html";
  }

  @GetMapping("/shitinarabeStart")
  public String shitinarabeStart(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    String nextName = SMMapper.selectNameById(1);
    String name;

    for (int i = 1; i < 5; i++) {
      name = SMMapper.selectNameById(i);
      if (i != 4) {
        SMMapper.updateSmatch(i + 1, name);
      } else {
        SMMapper.updateSmatch(1, name);
      }
    }
    asyncShitinarabe.syncInsertSMatch(7, "all", loginUser, nextName);

    String str = "";
    model.addAttribute("message", str);
    ArrayList<Shitinarabe> tehuda = SMapper.selectTehuda(loginUser);
    model.addAttribute("tehuda", tehuda);
    model.addAttribute("user", loginUser);
    return "shitinarabeFight.html";
  }

  @PostMapping("/shitinarabeFight")
  public String shitinarabeFight(Principal prin, ModelMap model, @RequestParam int id) {
    String mark = SMapper.selectMarkById(id);
    int number = SMapper.selectNumberById(id);
    String loginUser = prin.getName();
    ShitinarabeField right = new ShitinarabeField();
    ShitinarabeField left = new ShitinarabeField();

    if (SMMapper.selectId() != 3 && SMMapper.selectNextname().equals(loginUser)) {
      if (number == 13) {
        right = SFMapper.selectCard(mark, number - 12);
      } else {
        right = SFMapper.selectCard(mark, number + 1);
      }
      if (number == 1) {
        left = SFMapper.selectCard(mark, number + 12);
      } else {
        left = SFMapper.selectCard(mark, number - 1);
      }

      if (right.getField() == true || left.getField() == true) {
        SFMapper.updateField(mark, number);
        SMapper.updateTehuda(mark, number);
        String nextName = SMMapper.selectNextnameByName(loginUser);
        asyncShitinarabe.syncInsertSMatch(number, mark, loginUser, nextName);
      }
    }
    String str = "";
    model.addAttribute("message", str);
    ArrayList<Shitinarabe> tehuda = SMapper.selectTehuda(loginUser);
    if (tehuda.size() == 0) {
      SMMapper.insertSmatch(-1, null, loginUser, null);
    } else {
      model.addAttribute("tehuda", tehuda);
    }
    model.addAttribute("user", loginUser);
    return "shitinarabeFight.html";
  }

  @GetMapping("/skip")
  public String skip(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    if (SMMapper.selectId() != 3 && SMMapper.selectNextname().equals(loginUser)) {
      String nextName = SMMapper.selectNextnameByName(loginUser);
      asyncShitinarabe.syncInsertSMatch(0, "null", loginUser, nextName);
    }
    String str = "";
    model.addAttribute("message", str);
    ArrayList<Shitinarabe> tehuda = SMapper.selectTehuda(loginUser);
    model.addAttribute("tehuda", tehuda);
    model.addAttribute("user", loginUser);
    return "shitinarabeFight.html";
  }

  @GetMapping("/shitinarabeEnd")
  public String shitinarabeEnd(Principal prin, ModelMap model) {
    String winner = SMMapper.selectWinner();
    model.addAttribute("winner", winner);
    return "shitinarabeEnd.html";
  }

  @GetMapping("/shitinarabeSse")
  public SseEmitter shitinarabeSse() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncShitinarabe.asyncWait(sseEmitter);
    return sseEmitter;
  }

}
