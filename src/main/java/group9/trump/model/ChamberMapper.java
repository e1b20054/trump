package group9.trump.model;

import java.util.ArrayList;

//import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ChamberMapper {

  @Select("SELECT * from chamber where id = #{id};")
  Chamber selectById(int id);

  @Select("SELECT * from chamber;")
  ArrayList<Chamber> selectAll();

  @Select("SELECT * from chamber where name = #{name};")
  Chamber selectByName(String name);

  @Select("SELECT id from chamber where name = #{name};")
  int selectIdByName(String name);

  @Update("UPDATE chamber SET win = #{win} where name = #{name};")
  void updateWin(int win, String name);
}
