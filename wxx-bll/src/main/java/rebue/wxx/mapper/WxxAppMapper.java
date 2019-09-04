package rebue.wxx.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import rebue.robotech.mapper.MybatisBaseMapper;
import rebue.wxx.mo.WxxAppMo;

@Mapper
public interface WxxAppMapper extends MybatisBaseMapper<WxxAppMo, String> {

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int deleteByPrimaryKey(String id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insert(WxxAppMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int insertSelective(WxxAppMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    WxxAppMo selectByPrimaryKey(String id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKeySelective(WxxAppMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int updateByPrimaryKey(WxxAppMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<WxxAppMo> selectAll();

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    List<WxxAppMo> selectSelective(WxxAppMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existByPrimaryKey(String id);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    boolean existSelective(WxxAppMo record);

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    int countSelective(WxxAppMo record);
}
