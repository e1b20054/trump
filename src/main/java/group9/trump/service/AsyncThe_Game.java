package group9.trump.service;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import group9.trump.model.The_Game_FieldMapper;
import group9.trump.model.The_Game_Field;

@Service
public class AsyncThe_Game {
  boolean dbUpdated = false;

  private final Logger logger = LoggerFactory.getLogger(AsyncThe_Game.class);

  @Autowired
  The_Game_FieldMapper TGFMapper;

  /**
   * 購入対象の果物IDの果物をDBから削除し，購入対象の果物オブジェクトを返す
   *
   * @param id 購入対象の果物のID
   * @return 購入対象の果物のオブジェクトを返す
   */
  @Transactional
  public void syncThe_Game(int ba, int number) {

    TGFMapper.insertField(ba, number);

    // 非同期でDB更新したことを共有する際に利用する
    this.dbUpdated = true;
  }

  public ArrayList<The_Game_Field> syncShowThe_Game(int ba) {
    return TGFMapper.selectAll(ba);
  }

  /**
   * dbUpdatedがtrueのときのみブラウザにDBからフルーツリストを取得して送付する
   *
   * @param emitter
   */
  @Async
  public void asyncShowThe_Game(SseEmitter emitter) {
    dbUpdated = true;
    try {
      while (true) {// 無限ループ
        // DBが更新されていなければ0.5s休み
        if (false == dbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        // DBが更新されていれば更新後のフルーツリストを取得してsendし，1s休み，dbUpdatedをfalseにする
        int ba = TGFMapper.selectLastNumber();
        ArrayList<The_Game_Field> field = this.syncShowThe_Game(ba);
        emitter.send(field);
        TimeUnit.MILLISECONDS.sleep(1000);
        dbUpdated = false;
      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowThe_Game complete");
  }

}
