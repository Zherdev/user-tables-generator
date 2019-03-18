User Tables Generator v1.2
==========================

Приложение предназначено для генерации таблиц со случайными пользовательскими данными.
Для генерации используется [API](http://randomuser.ru/). Пользователи, полученные от API, сохраняются в базу данных MySQL. Если API недоступно, используются данные из базы. Пользователи, сгенерированные из базы данных, могут повторяться - обратное не указано в требованиях. В случае, если нет доступа к API, а база пуста, генерация происходит локально.
Результат работы приложения: xlsx-таблица, pdf-таблица, log-файл.


Файл для подключения БД
-----------------------

В каталоге src\main\resources создайте файл db.txt в кодировке UTF-8. Его структура построчно:

```
url
datebaseName
userName
password
```

Пример:

```
jdbc:mysql://localhost:3306
utb_db
admin
123
```


Сборка
------

Для сборки перейдите в директорию с исходными файлами приложения и выполните команду:

```
mvn clean compile assembly:single
```

В каталоге target\ будет создан исполняемый файл UserTablesGenerator-1.2-jar-with-dependencies.jar


Запуск
------

Вывод в консоль осуществляется в кодировке UTF-8. Для корректного отображения в cmd предварительно выполните команду:

```
chcp 65001
```
Для запуска User Tables Generator выполните команду:

```
java -jar target\UserTablesGenerator-1.2-jar-with-dependencies.jar
```

Выходные файлы будут сохранены в директорию, из которой производится запуск.


Автор
-----

Жердев Иван

http://www.zherdev.tech
