package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TrumpMapper {

  @Select("SELECT * from trump where id = #{id};")
  Trump selectById(int id);

  @Select("SELECT * from trump")
  ArrayList<Trump> selectAll();
}
