package rebue.wxx.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import rebue.robotech.mapper.MybatisBaseMapper;
import rebue.wxx.mo.WxxMchMo;

@Mapper
public interface WxxMchMapper extends MybatisBaseMapper<WxxMchMo, String> {
    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int deleteByPrimaryKey(String id);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insert(WxxMchMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insertSelective(WxxMchMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    WxxMchMo selectByPrimaryKey(String id);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKeySelective(WxxMchMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKey(WxxMchMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<WxxMchMo> selectAll();

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<WxxMchMo> selectSelective(WxxMchMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existByPrimaryKey(String id);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existSelective(WxxMchMo record);

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int countSelective(WxxMchMo record);
}