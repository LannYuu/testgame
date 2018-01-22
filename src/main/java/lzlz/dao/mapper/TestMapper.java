package lzlz.dao.mapper;

import lzlz.dao.entity.TestDO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface TestMapper extends Mapper<TestDO> {
    
}
