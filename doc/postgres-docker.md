### Деплой postgres в docker

`sudo apt update`

#### [Установка и проверка docker](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-20-04)

`sudo apt install docker.io`

или

`sudo apt install apt-transport-https ca-certificates curl software-properties-common`  
`curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -`  
`sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"`  
`sudo apt install docker-ce` - установка

----

`sudo systemctl status docker` - проверка

#### [Запуск postgres в docker](https://hub.docker.com/_/postgres)

`sudo docker pull postgres` - установка образа postgres из репозитория docker hub

  ```
  sudo docker run \
	-p 5432:5432 \
	--name postgres-db \
	-e POSTGRES_USER=jira \
	-e POSTGRES_PASSWORD=JiraRush \
	-e POSTGRES_DB=jira \
	-e PGDATA=/var/lib/postgresql/data/pgdata \
	-v ./pgdata:/var/lib/postgresql/data \
	-d postgres
  ```

-- Запуск postgres в docker-контейнере, где:

  ```
  -p 5432:5432 - порт, на котором запускается база данных
  --name postgres-db - имя docker-контейнера
  -e POSTGRES_PASSWORD=JiraRush - пароль от базы данных
  -e POSTGRES_USER=jira - имя учетной записи пользователя базы данных
  -e POSTGRES_DB=jira - название базы данных
  -e PGDATA=/var/lib/postgresql/data/pgdata - папка, где будут храниться данные
  ```

`sudo docker update --restart unless-stopped postgres-db` - автозапуск контейнера после рестарта сервера

#### Креденшиалы для подключения к базе данных

  ```
  1. host - ip-адрес сервера
  2. port - 5432
  3. username - jira
  4. password - JiraRush
  5. table - jira
  ```


