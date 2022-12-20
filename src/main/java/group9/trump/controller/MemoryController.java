package group9.trump.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import group9.trump.model.ChamberMapper;
import group9.trump.model.Chamber;
import group9.trump.model.TrumpMapper;
import group9.trump.model.Trump;
import group9.trump.model.MemoryDeckMapper;
import group9.trump.model.MemoryDeck;
import group9.trump.model.MemoryChamberMapper;
import group9.trump.model.MemoryChamber;
import group9.trump.service.AsyncMemoryWait;
import group9.trump.service.AsyncMemoryFight;

@Controller
public class MemoryController {

  int openCnt = 0;

  @Autowired
  ChamberMapper CMapper;

  @Autowired
  TrumpMapper TMapper;

  @Autowired
  MemoryDeckMapper MDMapper;

  @Autowired
  MemoryChamberMapper MCMapper;

  @Autowired
  AsyncMemoryWait asyncMemoryWait;

  @Autowired
  AsyncMemoryFight asyncMemoryFight;

  @GetMapping("/memory")
  public String memory(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    Chamber chamber = CMapper.selectByName(loginUser);
    model.addAttribute("chamber", chamber);
    return "memory.html";
  }

  @GetMapping("/memoryMatch")
  public String memoryMatch(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    ArrayList<MemoryDeck> Decks = MDMapper.selectAll();
    if (Decks.size() != 0 && Decks.get(0).getEndMatch() == true) {
      MCMapper.deleteAll();
    }
    ArrayList<MemoryChamber> MChambers = MCMapper.selectAll();
    if (MChambers.size() >= 4) {
      return "memorySkip.html";
    }
    MemoryChamber MChamber = MCMapper.selectByName(loginUser);
    if (MChamber == null) {
      asyncMemoryWait.syncInsertMemoryChamber(loginUser);
      MChamber = MCMapper.selectByName(loginUser);
    }
    MChambers = MCMapper.selectAll();
    model.addAttribute("user", loginUser);
    model.addAttribute("chamber", MChamber);
    model.addAttribute("MChambers", MChambers);
    return "memoryMatch.html";
  }

  @GetMapping("/memoryFight")
  public String memoryFight(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    MCMapper.updateByIsActive(true, MCMapper.selectByName(loginUser).getId());
    ArrayList<Trump> trump = TMapper.selectAllByNotJoker();
    if ((MCMapper.selectByName(loginUser)).getOya() == true) {
      asyncMemoryWait.syncDeleteMemoryDeck();
      // Collections.shuffle(trump);
      for (int j = 0; j < trump.size(); j++) {
        MDMapper.insertMemoryDeck(trump.get(j).getNumber(), trump.get(j).getMark());
      }
    }
    MDMapper.deleteByNumber(0);
    ArrayList<MemoryDeck> Deck = MDMapper.selectAll();
    model.addAttribute("Deck", Deck);
    model.addAttribute("user", loginUser);
    MemoryChamber userChamber = MCMapper.selectByName(loginUser);
    model.addAttribute("userChamber", userChamber);
    ArrayList<MemoryChamber> MChambers = MCMapper.selectAll();
    if (MChambers.size() == 1) {
      return "memoryFight1.html";
    }
    return "memoryFight.html";
  }

  @GetMapping("/memoryFight1")
  public String memoryFight1(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    ArrayList<Trump> trump = TMapper.selectAllByNotJoker();
    MDMapper.deleteAll();
    // Collections.shuffle(trump);
    for (int j = 0; j < trump.size(); j++) {
      MDMapper.insertMemoryDeck(trump.get(j).getNumber(), trump.get(j).getMark());
    }
    MDMapper.deleteByNumber(0);
    ArrayList<MemoryDeck> Deck = MDMapper.selectAll();
    model.addAttribute("Deck", Deck);
    model.addAttribute("user", loginUser);
    return "memoryFight1.html";
  }

  @GetMapping("/memorySelect")
  public String memorySelect(@RequestParam int id, Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    MemoryChamber userChamber = MCMapper.selectByName(loginUser);
    model.addAttribute("userChamber", userChamber);
    if (userChamber.getNow() == false) {
      ArrayList<MemoryDeck> Deck = MDMapper.selectAll();
      model.addAttribute("Deck", Deck);
      model.addAttribute("user", loginUser);
      return "memoryFight.html";
    }
    if (openCnt == 0) {
      asyncMemoryFight.syncUpdateByOpenTrueId(id);
      ArrayList<MemoryDeck> Deck = MDMapper.selectAll();
      model.addAttribute("Deck", Deck);
      model.addAttribute("user", loginUser);
      openCnt++;
    } else if (openCnt == 1) {
      asyncMemoryFight.syncUpdateByOpenTrueId(id);
      ArrayList<MemoryDeck> Deck = MDMapper.selectAll();
      model.addAttribute("Deck", Deck);
      model.addAttribute("user", loginUser);
      model.addAttribute("openCnt", openCnt);
      openCnt++;
    } else {
      ArrayList<MemoryDeck> Deck = MDMapper.selectAll();
      model.addAttribute("Deck", Deck);
      model.addAttribute("user", loginUser);
      model.addAttribute("openCnt", openCnt);
    }
    return "memoryFight.html";
  }

