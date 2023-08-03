https://github.com/cptntotoro/java-explore-with-me/pull/5

# Explore with me - микросервисное приложение-афиша мероприятий
Java, Spring Boot, Spring JPA, PostgreSQL, REST API, Docker, Mockito, JUnit, MapStruct, Lombok

## О проекте
Приложение для размещения мероприятий и поиска компаний для участия в них

![](https://pictures.s3.yandex.net/resources/S19_09-2_1674558748.png)

Приложение содержит четыре микросервиса: 
- main-service для бизнес-логики
- stats-service для сбора статистики просмотра событий по ip 
- две базы данных для каждого сервиса
  
Каждый микросервис запускается в собственном Docker контейнере.

## Основная функциональность: 

### Авторизованные пользователи 
- добавление в приложение новые мероприятия, редактировать их и просматривать после добавления
- подача заявок на участие в интересующих мероприятиях
- создатель мероприятия может подтверждать заявки, которые отправили другие пользователи сервиса

### Администраторы
- добавление, изменение и удаление категорий для событий
- добавление, удаление и закрепление на главной странице подборки мероприятий
- модерация событий, размещённых пользователями, — публикация или отклонение
- управление пользователями — добавление, активация, просмотр и удаление

## Эндпоинты

### main-service 

- POST /users/{userId}/events - Добавление нового события
- GET /users/{userId}/events/{eventId} - Получение информации о событии, добавленном текущим пользователем
- PATCH /users/{userId}/events/{eventId} - Изменение события добавленного текущим пользователем
- GET /users/{userId}/events - Получение событий, добавленных текущим пользователем
- GET /users/{userId}/events/{eventId}/requests - Получение запросов на участие в событии текущего пользователя
- PATCH /users/{userId}/events/{eventId}/requests - Изменение статуса (подтверждение, отмена) заявок на участие в событии текущего пользователя
<br>

- GET /categories - Получение категорий
- GET /categories/{catId} - Получение информации о категории по её идентификатору
<br>

- GET /compilations - Получение подборок событий
- GET /compilations/{compId} - Получение подборки событий по его id
<br>

- GET /admin/events - Поиск событий по параметрам запроса в любой комбинации:
    - users - список id пользователей
    - states - список статусов события (PENDING, PUBLISHED, CANCELED)
    - categories - список id категорий событий
    - rangeStart - начало временного отрезка в формате yyyy-MM-dd HH:mm:ss
    - rangeEnd - конец временного отрезка в формате yyyy-MM-dd HH:mm:ss
    - from - параметр для пагинации
    - size - параметр для пагинации
- PATCH /admin/events/{eventId} - Редактирование данных события 
<br>

- GET /events - Поиск событий по параметрам запроса в любой комбинации:
    - text - текст для поиска в названии и описании событий
    - categories - список id категорий событий
    - paid - только платные события (true/false)
    - rangeStart - начало временного отрезка в формате yyyy-MM-dd HH:mm:ss
    - rangeEnd - конец временного отрезка в формате yyyy-MM-dd HH:mm:ss
    - onlyAvailable - только доступные события, т.е. у которых еще не исчерпан лимит участников (true/false)
    - sort - способ сортировки событий (EVENT_DATE, VIEWS)
    - from - параметр для пагинации
    - size - параметр для пагинации
- GET /events/{id} - Получение подробной информации об опубликованном событии по его идентификатору
<br>

- GET /users/{userId}/requests - Получение информации о заявках текущего пользователя на участие в чужих событиях
- POST /users/{userId}/requests - Добавление запроса от текущего пользователя на участие в событии
- DELETE /users/{userId}/requests/{requestId}/cancel - Отмена своего запроса на участие в событии
<br>

- POST /admin/users - Добавление нового пользователя
- GET /admin/users - Получение информации о пользователях
- DELETE /admin/users/{userId} - Удаление пользователя
- POST /admin/compilations - Добавление новой подборки
- DELETE /admin/compilations/{compId} - Удаление подборки
- PATCH /admin/compilations/{compId} - Обновить информацию о подборке
- POST /admin/categories Добавление новой категории
- GET /admin/categories/{catId} Получение списка бронирований для всех вещей текущего пользователя
- DELETE /admin/categories/{catId} Удаление категории
<br>

- POST /users/{userId}/events/{eventId}/comments - Добавление комментария к событию
- PATCH /users/{userId}/events/{eventId}/comments/{commentId} - Обновление комментария 
- GET /users/{userId}/events/{eventId}/comments/{commentId} - Получение комментария к событию
- DELETE /users/{userId}/events/{eventId}/comments/{commentId} - Удаление комментария к событию
- GET /users/{userId}/events/{eventId}/comments - Получение списка комментариев пользователя к событию
- GET /users/{userId}/comments - Получение всех комментариев пользователя
<br>

- POST /users/{userId}/events/{eventId}/comments - добавление комментария пользователем событию
- PATCH /users/{userId}/events/{eventId}/comments - редактирование пользователем собственного комментария
- DELETE /users/{userId}/events/{eventId}/comments/{commentId} - удаление комментария пользователем

### stats-service 

- GET /stats - Получение статистики по посещениям
- POST /hit - Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем

## Как использовать:
Ознакомиться с примерами использования можно в [этой коллекции тестов Postman](https://github.com/yandex-praktikum/java-explore-with-me/tree/main_svc/postman)


