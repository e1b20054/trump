package group9.trump.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface TurnMapper {

  @Select("SELECT id FROM turn ORDER BY id DESC LIMIT 1;")
  Turn selectTurnId();

  @Select("SELECT value FROM turn ORDER BY id DESC LIMIT 1;")
  Turn selectTurnValue();

  @Insert("INSERT INTO turn (value) VALUES (#{value});")
  void insertValue(int value);

}
