package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface The_Game_FieldMapper {

  @Select("SELECT ba, number from the_game_field where ba =#{ba};")
  ArrayList<The_Game_Field> selectAll(int ba);

  @Insert("INSERT INTO the_game_field (ba, number) VALUES (#{ba},#{number});")
  void insertField(int ba, int number);

  @Select("SELECT number from the_game_field where ba =#{ba} order by id desc limit 1;")
  int selectNumber(int ba);

  @Select("SELECT ba from the_game_field order by id desc limit 1;")
  int selectLastNumber();

  @Delete("DELETE FROM the_game_field WHERE number =#{number};")
  boolean deleteField(int number);

}
