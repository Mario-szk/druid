/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.bvt.sql.oceanbase;

import com.alibaba.druid.sql.MysqlTest;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import org.junit.Assert;

import java.util.List;

public class OceanbaseCreateTableTest_rangePartition3 extends MysqlTest {
    public void test_0() throws Exception {
        String sql = "CREATE TABLE employees ( " //
                + "id INT NOT NULL, " //
                + "fname VARCHAR(30), " //
                + "lname VARCHAR(30), " //
                + "hired DATE NOT NULL DEFAULT '1970-01-01', " //
                + "separated DATE NOT NULL DEFAULT '9999-12-31', " //
                + "job_code INT NOT NULL, store_id INT NOT NULL " //
                + ") PARTITION BY RANGE (job_code) " //
                + "( PARTITION p0 VALUES LESS THAN (100), " //
                + "PARTITION p1 VALUES LESS THAN (1000), " //
                + "PARTITION p2 VALUES LESS THAN (10000) "
                + ")"; //

        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();
        SQLStatement stmt = stmtList.get(0);

        {
            String result = SQLUtils.toMySqlString(stmt);
            Assert.assertEquals("CREATE TABLE employees ("
                            + "\n\tid INT NOT NULL,"
                            + "\n\tfname VARCHAR(30),"
                            + "\n\tlname VARCHAR(30),"
                            + "\n\thired DATE NOT NULL DEFAULT '1970-01-01',"
                            + "\n\tseparated DATE NOT NULL DEFAULT '9999-12-31',"
                            + "\n\tjob_code INT NOT NULL,"
                            + "\n\tstore_id INT NOT NULL"
                            + "\n)"
                            + "\nPARTITION BY RANGE COLUMNS (job_code) ("
                            + "\n\tPARTITION p0 VALUES LESS THAN (100),"
                            + "\n\tPARTITION p1 VALUES LESS THAN (1000),"
                            + "\n\tPARTITION p2 VALUES LESS THAN (10000)"
                            + "\n)",
                    result);
        }
        {
            String result = SQLUtils.toMySqlString(stmt, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION);
            Assert.assertEquals("create table employees ("
                            + "\n\tid INT not null,"
                            + "\n\tfname VARCHAR(30),"
                            + "\n\tlname VARCHAR(30),"
                            + "\n\thired DATE not null default '1970-01-01',"
                            + "\n\tseparated DATE not null default '9999-12-31',"
                            + "\n\tjob_code INT not null,"
                            + "\n\tstore_id INT not null"
                            + "\n)"
                            + "\npartition by range columns (job_code) ("
                            + "\n\tpartition p0 values less than (100),"
                            + "\n\tpartition p1 values less than (1000),"
                            + "\n\tpartition p2 values less than (10000)"
                            + "\n)",
                    result);
        }

        Assert.assertEquals(1, stmtList.size());

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        stmt.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(7, visitor.getColumns().size());
        Assert.assertEquals(0, visitor.getConditions().size());

        // Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("t_basic_store")));

    }
}
