package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface The_Game_Field1Mapper {

  @Select("SELECT * from the_game_field1;")
  ArrayList<The_Game_Field1> selectAll();

  @Insert("INSERT INTO the_game_field1 (number) VALUES (#{number});")
  void insertField1(int number);

  @Select("SELECT number from the_game_field1 order by id desc limit 1;")
  int selectNumber();

  @Delete("DELETE FROM the_game_field1 WHERE number =#{number}")
  boolean deleteField1(int number);

}
