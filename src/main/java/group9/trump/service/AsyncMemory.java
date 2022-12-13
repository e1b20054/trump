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

@Service
public class AsyncMemory {
  boolean mcdbUpdated = false;

  @Autowired
  MemoryChamberMapper MCMapper;

  private final Logger logger = LoggerFactory.getLogger(AsyncMemory.class);

  public void syncInsertMemoryChamber(String name) {
    ArrayList<MemoryChamber> MChamber = MCMapper.selectAll();
    if (MChamber.size() == 0) {
      MCMapper.insertMemoryChamber(name, true);
    } else {
      MCMapper.insertMemoryChamber(name, false);
      this.mcdbUpdated = true;
    }
  }

  @Async
  public void asyncReloadMemoryMatch(SseEmitter emitter) {
    logger.info("start connection");
    mcdbUpdated = true;
    try {
      while (true) {
        if (false == mcdbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        ArrayList<MemoryChamber> memoryChamber = MCMapper.selectAll();
        emitter.send(memoryChamber);
        TimeUnit.MILLISECONDS.sleep(1000);
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
