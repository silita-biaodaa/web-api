package com.silita.common.es.Inter;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

public interface ElasticsearchCrudRepository<T, ID extends Serializable> extends ElasticsearchRepository<T, ID>, PagingAndSortingRepository<T, ID> {

}

