package lzlz.controller;

import com.google.common.collect.Maps;
import lzlz.dao.entity.TestDO;
import lzlz.dao.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用于测试连接
 */
@Controller
@RequestMapping("test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TestMapper testMapper;

    @RequestMapping("db")
    public ModelAndView testDb() {

        String sql = "SELECT * FROM test";
        List<Map<String, String>> queryList = jdbcTemplate.query(sql, (rs, rowNum) -> {

            HashMap<String, String> map = Maps.newHashMap();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnLabel = metaData.getColumnLabel(i);
                String columValue = rs.getString(i);
                map.put(columnLabel, columValue);
            }
            return map;
        });

        Optional<String> reduce = queryList.stream()
                .map(item -> item.toString() + System.lineSeparator())
                .reduce((all, item) -> all + item);

        String value = reduce.orElse("");

        return new ModelAndView("test/common-test").addObject("value", value);
    }

    /**
     * redis连接测试
     */
    @RequestMapping("redis")
    public ModelAndView testRedis() {

        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        valueOps.set("1", "hello,redis");
        String value = valueOps.get("1");
        return new ModelAndView("test/common-test").addObject("value", value);
    }

    /**
     * mybatis连接测试
     */
    @RequestMapping("mybatis")
    public ModelAndView testMybatis() {
//        String result = Joiner.on(System.lineSeparator())
//                .join(testMapper
//                        .selectAll()
//                        .stream()
//                        .map(TestDO::toString)
//                        .collect(Collectors
//                                .toList()));
        String result = testMapper.selectAll()
                .stream()
                .map(TestDO::toString)
                .reduce((prev,add)->prev+System.lineSeparator()+add).orElse("");
        return new ModelAndView("test/common-test").addObject("value", result);
    }
}
