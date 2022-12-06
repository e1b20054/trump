package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface The_Game_Field2Mapper {

  @Select("SELECT * from the_game_field2;")
  ArrayList<The_Game_Field2> selectAll();

  @Insert("INSERT INTO the_game_field2 (number) VALUES (#{number});")
  void insertField2(int number);

  @Select("SELECT number from the_game_field2 order by id desc limit 1;")
  int selectNumber();

  @Delete("DELETE FROM the_game_field2 WHERE number =#{number}")
  boolean deleteField2(int number);

}
