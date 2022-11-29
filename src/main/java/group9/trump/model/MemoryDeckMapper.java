package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemoryDeckMapper {
  @Select("SELECT * from MemoryDeck where id = #{id};")
  MemoryDeck selectById(int id);

  @Select("SELECT * from MemoryDeck;")
  ArrayList<MemoryDeck> selectAll();

  @Insert("INSERT INTO MemoryDeck (number,mark,open,get) VALUES (#{number},#{mark},'FALSE','FALSE');")
  void insertMemoryDeck(int number, String mark);

  @Delete("DELETE FROM MemoryDeck WHERE number =#{number}")
  boolean deleteByNumber(int number);

  @Select("SELECT * from MemoryDeck WHERE id >= #{front} and id <= #{back};")
  ArrayList<MemoryDeck> selectAllByArea(int front, int back);

  @Update("UPDATE MemoryDeck SET open = 'TRUE' WHERE id = #{id};")
  void updateByOpenTrueId(int id);

  @Update("UPDATE MemoryDeck SET open = 'FALSE' WHERE id = #{id};")
  void updateByOpenFalseId(int id);

  @Select("SELECT * from MemoryDeck where open = TRUE;")
  ArrayList<MemoryDeck> selectByOpenTrue();

  @Select("SELECT * from MemoryDeck where get = TRUE;")
  ArrayList<MemoryDeck> selectByGetTrue();

  @Update("UPDATE MemoryDeck SET get = 'TRUE' WHERE id = #{id};")
  void updateByGetTrueId(int id);

  @Delete("DELETE FROM MemoryDeck;")
  void deleteAll();
}
