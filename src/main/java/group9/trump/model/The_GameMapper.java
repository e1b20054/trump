package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface The_GameMapper {
  @Select("SELECT * from the_game where id = #{id};")
  The_Game selectById(int id);

  @Select("SELECT number from the_game;")
  ArrayList<Integer> selectAll();

}
