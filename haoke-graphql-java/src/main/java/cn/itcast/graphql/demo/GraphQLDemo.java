package cn.itcast.graphql.demo;

import cn.itcast.graphql.vo.User;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

import static graphql.Scalars.*;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

public class GraphQLDemo {

    public static void main(String[] args) {

        /**
         * type User { #定义对象}
         */
        GraphQLObjectType userObjectType = newObject()
                .name("User")
                .field(newFieldDefinition().name("id").type(GraphQLLong))
                .field(newFieldDefinition().name("name").type(GraphQLString))
                .field(newFieldDefinition().name("age").type(GraphQLInt))
                .build();

        /**
         * user : User #指定对象以及参数类型
         */
        GraphQLFieldDefinition userFieldDefinition = newFieldDefinition()
                .name("user")
                .type(userObjectType)
                .argument(newArgument().name("id").type(GraphQLLong).build())
                .dataFetcher(environment -> {
                    Long id = environment.getArgument("id");
                    // 查询数据库了
                    // TODO 先模式实现
                    return  new User(id, "张三:"+id, 20+id.intValue(), null);
                })
//                .dataFetcher(new StaticDataFetcher(new User(1L, "张三", 20)))
                .build();

        /**
         * type UserQuery { #定义查询的类型}
         */
        GraphQLObjectType userQueryObjectType = newObject()
                .name("UserQuery")
                .field(userFieldDefinition)
                .build();

        /**
         * schema { #定义查询 }
         */
        GraphQLSchema graphQLSchema = GraphQLSchema.newSchema().query(userQueryObjectType).build();

        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();

        String query = "{user(id:100){id,name}}";
        ExecutionResult result = graphQL.execute(query);

        System.out.println("query:" + query);
//        System.out.println(result.getErrors());
//        System.out.println(result.getData());

        System.out.println(result.toSpecification());

    }
}
