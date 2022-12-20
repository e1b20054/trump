package group9.trump.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import group9.trump.model.MemoryDeckMapper;
import group9.trump.model.MemoryDeck;
import group9.trump.model.MemoryChamberMapper;
import group9.trump.model.MemoryChamber;

@Service
public class AsyncMemoryFight {
  boolean mddbUpdated = false;
  boolean mcdbUpdated = false;


  @Autowired
  MemoryDeckMapper MDMapper;

  @Autowired
  MemoryChamberMapper MCMapper;

  private final Logger logger = LoggerFactory.getLogger(AsyncMemoryFight.class);

  public void syncUpdateByOpenTrueId(int id){
    MDMapper.updateByOpenTrueId(id);
    this.mddbUpdated = true;
  }

  public void syncUpdateByGetTrueId(int id_1, int id_2, String name) {
    MDMapper.updateByGetTrueId(id_1, name);
    MDMapper.updateByGetTrueId(id_2, name);
    MCMapper.updateByGet((MCMapper.selectByName(name).getGet())+2, name);
    this.mddbUpdated = true;
  }

  public void syncUpdateByOpenFalseId(int id_1, int id_2){
    MDMapper.updateByOpenFalseId(id_1);
    MDMapper.updateByOpenFalseId(id_2);
    this.mddbUpdated = true;
  }

  public void syncMatchEndMatch(String name){
    MCMapper.updateByIsActive(false, MCMapper.selectByName(name).getId());
    MDMapper.updateByEndMatch();
    this.mcdbUpdated = true;
  }

  @Async
  public void asyncMemoryFight(SseEmitter emitter) {
    logger.info("start connection");
    this.mddbUpdated = true;
    try {
      while (true) {
        if (false == this.mddbUpdated && false == this.mcdbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        emitter.send(MDMapper.selectAll());
        TimeUnit.MILLISECONDS.sleep(1000);
        emitter.send(MDMapper.selectAll());
        this.mddbUpdated = false;
        this.mcdbUpdated = false;
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncMemoryFight complete");
  }
}
