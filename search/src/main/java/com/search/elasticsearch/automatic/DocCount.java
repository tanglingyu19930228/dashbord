package com.search.elasticsearch.automatic;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 描述：TODO
 * Author：linyue
 * Date：2020/4/21
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class DocCount<T extends Doc> {
    T data;
    long count;
}
