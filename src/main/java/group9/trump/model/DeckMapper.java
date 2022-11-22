package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface DeckMapper {
  @Select("SELECT * from deck where id = #{id};")
  Deck selectById(int id);

  @Select("SELECT * from deck;")
  ArrayList<Deck> selectAll();

  @Insert("INSERT INTO deck (number,mark) VALUES (#{number},#{mark});")
  void insertDeck(int number,String mark);

  @Delete("DELETE FROM deck WHERE number =#{number}")
  boolean deleteDeck(int number);

}
