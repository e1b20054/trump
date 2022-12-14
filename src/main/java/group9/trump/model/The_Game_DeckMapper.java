package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface The_Game_DeckMapper {
  @Select("SELECT number from the_game_deck;")
  ArrayList<Integer> selectAll();

  @Insert("INSERT INTO the_game_deck (number) VALUES (#{number});")
  void insertDeck(int number);

  @Delete("DELETE FROM the_game_deck WHERE number =#{number}")
  boolean deleteDeck(int number);

  @Delete("DELETE FROM the_game_deck")
  boolean delete();

  @Select("SELECT count(*) from the_game_deck;")
  int selectCount();

}
