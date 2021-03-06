
## Lealone是什么

* 是一个对等的(P2P)、面向OLTP场景的分布式关系数据库


## Lealone名字的由来

* Lealone 发音 ['li:ləʊn] 这是我新造的英文单词， <br>
  灵感来自于办公桌上那些叫绿萝的室内植物，一直想做个项目以它命名。 <br>
  绿萝的拼音是lv luo，与Lealone英文发音有点相同，<br>
  Lealone是lea + lone的组合，反过来念更有意思哦。:)


## Lealone有哪些特性
 
* 插件化存储引擎架构，内置[WiredTiger](https://github.com/wiredtiger/wiredtiger/tree/develop)和[MVStore](http://www.h2database.com/html/mvstore.html)存储引擎

* 支持ACID、高性能分布式事务，<br>
  使用一种非常新颖的[基于局部时间戳的多版本冲突与有效性检测的分布式事务模型](https://github.com/codefollower/Lealone/wiki/Lealone-transaction-model)
  
* 支持JDBC 4.0规范

* 对[H2数据库](http://www.h2database.com/html/main.html)的SQL引擎进行了大量的改进和扩展

* SQL语法类似MySQL、PostgreSQL，支持索引、视图、Join、子查询 <br>
  支持触发器、自定义函数、Order By、Group By、聚合


## Lealone文档

* 用户文档

* 设计文档

* [开发文档](https://github.com/codefollower/Lealone/wiki/Lealone%E5%BC%80%E5%8F%91%E8%80%85%E6%96%87%E6%A1%A3)


## License

* [License](https://github.com/codefollower/Lealone/blob/master/LICENSE.txt)

