source .env
docker run --name cs240-chess-mysql -e MYSQL_ROOT_PASSWORD=$CS240_DB_PASSWORD -p 3306:3306 -d mysql