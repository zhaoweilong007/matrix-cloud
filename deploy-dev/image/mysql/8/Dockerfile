FROM mysql:8.0.33
#ADD https://raw.githubusercontent.com/alibaba/nacos/develop/distribution/conf/mysql-schema.sql /docker-entrypoint-initdb.d/nacos-mysql.sql
ADD ../../../sql/20230926/nacos.sql /docker-entrypoint-initdb.d/nacos.sql
ADD ../../../sql/20230926/seata.sql /docker-entrypoint-initdb.d/seata.sql
ADD ../../../sql/20230926/tables_xxl_job.sql /docker-entrypoint-initdb.d/tables_xxl_job.sql
RUN chown -R mysql:mysql /docker-entrypoint-initdb.d
EXPOSE 3306
CMD ["mysqld", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci"]