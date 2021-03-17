package com.bytecub.plugin.es.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bytecub.plugin.es.SampleEsClient;
import com.bytecub.plugin.es.config.enums.BcEsMetaType;
import com.bytecub.plugin.es.config.enums.KeyMatchTypeEnum;
import com.bytecub.plugin.es.domain.EsRange;
import com.bytecub.plugin.es.domain.PropertiesItem;
import com.bytecub.plugin.es.domain.SearchItem;
import com.bytecub.plugin.es.utils.JsonUtil;
import com.bytecub.utils.JSONProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: SampleEsClientImpl.java, v 0.1 2020-12-11  Exp $$  
 */
@Slf4j
@Service
public class SampleEsClientImpl implements SampleEsClient {
    private static final String SHARDS = "index.number_of_shards";
    private static final String COPY = "index.number_of_replicas";
    private static final String REFRESH = "index.refresh_interval";
    @Value("${bytecub.es.shards:5}")
    Integer shards;
    @Value("${bytecub.es.replicas:1}")
    Integer replicas;
    @Value("${bytecub.es.refresh:1s}")
    String refresh;
    @Autowired
    RestHighLevelClient client;
    @Autowired
    ElasticsearchRestTemplate esTemplate;

    @Override
    public Boolean createIndexTemplate(String templateName, String indexAliases, String router, List<String> indexPatterns,
        Map<String, PropertiesItem> properties) {
        PutIndexTemplateRequest request = new PutIndexTemplateRequest(templateName);
        request.patterns(indexPatterns);
        request
            .settings(Settings.builder().put(SHARDS, this.shards).put(COPY, this.replicas).put(REFRESH, this.refresh));

        Map<String, Object> jsonMap = new HashMap<>();
        {
            Map<String, Object> property = new HashMap<>();
            {
                properties.forEach((key, value) -> {
                    String valueString = JSONProvider.toJSONString(value);
                    property.put(key, JSONProvider.fromString(valueString));
                });

            }
            jsonMap.put("properties", property);
        }
        request.mapping(jsonMap);
        // .searchRouting("xyz")
        if (!StringUtils.isEmpty(indexAliases)) {
            Alias alias = new Alias(indexAliases);
//            if(!StringUtils.isEmpty(router)){
//                alias.routing(router);
//            }
            request.alias(alias);
        }

        try {
            AcknowledgedResponse putTemplateResponse = client.indices().putTemplate(request, RequestOptions.DEFAULT);
            return putTemplateResponse.isAcknowledged() ? true : false;
        } catch (Exception e) {
            log.warn("创建ES模版异常", e);
            return false;
        }

    }

