В настройках nginx `cat /etc/nginx/nginx.conf` есть сканирование активных
конфигураций: `include /etc/nginx/sites-enabled/*;`
В каталоге `/etc/nginx/sites-enabled/` если линк на профиль по умолчанию: `ls -la /etc/nginx/sites-enabled/`
[Меняем его на наш профиль](https://unix.stackexchange.com/a/152000/216630):

```
cd /etc/nginx/sites-enabled/
sudo ln -sfn /opt/jirarush/config/nginx.conf default
cat default
sudo service nginx reload (start/stop)
```