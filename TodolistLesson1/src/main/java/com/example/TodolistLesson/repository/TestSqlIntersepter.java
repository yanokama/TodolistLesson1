package com.example.TodolistLesson.repository;

import java.util.List;
import java.util.Properties;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Intercepts({
	@Signature(type = Executor.class
			, method = "query"
			, args = {MappedStatement.class, Object.class
					, RowBounds.class, ResultHandler.class})
})
@Component
public class TestSqlIntersepter implements Interceptor{

	//SQLにマッピングされるパラメータ名に対応するパラメータ値を取得するために
    //JexlEngineオブジェクトを利用
    private JexlEngine engine = new JexlBuilder().create();		
    
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		//返却オブジェクトを定義
        Object returnObj = null;
        
        //SQL実行メソッドを実行し、その開始・終了時刻も取得
        long start = System.currentTimeMillis();
        returnObj = invocation.proceed();
        long end = System.currentTimeMillis();
        
	    //SQL実行時間、実行メソッド名(フルパス)を出力
	    MappedStatement statement = (MappedStatement)invocation.getArgs()[0];
	    String[] items = statement.getId().split("\\.");
	    log.info("実行時間: {} ms. 実行メソッド: {}"
	    		, end-start, String.join(".", items));

        //SQLログを出力
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = statement.getBoundSql(parameter);
        log.info("@@@---Mybatisカスタムログテスト----@@@");
        log.info("Preparing: {}", shapingSql(boundSql.getSql()));
        log.info("Parameters: {}", getParameterValues(boundSql));
               
        return returnObj;
	}
	
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
 
    @Override
    public void setProperties(Properties properties) {
 
    }	
	
	
    /**
     * 引数のSQL文から、改行コード(LF)と連続する空白文字を除く
     * @param beforeSql SQL文
     * @return 整形後のSQL文
     */
    private String shapingSql(String beforeSql){
    	if( beforeSql == null || "".equals(beforeSql)){
            return beforeSql;
        }
        return beforeSql.replaceAll("\n", "")
                .replaceAll(" {2,}", " ");
    }
    
    /**
     * 引数のBoundSqlから、SQLに埋め込まれるパラメータ値を取得する
     * @param boundSql BoundSqlオブジェクト
     * @return SQLに埋め込まれるパラメータ値
     */
    //ToDo: パラメータ取得のところでワーニング出ているので原因調査して解消する
    private String getParameterValues(final BoundSql boundSql) {
        //返却用戻り値を格納
        StringBuilder builder = new StringBuilder();
        //パラメータ値を取得
        Object param = boundSql.getParameterObject();
        if(param == null){
            //パラメータの設定が無い場合は、(No Params.)を返却
            builder.append("(No Params.)");
            return builder.toString();
        }
        //SQLにマッピングされるパラメータ名を取得
        List<ParameterMapping> paramMapping = boundSql.getParameterMappings();
        //SQLにマッピングされるパラメータ名に対応するパラメータ値を取得し、
        //返却用戻り値に順次格納する
        for (ParameterMapping mapping : paramMapping) {
            String propValue = mapping.getProperty();
            builder.append(propValue + "=");
            try {
                //SQLにマッピングされるパラメータ名に対応するパラメータ値を取得
            	builder.append(engine.getProperty(param, propValue));
            	builder.append(", ");
            } catch (JexlException e) {
                //パラメータ値がLong型の場合等はJexlExceptionが発生するため
                //パラメータ値をそのまま指定する
                builder.append(param);
                builder.append(", ");
                continue;
            }
        }
        //返却用戻り値の最後のカンマを除く
        if(builder.lastIndexOf(", ") > 0){
            builder.deleteCharAt(builder.lastIndexOf(", "));
        }
        return builder.toString();
    }
	
}
