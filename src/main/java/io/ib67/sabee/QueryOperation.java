package io.ib67.sabee;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;

/**
 * SQL 语句的一部分，例如 SELECT ×
 * 必须实现 hashCode 的计算
 */
@ApiStatus.AvailableSince("0.0.1")
@Getter
@Setter
@RequiredArgsConstructor
public class QueryOperation {
    /**
     * 实际上的 SQL 语句，参与预编译。
     * @param schema
     */
    private String schema;
    /**
     * 预编译对应的参数
     * @param parameter 参数
     * @return
     */
    private Object[] parameters;
    /**
     * 操作，比如 SELECT
     * @return
     */
    private final String operation;

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 37 + operation.hashCode();
        hash = hash * 37 + schema.hashCode();
        return hash;
    }
}
