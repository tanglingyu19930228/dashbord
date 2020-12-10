package com.search.bean;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/12/10 22:43
 */

/**
 * 测略可进一步修改
 */
public class TableShardingAlgorithm implements PreciseShardingAlgorithm<String> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        String table = availableTargetNames.stream().findFirst().get();
        for (String tableName : availableTargetNames) {
            String trueTableName = "t_order_" + genderToTableSuffix(String.valueOf(shardingValue.getValue()));
            if (tableName.equals(trueTableName)) {
                table = tableName;
            }
        }
        return table;
    }

    private String genderToTableSuffix(String value) {
        int i = Hashing.murmur3_128(8947189).newHasher().putString(value, Charsets.UTF_8).hash().asInt();
        return String.valueOf(Math.abs(i) % 3);
    }
}
