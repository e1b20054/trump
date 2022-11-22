package group9.trump.model;

import java.util.ArrayList;

//import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChamberMapper {

  @Select("SELECT * from chamber where id = #{id};")
  Chamber selectById(int id);

  @Select("SELECT * from chamber;")
  ArrayList<Chamber> selectAll();

  @Select("SELECT * from chamber where name = #{name};")
  Chamber selectByName(String name);
}
