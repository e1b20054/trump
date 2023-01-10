package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface DoubtResultMapper {

  @Select("SELECT * FROM DoubtResult ORDER BY id DESC LIMIT 1;")
  DoubtResult selectOne();

  @Insert("INSERT INTO DoubtResult (judge,name,gameturn) VALUES (#{judge},#{name},#{gameturn});")
  void insertResult(String judge, String name, int gameturn);

  @Delete("DELETE FROM DoubtResult;")
  boolean deleteResult();

}
