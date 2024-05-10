package io.github.javpower.milvus.demo;

import com.google.common.collect.Lists;
import io.github.javpower.milvus.demo.model.Face;
import io.github.javpower.milvus.demo.test.FaceMilvusMapper;
import io.github.javpower.milvus.plus.model.MilvusResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationRunnerTest implements ApplicationRunner {
    @Autowired
    private FaceMilvusMapper mapper;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Float> vector = Lists.newArrayList(0.1f,0.2f,0.3f);
        MilvusResp<Face> resp = mapper.lambda()
                .eq(Face::getPersonId,1l)
                .vector(vector)
                .limit(100l)
                .query();
    }
}