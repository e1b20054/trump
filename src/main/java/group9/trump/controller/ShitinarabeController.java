package group9.trump.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
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
import group9.trump.model.Chamber;
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
    if (sfmapper.size() == 0) {
      ArrayList<Trump> trumps = TMapper.selectAll();
      Shitinarabe tmp = new Shitinarabe();
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
      for (int i = 1; i < 5; i++) {
        for (int j = 0; j < 13; j++) {
          card = trumps.get((i - 1) * 13 + j);
          tmp.setNumber(card.getNumber());
          tmp.setMark(card.getMark());
          if (i == 1) {
            tmp.setName("user1");
          }
          if (i == 2) {
            tmp.setName("user2");
          }
          if (i == 3) {
            tmp.setName("user1");
          }
          if (i == 4) {
            tmp.setName("user2");
          }

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
    }

    ArrayList<Shitinarabe> tehuda = SMapper.selectTehuda(loginUser);
    ArrayList<ShitinarabeField> fieldlist = SFMapper.selectAll();
    model.addAttribute("field", fieldlist);
    model.addAttribute("tehuda", tehuda);
    return "shitinarabe.html";
  }

  @GetMapping("/card")
  public String card(@RequestParam String mark, int number, Principal prin, ModelMap model) {
    String loginUser = prin.getName();
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

    if ("field".equals(right.getState()) || "field".equals(left.getState())) {
      tmp = SMapper.selectCard(mark, number);
      SFMapper.updateField(mark, number);
      SMapper.updateTehuda(tmp.getId());
      asyncShitinarabe.syncInsertSMatch(number, mark, loginUser, "xxx");
      model.addAttribute("flag", "true");
    } else {
      model.addAttribute("flag", "false");
    }
    ArrayList<Shitinarabe> tehuda = SMapper.selectTehuda(loginUser);
    ArrayList<ShitinarabeField> fieldlist = SFMapper.selectAll();
    model.addAttribute("field", fieldlist);
    model.addAttribute("tehuda", tehuda);
    return "shitinarabeFight.html";
  }

  @GetMapping("/shitinarabeSse")
  public SseEmitter shitinarabeSse() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncShitinarabe.asyncWait(sseEmitter);
    return sseEmitter;
  }

}