    @Override
    public Boolean createIndex(String indexName, Map<String, BcEsMetaType> sourceMap) {
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            String source = this.map2Source(sourceMap);
            createIndexRequest.mapping(source, XContentType.JSON);
            createIndexRequest.settings(this.builder());
            CreateIndexResponse createIndexResponse =
                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            boolean ack = createIndexResponse.isAcknowledged();
            log.info("创建索引[{}] 结果为:{}", indexName, createIndexResponse);
            return ack;
        } catch (ElasticsearchStatusException ee) {
            if (ee.getDetailedMessage().contains("resource_already_exists_exception")) {
                return true;
            }
            log.warn("创建索引[{}]异常{}", indexName, sourceMap, ee);
            return false;
        } catch (Exception e) {
            log.warn("创建索引[{}]异常{}", indexName, sourceMap, e);
            return false;
        }
    }

    @Override
    public <T> Boolean addDoc(String indexName, T data, String routing) {
        long start = System.currentTimeMillis();
        Boolean result = false;
        try {
            IndexRequest request = null;
            if (StringUtils.isNotEmpty(routing)) {
                request = new IndexRequest(indexName).routing(routing);
            } else {
                request = new IndexRequest(indexName);
            }
            IndexResponse response = executeRequest(request, data);
            result = !StringUtils.isEmpty(response.getId()) ? true : false;
        } catch (Exception e) {
            log.warn("添加文档到索引[{}]异常{}", indexName, data, e);
            result = false;
        }
        long ms = System.currentTimeMillis() - start;
        log.info("为索引{} 耗时{}ms存储数据到es", indexName, ms);
        return result;
    }

    /** 暂时是直接删除索引 */
    @Override
    public Boolean reindex(String source, String target) {
        return esTemplate.indexOps(IndexCoordinates.of(source)).delete();
    }

    @Override
    public Boolean dropIndex(String indexName) {
        return esTemplate.indexOps(IndexCoordinates.of(indexName)).delete();
    }

    @Override
    public <Y, T> List<T> queryByCondition(String index, String route, Y y, Class<T> clazz) throws Exception {
        SearchRequest searchRequest = new SearchRequest(index);
        // searchRequest.routing(route);
        String requestSearch = JsonUtil.toJSONString(y);
        Map<String, Object> map = JSON.parseObject(requestSearch);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        map.forEach((key, value) -> {
            boolQuery.must(QueryBuilders.matchQuery(key, value));
        });
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.sort("bcsArrivedTime", SortOrder.DESC);
        SearchResponse searchResponse =
            client.search(searchRequest.source(searchSourceBuilder), RequestOptions.DEFAULT);
        return returnResponse(searchResponse, clazz);
    }

    @Override
    public <T> List<T> searchByPage(String[] indices, String route, List<SearchItem> conditionList, Class<T> clazz,
        int fromId, int size, String sortName, SortOrder orderBy, List<EsRange> rangeList) throws Exception {
        List<T> list = new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = this.buildQuery(conditionList, rangeList);
        if (null != boolQuery) {
            searchSourceBuilder.query(boolQuery);
        }
        searchSourceBuilder.from(fromId);
        searchSourceBuilder.size(size);
        // SortOrder.DESC
        searchSourceBuilder.sort(sortName, orderBy);
        SearchRequest searchRequest = new SearchRequest(indices, searchSourceBuilder);
        if (StringUtils.isEmpty(route)) {
            searchRequest.routing(route);
        }
        SearchResponse searchResponse =
            client.search(searchRequest.source(searchSourceBuilder), RequestOptions.DEFAULT);
        return returnResponse(searchResponse, clazz);
    }

    @Override
    public long count(String[] indices, String route, List<SearchItem> conditionList, List<EsRange> rangeList)
        throws Exception {
        BoolQueryBuilder boolQueryBuilder = this.buildQuery(conditionList, rangeList);
        CountRequest countRequest = new CountRequest(indices, boolQueryBuilder);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        return countResponse.getCount();
    }

    private BoolQueryBuilder buildQuery(List<SearchItem> conditionList, List<EsRange> rangeList) {
        if (CollectionUtils.isEmpty(conditionList) && CollectionUtils.isEmpty(rangeList)) {
            return null;
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if(!CollectionUtils.isEmpty(conditionList)){
            for (SearchItem searchItem : conditionList) {
                if (KeyMatchTypeEnum.NOT_EXIST.equals(searchItem.getMatchType())) {
                    QueryBuilders.existsQuery(searchItem.getKey());
                    boolQuery.must(QueryBuilders.existsQuery(searchItem.getKey()));
                } else {
                    boolQuery.must(QueryBuilders.matchQuery(searchItem.getKey(), searchItem.getValue()));
                }
            }
        }

        if(!CollectionUtils.isEmpty(rangeList)){
            for (EsRange range : rangeList) {
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(range.getField());
                if (null != range.getMin()) {
                    rangeQueryBuilder.gte(range.getMin());
                }
                if (null != range.getMax()) {
                    rangeQueryBuilder.lte(range.getMax());
                }
                boolQuery.filter(rangeQueryBuilder);
            }
        }
        return boolQuery;
    }

    private <T> List<T> returnResponse(SearchResponse searchResponse, Class<T> clazz) {
        if (null != searchResponse.getHits()) {
            List<T> responseList = new ArrayList<T>();
            for (SearchHit searchHit : searchResponse.getHits()) {
                Map<String, Object> sourceMap = searchHit.getSourceAsMap();
                sourceMap.put("id", searchHit.getId());
                String sourceString = JSONObject.toJSONString(sourceMap);
                responseList.add(JSONObject.parseObject(sourceString, clazz));
            }
            return responseList;
        }
        return null;
    }

    private String map2Source(Map<String, BcEsMetaType> sourceMap) {
        if (CollectionUtils.isEmpty(sourceMap)) {
            return null;
        }
        Map<String, Map<String, String>> propMap = new HashMap<>();
        Map<String, Map<String, Map<String, String>>> resultMap = new HashMap<>();
        sourceMap.forEach((key, item) -> {
            Map<String, String> propItem = new HashMap<>();
            propItem.put("type", item.getType());
            propMap.put(key, propItem);
        });
        resultMap.put("properties", propMap);

        return JsonUtil.toJSONString(resultMap);
    }

    private Settings.Builder builder() {
        Settings.Builder builder =
            Settings.builder().put("index.number_of_shards", shards).put("index.number_of_replicas", replicas);
        return builder;
    }

    private <T> IndexResponse executeRequest(IndexRequest request, T t) throws IOException {
        String jsonSource = JSONObject.toJSONString(t);
        request.source(jsonSource, XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        return response;
    }
}
