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
package com.alibaba.druid.bvt.sql.mysql.select;

import com.alibaba.druid.sql.MysqlTest;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat.Column;
import org.junit.Assert;

import java.util.List;

public class MySqlSelectTest_18 extends MysqlTest {
    public void test_0() throws Exception {
        String sql = "SELECT host.id as id" //
                + ",   host.item_id as itemId" //
                + ",   host.node_id as nodeId" //
                + ",   host.node_type as nodeType" //
                + ",   host.begin_time as beginTime" //
                + ",   host.end_time as endTime" //
                + ",   host.gmt_create as gmtCreate" //
                + ",   host.gmt_modify as gmtModify" //
                + ",   host.reason as reason" //
                + ",   host.creator_id as creatorId" //
                + ",   host.modifier_id as modifierId" //
                + ",   user.name as creator" //
                + ",   user.name as modifier" //
                + ",   user.nick_name as nickName   " //
                + " FROM notice_close_node host left join sys_user user on user.id = host.modifier_id";

        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
//        print(statementList);

        Assert.assertEquals(1, statementList.size());

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
//        System.out.println("coditions : " + visitor.getConditions());
//        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(2, visitor.getTables().size());
        Assert.assertEquals(14, visitor.getColumns().size());
        Assert.assertEquals(2, visitor.getConditions().size());

        Assert.assertTrue(visitor.getColumns().contains(new Column("sys_user", "id")));
        Assert.assertTrue(visitor.getColumns().contains(new Column("notice_close_node", "modifier_id")));
    }
}
