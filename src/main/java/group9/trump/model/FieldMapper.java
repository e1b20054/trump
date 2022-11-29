package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;


@Mapper
public interface FieldMapper {
  @Select("SELECT * from field;")
  ArrayList<Field> selectAll();

  @Insert("INSERT INTO Field (number,mark) VALUES (#{number},#{mark});")
  void insertField(int number, String mark);

  @Delete("DELETE FROM Field")
  boolean deleteField();

  @Select("SELECT * FROM Field ORDER BY id DESC LIMIT 1;")
  Field selectFieldOne();


}
