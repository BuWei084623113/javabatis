package com.javabatis;

import com.javabatis.bean.TableInfo;
import com.javabatis.builder.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RunApplication {
    private static Logger logger = LoggerFactory.getLogger(RunApplication.class);
    public static void main(String[] args){
       List<TableInfo> tableInfoList =  BuildTable.getTable();
       BuildBase.execute();
//       logger.info("table:{}",tableInfoList);
//        准备生成文件
        for (TableInfo tableInfo : tableInfoList){
            BuildPo.execute(tableInfo);
            BuildQuery.execute(tableInfo);
            BuildMapper.execute(tableInfo);
            BuildMapperXml.execute(tableInfo);
            BuildService.execute(tableInfo);
            BuildServiceImpl.execute(tableInfo);
            BuildController.execute(tableInfo);
        }
    }
}
