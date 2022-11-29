package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface The_Game_Field4Mapper {
  @Select("SELECT max(id) from the_game_field4;")
  int selectMaxId();

  @Select("SELECT number from the_game_field3 where id = (SELECT max(id) from the_game_field3);")
  int selectNumber();

  @Select("SELECT * from the_game_field4;")
  ArrayList<The_Game_Field4> selectAll();

  @Insert("INSERT INTO the_game_field4 (number) VALUES (#{number});")
  void insertField4(int number);

  @Delete("DELETE FROM the_game_field4 WHERE id =#{id}")
  boolean deleteField4(int id);

}
