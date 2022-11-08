FROM mysql:8.0.30
#ADD https://raw.githubusercontent.com/alibaba/nacos/develop/distribution/conf/mysql-schema.sql /docker-entrypoint-initdb.d/nacos-mysql.sql
ADD ../../../sql/nacos.sql /docker-entrypoint-initdb.d/nacos.sql
ADD ../../../sql/matrix.sql /docker-entrypoint-initdb.d/matrix.sql
ADD ../../../sql/seata.sql /docker-entrypoint-initdb.d/seata.sql
ADD ../../../sql/tables_xxl_job.sql /docker-entrypoint-initdb.d/tables_xxl_job.sql
RUN chown -R mysql:mysql /docker-entrypoint-initdb.d
EXPOSE 3306
CMD ["mysqld", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci"]