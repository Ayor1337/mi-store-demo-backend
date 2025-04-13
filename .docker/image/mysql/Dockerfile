FROM mysql:9.2.0
ADD ./store_schema.sql /docker-entrypoint-initdb.d/store_schema.sql
RUN chown -R mysql:mysql /docker-entrypoint-initdb.d/store_schema.sql
EXPOSE 3306
CMD ["mysqld", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci"]