package com.matrix;

import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import com.matrix.annotation.EnableMatrix;
import com.matrix.es.domain.Document;
import com.matrix.es.mapper.DocumentMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/11/3 14:26
 **/
@SpringBootApplication
@EnableMatrix
@Slf4j
public class DemoApp {
    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class, args);
    }


    @Autowired
    DocumentMapper documentMapper;

    @XxlJob(value = "testJob")
    public void testJob() {
        XxlJobHelper.log("run test job.............thread name:{}", Thread.currentThread().getName());
        log.info("run test job.............thread name:{}", Thread.currentThread().getName());

        Document document = new Document();
        document.setContent("test");
        document.setTitle("es test");

        documentMapper.insert(document);

        List<Document> documents = documentMapper.selectList(new LambdaEsQueryWrapper<>());
        log.info("search list of documents:{}", documents);
    }
}
