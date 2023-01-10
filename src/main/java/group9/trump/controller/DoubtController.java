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
import group9.trump.model.Deck;
import group9.trump.model.TehudaMapper;
import group9.trump.model.Tehuda;
import group9.trump.model.FieldMapper;
import group9.trump.model.Field;
import group9.trump.model.DoubtResultMapper;
import group9.trump.model.DoubtResult;
import group9.trump.model.DoubtChamberMapper;
import group9.trump.model.DoubtChamber;
import group9.trump.service.AsyncDoubtField;
import group9.trump.service.AsyncDoubtWait;

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
  DoubtChamberMapper DCMapper;

  @Autowired
  AsyncDoubtField asyncDoubtField;

  @Autowired
  AsyncDoubtWait asyncDoubtWait;

  @GetMapping("/doubt")
  public String doubt(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    Chamber chamber = CMapper.selectByName(loginUser);
    model.addAttribute("chamber", chamber);
    return "doubt.html";
  }

  @GetMapping("/doubtMatch")
  public String doubtMatch(Principal prin, ModelMap model) {
    int flag = 0;
    String loginUser = prin.getName();
    ArrayList<DoubtChamber> DChambers = DCMapper.selectAll();
    DoubtChamber DChamber = DCMapper.selectByName(loginUser);
    if (DChamber == null) {
      if (DChambers.size() >= 4) {
        flag = 1;
        return "memorySkip.html";
      }
    }

    if (DChamber == null && flag == 0) {
      asyncDoubtWait.syncInsertDoubtChamber(loginUser);
      DChamber = DCMapper.selectByName(loginUser);
    }

    DChambers = DCMapper.selectAll();
    model.addAttribute("user", loginUser);
    model.addAttribute("chamber", DChamber);
    model.addAttribute("DChambers", DChambers);
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
    if ((DCMapper.selectByName(loginUser)).getOya() == true) {
      asyncDoubtWait.syncUpdateOyaFalse();
    }

    int i = 0;
    ArrayList<Tehuda> Tehuda = new ArrayList<>();
    ArrayList<Tehuda> trumps = TMapper.selectAllTehuda();
    Collections.shuffle(trumps);

    while (52 > Tehuda.size()) {
      Tehuda.add(trumps.get(i));
      i++;
    }

    for (int k = 0; k < Tehuda.size(); k = k + 4) {
      TTMapper.insertTehuda(Tehuda.get(k).getNumber(), Tehuda.get(k).getMark(), DCMapper.selectNameById(1));
      TTMapper.insertTehuda(Tehuda.get(k + 1).getNumber(), Tehuda.get(k + 1).getMark(), DCMapper.selectNameById(2));
      TTMapper.insertTehuda(Tehuda.get(k + 2).getNumber(), Tehuda.get(k + 2).getMark(), DCMapper.selectNameById(3));
      TTMapper.insertTehuda(Tehuda.get(k + 3).getNumber(), Tehuda.get(k + 3).getMark(), DCMapper.selectNameById(4));
    }

    int turn = 13;
    int gameturn = 0;
    String nextname = DCMapper.selectNameById(1);
    asyncDoubtField.syncDoubtField(0, "♠", turn, loginUser, nextname, gameturn);
    model.addAttribute("nextname", nextname);
    Tehuda = TTMapper.selectAllOrder();
    model.addAttribute("tehuda", Tehuda);
    ArrayList<Deck> trump = DMapper.selectAll();
    model.addAttribute("trump", trump);
    DoubtResult result = DRMapper.selectOne();
    model.addAttribute("result", result);
    final Field field = this.asyncDoubtField.syncShowDoubt();
    model.addAttribute("field", field);

    return "doubtFight.html";
  }

  @GetMapping("/doubtFight")
  public String doubtFight1(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);

    final Field field = this.asyncDoubtField.syncShowDoubt();
    model.addAttribute("field", field);

    DoubtResult result = DRMapper.selectOne();
    model.addAttribute("result", result);

    ArrayList<Tehuda> Tehuda = TTMapper.selectAllOrder();
    model.addAttribute("tehuda", Tehuda);

    String name1 = DCMapper.selectNameById(1);
    String name2 = DCMapper.selectNameById(2);
    String name3 = DCMapper.selectNameById(3);
    String name4 = DCMapper.selectNameById(4);

    int name1TehudaCount = TTMapper.selectTehudaCount(name1);
    int name2TehudaCount = TTMapper.selectTehudaCount(name2);
    int name3TehudaCount = TTMapper.selectTehudaCount(name3);
    int name4TehudaCount = TTMapper.selectTehudaCount(name4);
    String name1Name = DCMapper.selectNameById(1);
    String name2Name = DCMapper.selectNameById(2);
    String name3Name = DCMapper.selectNameById(3);
    String name4Name = DCMapper.selectNameById(4);
    model.addAttribute("name1TehudaCount", name1TehudaCount);
    model.addAttribute("name2TehudaCount", name2TehudaCount);
    model.addAttribute("name3TehudaCount", name3TehudaCount);
    model.addAttribute("name4TehudaCount", name4TehudaCount);
    model.addAttribute("name1", name1Name);
    model.addAttribute("name2", name2Name);
    model.addAttribute("name3", name3Name);
    model.addAttribute("name4", name4Name);

    return "doubtFight.html";
  }

  @PostMapping("/doubtFight")
  public String doubtFight2(Principal prin, ModelMap model, @RequestParam int selectedcard) {
    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);
    String nextname = FMapper.selectNextOne();

    if (nextname.equals(loginUser)) {
      int turn = FMapper.selectTurnOne();
      int gameturn = FMapper.selectGameTurnOne();
      gameturn++;
      turn++;
      if (turn == 14) {
        turn = 1;
      }
      Tehuda putcard = TTMapper.selectById(selectedcard);
      TTMapper.deleteTehudaById(selectedcard);

      String name1 = DCMapper.selectNameById(1);
      String name2 = DCMapper.selectNameById(2);
      String name3 = DCMapper.selectNameById(3);
      String name4 = DCMapper.selectNameById(4);

      if (nextname.equals(name4)) {
        nextname = name1;
      } else if (nextname.equals(name1)) {
        nextname = name2;
      } else if (nextname.equals(name2)) {
        nextname = name3;
      } else if (nextname.equals(name3)) {
        nextname = name4;
      }

      int name1TehudaCount = TTMapper.selectTehudaCount(name1);
      int name2TehudaCount = TTMapper.selectTehudaCount(name2);
      int name3TehudaCount = TTMapper.selectTehudaCount(name3);
      int name4TehudaCount = TTMapper.selectTehudaCount(name4);
      String name1Name = DCMapper.selectNameById(1);
      String name2Name = DCMapper.selectNameById(2);
      String name3Name = DCMapper.selectNameById(3);
      String name4Name = DCMapper.selectNameById(4);
      model.addAttribute("name1TehudaCount", name1TehudaCount);
      model.addAttribute("name2TehudaCount", name2TehudaCount);
      model.addAttribute("name3TehudaCount", name3TehudaCount);
      model.addAttribute("name4TehudaCount", name4TehudaCount);
      model.addAttribute("name1", name1Name);
      model.addAttribute("name2", name2Name);
      model.addAttribute("name3", name3Name);
      model.addAttribute("name4", name4Name);

      if (FMapper.selectNumberOne() == 0) {
        FMapper.deleteField();
      }
      model.addAttribute("nextname", nextname);
      asyncDoubtField.syncDoubtField(putcard.getNumber(), putcard.getMark(), turn, loginUser, nextname, gameturn);
      final Field field = this.asyncDoubtField.syncShowDoubt();
      model.addAttribute("field", field);
      ArrayList<Tehuda> Tehuda = TTMapper.selectAllOrder();
      model.addAttribute("tehuda", Tehuda);
      DoubtResult result = DRMapper.selectOne();
      model.addAttribute("result", result);
      if (TTMapper.selectTehudaCount(loginUser) == 0) {
        asyncDoubtField.syncDoubtField(putcard.getNumber(), putcard.getMark(), turn, loginUser, "<<ゲーム終了>>", gameturn);
        Chamber chamber = CMapper.selectByName(loginUser);
        CMapper.updateWin(chamber.getWin() + 1, loginUser);
        chamber = CMapper.selectByName(loginUser);
        model.addAttribute("chamber", chamber);
        DCMapper.deleteAll();
        return "doubtEnd.html";
      }
      return "doubtFight.html";
    }
    DoubtResult result = DRMapper.selectOne();
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

  @GetMapping("/doubtWaitSse")
  public SseEmitter doubtWaitSse() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncDoubtWait.asyncReloadDoubtMatch(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("/doubtCall")
  public String doubtCall(Principal prin, ModelMap model) {
    String judge = "";
    String user = "";
    int i = 1;

    String name1 = DCMapper.selectNameById(1);
    String name2 = DCMapper.selectNameById(2);
    String name3 = DCMapper.selectNameById(3);
    String name4 = DCMapper.selectNameById(4);
    int name1TehudaCount = TTMapper.selectTehudaCount(name1);
    int name2TehudaCount = TTMapper.selectTehudaCount(name2);
    int name3TehudaCount = TTMapper.selectTehudaCount(name3);
    int name4TehudaCount = TTMapper.selectTehudaCount(name4);
    String name1Name = TTMapper.selectNameById(1);
    String name2Name = TTMapper.selectNameById(2);
    String name3Name = TTMapper.selectNameById(3);
    String name4Name = TTMapper.selectNameById(4);
    model.addAttribute("name1TehudaCount", name1TehudaCount);
    model.addAttribute("name2TehudaCount", name2TehudaCount);
    model.addAttribute("name3TehudaCount", name3TehudaCount);
    model.addAttribute("name4TehudaCount", name4TehudaCount);
    model.addAttribute("name1", name1Name);
    model.addAttribute("name2", name2Name);
    model.addAttribute("name3", name3Name);
    model.addAttribute("name4", name4Name);

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

    int gameturn = FMapper.selectGameTurnOne();
    DRMapper.insertResult(judge, user, gameturn);
    DoubtResult result = DRMapper.selectOne();
    model.addAttribute("result", result);
    int turn = FMapper.selectTurnOne();
    String nextname = FMapper.selectNextOne();
    FMapper.deleteField();
    asyncDoubtField.syncDoubtField(0, "♠", turn, loginUser, nextname, gameturn);
    field = this.asyncDoubtField.syncShowDoubt();
    // DRMapper.insertResult(result,user);
    model.addAttribute("field", field);
    ArrayList<Tehuda> Tehuda = TTMapper.selectAllOrder();
    model.addAttribute("tehuda", Tehuda);
    return "doubtFight.html";
  }
}
