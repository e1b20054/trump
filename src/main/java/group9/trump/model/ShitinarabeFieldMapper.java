package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
//import org.apache.ibatis.annotations.Options;

@Mapper
public interface ShitinarabeFieldMapper {
  @Insert("INSERT INTO shitinarabeField (number, mark, Field) VALUES (#{number}, #{mark}, #{Field})")
  void insertShitinarabeField(int number, String mark, Boolean Field);

  @Select("SELECT * from shitinarabeField;")
  ArrayList<ShitinarabeField> selectAll();

  @Update("UPDATE shitinarabeField SET Field = TRUE where mark = #{mark} and number = #{number};")
  void updateField(String mark, int number);

}
