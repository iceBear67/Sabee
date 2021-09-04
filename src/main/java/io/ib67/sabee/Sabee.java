package io.ib67.sabee;

import lombok.Getter;

import javax.sql.DataSource;

@Getter
public class Sabee {
    private DataSource dataSource; // connection pool
    public Sabee(DataSource dataSource){
        this.dataSource=dataSource;
    }

}
