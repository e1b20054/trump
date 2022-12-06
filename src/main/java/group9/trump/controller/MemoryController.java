package group9.trump.controller;

import java.security.Principal;
import java.util.ArrayList;
//import java.util.Collections;

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
import group9.trump.service.AsyncMemory;

@Controller
public class MemoryController {

  int openCnt = 0;
  int roomNo = 1;

  @Autowired
  ChamberMapper CMapper;

  @Autowired
  TrumpMapper TMapper;

  @Autowired
  MemoryDeckMapper MDMapper;

  @Autowired
  MemoryChamberMapper MCMapper;

  @Autowired
  AsyncMemory asyncMemory;

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
    ArrayList<MemoryChamber> MChambers = MCMapper.selectByRoomNo(roomNo);
    if (MChambers.size() >= 4) {
      return "memorySkip.html";
    }
    asyncMemory.syncInsertMemoryChamber(loginUser, roomNo);
    MemoryChamber MChamber = MCMapper.selectByName(loginUser);
    MChambers = MCMapper.selectByRoomNo(roomNo);
    model.addAttribute("user", loginUser);
    model.addAttribute("chamber", MChamber);
    model.addAttribute("MChambers", MChambers);
    return "memoryMatch.html";
  }

  @GetMapping("/memoryFight")
  public String memoryFight(Principal prin, ModelMap model) {
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
    return "memoryFight.html";
  }

  @GetMapping("/select")
  public String select(@RequestParam int id, Principal prin, ModelMap model) {
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
    return "memoryFight.html";
  }

  @GetMapping("/memoryNext")
  public String next(Principal prin, ModelMap model) {
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
      CMapper.updateWin(chamber.getWin() + 1);
      chamber = CMapper.selectByName(loginUser);
      model.addAttribute("chamber", chamber);
      return "memoryEnd.html";
    }
    return "memoryFight.html";
  }

  @GetMapping("/memoryWaitSse")
  public SseEmitter memoryWaitSse() {
    final SseEmitter sseEmitter = new SseEmitter();
    // this.asyncKekka.asyncShowkekka(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("/memorySelectSse")
  public SseEmitter memorySelectSse() {
    final SseEmitter sseEmitter = new SseEmitter();
    // this.asyncKekka.asyncShowkekka(sseEmitter);
    return sseEmitter;
  }
}
