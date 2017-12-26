package cn.itcast.bos.dao.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 宝宝心里苦丶 on 2017/12/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class StandardRepositoryTest {
    @Autowired
    private StandardRepository standardRepository;

    @Test
    //@Query("from Standard where name=?")
    public void testQuery(){
        System.out.println(standardRepository.queryByName2("66-88公斤"));
    }
    @Test
    @Transactional
    @Rollback(false)
    public void testUpdate(){
        standardRepository.updateMinLength(1,15);
    }

}