  @GetMapping("/memorySelect1")
  public String memorySelect1(@RequestParam int id, Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    if (openCnt == 0) {
      MDMapper.updateByOpenTrueId(id);
      ArrayList<MemoryDeck> Deck = MDMapper.selectAll();
      model.addAttribute("Deck", Deck);
      model.addAttribute("user", loginUser);
      openCnt++;
    } else if (openCnt == 1) {
      MDMapper.updateByOpenTrueId(id);
      ArrayList<MemoryDeck> Deck = MDMapper.selectAll();
      model.addAttribute("Deck", Deck);
      model.addAttribute("user", loginUser);
      model.addAttribute("openCnt", openCnt);
      openCnt++;
    } else {
      ArrayList<MemoryDeck> Deck = MDMapper.selectAll();
      model.addAttribute("Deck", Deck);
      model.addAttribute("user", loginUser);
      model.addAttribute("openCnt", openCnt);
    }
    return "memoryFight1.html";
  }

  @GetMapping("/memoryNext")
  public String memoryNext(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    ArrayList<MemoryDeck> openTrue = MDMapper.selectByOpenTrue();
    if (openTrue.get(0).getNumber() == openTrue.get(1).getNumber()) {
      asyncMemoryFight.syncUpdateByGetTrueId(openTrue.get(0).getId(), openTrue.get(1).getId(), loginUser);
    } else {
      int nowId = MCMapper.selectByNowTrueId();
      int nextId = nowId;
      ArrayList<MemoryChamber> MChambers = MCMapper.selectAll();
      if (nowId == MChambers.size()) {
        nextId = 1;
      } else {
        nextId++;
      }
      MCMapper.updateByNow(true, nextId);
      MCMapper.updateByNow(false, nowId);
    }
    MDMapper.updateByOpenFalseId(openTrue.get(0).getId());
    MDMapper.updateByOpenFalseId(openTrue.get(1).getId());
    asyncMemoryFight.syncUpdateByOpenFalseId(openTrue.get(0).getId(), openTrue.get(1).getId());
    ArrayList<MemoryDeck> Deck = MDMapper.selectAll();
    model.addAttribute("Deck", Deck);
    model.addAttribute("user", loginUser);
    MemoryChamber userChamber = MCMapper.selectByName(loginUser);
    model.addAttribute("userChamber", userChamber);
    openCnt = 0;
    ArrayList<MemoryDeck> getTrump = MDMapper.selectByGetTrue();
    if (getTrump.size() == 52) {
      asyncMemoryFight.syncMatchEndMatch(loginUser);
      ArrayList<MemoryChamber> Rank = MCMapper.selectRank();
      MemoryChamber winner = MCMapper.selectTop();
      if (loginUser != "admin" && loginUser.equals(winner.getName())) {
        Chamber chamber = CMapper.selectByName(loginUser);
        CMapper.updateWin(chamber.getWin() + 1, loginUser);
      }
      model.addAttribute("Rank", Rank);
      model.addAttribute("winner", winner);
      return "memoryEnd.html";
    }
    return "memoryFight.html";
  }

  @GetMapping("/memoryEnd")
  public String memoryEnd(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    asyncMemoryFight.syncMatchEndMatch(loginUser);
    ArrayList<MemoryChamber> Rank = MCMapper.selectRank();
    MemoryChamber winner = MCMapper.selectTop();
    if (loginUser != "admin" && loginUser.equals(winner.getName())) {
      Chamber chamber = CMapper.selectByName(loginUser);
      CMapper.updateWin(chamber.getWin() + 1, loginUser);
    }
    model.addAttribute("Rank", Rank);
    model.addAttribute("winner", winner);
    return "memoryEnd.html";
  }

  @GetMapping("/memoryNext1")
  public String memoryNext1(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    ArrayList<MemoryDeck> openTrue = MDMapper.selectByOpenTrue();
    if (openTrue.get(0).getNumber() == openTrue.get(1).getNumber()) {
      MDMapper.updateByGetTrueId(openTrue.get(0).getId(), loginUser);
      MDMapper.updateByGetTrueId(openTrue.get(1).getId(), loginUser);
    }
    MDMapper.updateByOpenFalseId(openTrue.get(0).getId());
    MDMapper.updateByOpenFalseId(openTrue.get(1).getId());
    ArrayList<MemoryDeck> Deck = MDMapper.selectAll();
    model.addAttribute("Deck", Deck);
    model.addAttribute("user", loginUser);
    openCnt = 0;
    ArrayList<MemoryDeck> getTrump = MDMapper.selectByGetTrue();
    if (getTrump.size() == 52) {
      Chamber chamber = CMapper.selectByName(loginUser);
      CMapper.updateWin(chamber.getWin() + 1, loginUser);
      chamber = CMapper.selectByName(loginUser);
      model.addAttribute("chamber", chamber);
      return "memoryEnd1.html";
    }
    return "memoryFight1.html";
  }

  @GetMapping("/memoryWaitSse")
  public SseEmitter memoryWaitSse() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncMemoryWait.asyncReloadMemoryMatch(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("/memoryFightSse")
  public SseEmitter memoryFightSse() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncMemoryFight.asyncMemoryFight(sseEmitter);
    return sseEmitter;
  }
}
