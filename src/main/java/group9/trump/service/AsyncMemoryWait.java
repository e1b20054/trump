package group9.trump.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import group9.trump.model.MemoryChamberMapper;
import group9.trump.model.MemoryChamber;
import group9.trump.model.MemoryDeckMapper;
import group9.trump.model.MemoryDeck;

@Service
public class AsyncMemoryWait {
  boolean mcdbUpdated = false;
  boolean mddbUpdated = false;

  @Autowired
  MemoryChamberMapper MCMapper;

  @Autowired
  MemoryDeckMapper MDMapper;

  private final Logger logger = LoggerFactory.getLogger(AsyncMemoryWait.class);

  public void syncInsertMemoryChamber(String name) {
    ArrayList<MemoryChamber> MChamber = MCMapper.selectAll();
    if (MChamber.size() == 0) {
      MCMapper.insertMemoryChamber(name, true, true, false, 0);
    } else {
      MCMapper.insertMemoryChamber(name, false, false, false, 0);
    }
    this.mcdbUpdated = true;
  }

  public void syncDeleteMemoryDeck(){
    MDMapper.deleteAll();

    MCMapper.updateByOya(false, (MCMapper.selectByOyaTrueId()));
    this.mddbUpdated = true;
  }

  @Async
  public void asyncReloadMemoryMatch(SseEmitter emitter) {
    logger.info("start connection");
    mcdbUpdated = true;
    try {
      while (true) {
        if (false == mcdbUpdated && false == mddbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        ArrayList<MemoryChamber> memoryChamber = MCMapper.selectAll();
        emitter.send(memoryChamber);
        TimeUnit.MILLISECONDS.sleep(1000);
        emitter.send(memoryChamber);
        mcdbUpdated = false;
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncReloadMemoryMatch complete");
  }

  @Async
  public void asyncMemoryFight(SseEmitter emitter) {
    logger.info("start connection");
    mddbUpdated = true;
    try {
      while (true) {
        if (false == mddbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        ArrayList<MemoryDeck> Deck = MDMapper.selectAll();
        emitter.send(MDMapper);
        TimeUnit.MILLISECONDS.sleep(1000);
        mddbUpdated = false;
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncMemoryFight complete");
  }
}
