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
//import group9.trump.model.MemoryDeck;

@Service
public class AsyncMemoryWait {
  boolean mcdbUpdated = false;
  boolean mddbUpdated = false;
  int id = 1;

  @Autowired
  MemoryChamberMapper MCMapper;

  @Autowired
  MemoryDeckMapper MDMapper;

  private final Logger logger = LoggerFactory.getLogger(AsyncMemoryWait.class);

  public void syncInsertMemoryChamber(String name) {
    ArrayList<MemoryChamber> MChamber = MCMapper.selectAll();
    if (MChamber.size() == 0) {
      id = 1;
      MCMapper.insertMemoryChamber(id, name, true, true, false, 0);
      id++;
    } else {
      MCMapper.insertMemoryChamber(id, name, false, false, false, 0);
      id++;
    }
    this.mcdbUpdated = true;
  }

  public void syncDeleteMemoryDeck() {
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
}
