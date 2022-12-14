package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface FieldMapper {
  @Select("SELECT * from field where id = #{id};")
  Field selectById(int id);

  @Select("SELECT * from field;")
  ArrayList<Field> selectAll();

  @Insert("INSERT INTO Field (number,mark,turn,name,nextname,gameturn) VALUES (#{number},#{mark},#{turn},#{name},#{nextname},#{gameturn});")
  void insertField(int number, String mark, int turn, String name, String nextname, int gameturn);

  @Delete("DELETE FROM Field")
  boolean deleteField();

  @Select("SELECT * FROM Field ORDER BY id DESC LIMIT 1;")
  Field selectFieldOne();

  @Select("SELECT * FROM Field ORDER BY id LIMIT 1;")
  Field selectFieldFirst();

  @Select("SELECT turn FROM Field ORDER BY id DESC LIMIT 1;")
  int selectTurnOne();

  @Select("SELECT gameturn FROM Field ORDER BY id DESC LIMIT 1;")
  int selectGameTurnOne();

  @Select("SELECT number FROM Field ORDER BY id DESC LIMIT 1;")
  int selectNumberOne();

  @Select("SELECT name FROM Field ORDER BY id DESC LIMIT 1;")
  String selectUserOne();

  @Select("SELECT nextname FROM Field ORDER BY id DESC LIMIT 1;")
  String selectNextOne();

}
